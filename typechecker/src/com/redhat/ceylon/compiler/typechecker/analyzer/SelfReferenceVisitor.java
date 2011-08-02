package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Validates that the initializer of a class does
 * not leak self-references to the instance being
 * initialized.
 * 
 * @author Gavin King
 *
 */
public class SelfReferenceVisitor extends Visitor {
    
    private final TypeDeclaration typeDeclaration;
    private Tree.Statement lastExecutableStatement;
    private boolean declarationSection = false;
    private int nestedLevel = -1;
    
    public SelfReferenceVisitor(TypeDeclaration td) {
        typeDeclaration = td;
    }

    private void visitExtendedType(Tree.ExtendedTypeExpression that) {
        Declaration member = that.getDeclaration();
        if (member!=null) {
            if ( !declarationSection && that.getScope().getInheritingDeclaration(member)==typeDeclaration ) {
                //TODO: this logic is broken!
                that.addError("inherited member class may not be extended in initializer: " + 
                        member.getName() + " of " + that.getScope().getInheritingDeclaration(member).getName());
            }
        }
    }

    private void visitReference(Tree.Primary that) {
        Declaration member  = that.getDeclaration();
        if (member!=null) {
            if ( !declarationSection && that.getScope().getInheritingDeclaration(member)==typeDeclaration ) {
                //TODO: this logic is broken!
                that.addError("inherited member may not be used in initializer: " + 
                            member.getName() + " of " + that.getScope().getInheritingDeclaration(member).getName());
            }
        }
    }
    
    @Override
    public void visit(Tree.AnnotationList that) {}

    @Override
    public void visit(Tree.ExtendedTypeExpression that) {
        super.visit(that);
        visitExtendedType(that);
    }

    @Override
    public void visit(Tree.BaseMemberExpression that) {
        super.visit(that);
        visitReference(that);
    }

    @Override
    public void visit(Tree.BaseTypeExpression that) {
        super.visit(that);
        visitReference(that);
    }

    @Override
    public void visit(Tree.QualifiedMemberExpression that) {
        super.visit(that);
        if (isSelfReference(that.getPrimary())) {
            visitReference(that);
        }
    }

    @Override
    public void visit(Tree.QualifiedTypeExpression that) {
        super.visit(that);
        if (isSelfReference(that.getPrimary())) {
            visitReference(that);
        }
    }

    private boolean isSelfReference(Tree.Term that) {
        return (nestedLevel==0 && (that instanceof Tree.This || that instanceof Tree.Super))
            || (nestedLevel==1 && that instanceof Tree.Outer);
    }

    @Override
    public void visit(Tree.IsCondition that) {
        super.visit(that);
        if (that.getVariable().getSpecifierExpression()!=null) {
            Tree.Term term = that.getVariable().getSpecifierExpression().getExpression().getTerm();
            if ( isSelfReference(term) ) {
                if (term instanceof Tree.Super ) {
                    term.addError("cannot narrow super");
                }
                else if ( mayNotLeakThis() ) {
                    term.addError("cannot narrow self-reference in initializer");
                }
                else if ( mayNotLeakOuter() ) {
                    term.addError("cannot narrow self-reference in initializer");
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.ObjectDefinition that) {
        if (that.getDeclarationModel().getTypeDeclaration()==typeDeclaration) {
            nestedLevel=0;
            super.visit(that);
            nestedLevel=-1;
        }
        else if (nestedLevel>=0){
            nestedLevel++;
            super.visit(that);
            nestedLevel--;
        }
        else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.ObjectArgument that) {
        if (that.getDeclarationModel().getTypeDeclaration()==typeDeclaration) {
            nestedLevel=0;
            super.visit(that);
            nestedLevel=-1;
        }
        else if (nestedLevel>=0){
            nestedLevel++;
            super.visit(that);
            nestedLevel--;
        }
        else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.TypeDeclaration that) {
        if (that.getDeclarationModel()==typeDeclaration) {
            nestedLevel=0;
            declarationSection = false;
            super.visit(that);
            nestedLevel=-1;
        }
        else if (nestedLevel>=0){
            nestedLevel++;
            super.visit(that);
            nestedLevel--;
        }
        else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.InterfaceBody that) {
        if (nestedLevel==0) {
            declarationSection = true;
            lastExecutableStatement = null;
            super.visit(that);
            declarationSection = false;
        }
        else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.ClassBody that) {
        if (nestedLevel==0) {
            Tree.Statement les = null;
            for (Tree.Statement s: that.getStatements()) {
                if (s instanceof Tree.ExecutableStatement) {
                    les = s;
                }
                else {
                    if (s instanceof Tree.AttributeDeclaration) {
                        if ( ((Tree.AttributeDeclaration) s).getSpecifierOrInitializerExpression()!=null ) {
                            les = s;
                        }
                    }
                    if (s instanceof Tree.MethodDeclaration) {
                        if ( ((Tree.MethodDeclaration) s).getSpecifierExpression()!=null ) {
                            les = s;
                        }
                    }
                }
            }
            declarationSection = les==null;
            lastExecutableStatement = les;
            super.visit(that);
            lastExecutableStatement = null;
            declarationSection = false;
        }
        else {
            super.visit(that);
        }
    }
            
    boolean mayNotLeakThis() {
        return !declarationSection && nestedLevel==0;
    }
    
    boolean mayNotLeakOuter() {
        return !declarationSection && nestedLevel==1;
    }
    
    boolean inBody() {
        return nestedLevel>=0;
    }
    
    @Override
    public void visit(Tree.Statement that) {
        super.visit(that);
        if (nestedLevel==0) {
            declarationSection = declarationSection || 
                    that==lastExecutableStatement;
        }
    }
    
    private void checkSelfReference(Node that, Tree.Term term) {
        if ( term instanceof Tree.Super ) {
            that.addError("leaks super reference");
        }    
        if ( mayNotLeakThis() &&
                term instanceof Tree.This ) {
            that.addError("leaks this reference");
        }    
        if ( mayNotLeakOuter() &&
                term instanceof Tree.Outer ) {
            that.addError("leaks outer reference");
        }
    }

    @Override
    public void visit(Tree.Return that) {
        super.visit(that);
        if ( that.getExpression()!=null && inBody() ) {
            checkSelfReference(that, that.getExpression().getTerm());    
        }
    }

    @Override
    public void visit(Tree.SpecifierOrInitializerExpression that) {
        super.visit(that);
        if ( inBody() ) {
            checkSelfReference(that, that.getExpression().getTerm());    
        }
    }

    @Override
    public void visit(Tree.AssignmentOp that) {
        super.visit(that);
        if ( inBody() ) {
            checkSelfReference(that, that.getRightTerm());    
        }
    }

    @Override
    public void visit(Tree.PositionalArgumentList that) {
        super.visit(that);
        if ( inBody() ) {
            for ( Tree.PositionalArgument arg: that.getPositionalArguments()) {
                checkSelfReference(arg, arg.getExpression().getTerm());    
            }
        }
    }

    @Override
    public void visit(Tree.NamedArgumentList that) {
        super.visit(that);
        if ( inBody() ) {
            for ( Tree.NamedArgument arg: that.getNamedArguments()) {
                if (arg instanceof Tree.SpecifiedArgument) {
                    Tree.SpecifierExpression se = ((Tree.SpecifiedArgument) arg).getSpecifierExpression();
                    checkSelfReference(se, se.getExpression().getTerm());    
                }
            }
        }
    }

}
