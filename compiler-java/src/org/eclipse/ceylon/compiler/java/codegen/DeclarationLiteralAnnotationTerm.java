package org.eclipse.ceylon.compiler.java.codegen;

import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCAnnotation;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;

public class DeclarationLiteralAnnotationTerm extends LiteralAnnotationTerm {
    /** 
     * Sometimes we need an instance just for calling 
     * {@link #makeAtValue(ExpressionTransformer, String, JCExpression)} on.
     */
    public static final LiteralAnnotationTerm FACTORY = new DeclarationLiteralAnnotationTerm(null);
    
    final String value;
    
    public DeclarationLiteralAnnotationTerm(String value) {
        super();
        this.value = value;
    }
    @Override
    public org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeAtValue(
            ExpressionTransformer exprGen, String name, JCExpression value) {
        return exprGen.makeAtDeclarationValue(name, value);
    }
    @Override
    public JCExpression makeLiteral(ExpressionTransformer exprGen) {
        return exprGen.make().Literal(this.value);
    }
    @Override
    public org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeExprs(ExpressionTransformer exprGen, org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> value) {
        return exprGen.makeAtDeclarationExprs(exprGen.make().NewArray(null,  null,  AbstractTransformer.upcastExprList(value)));
    }
    @Override
    public String toString() {
        return value.toString();
    }
}
