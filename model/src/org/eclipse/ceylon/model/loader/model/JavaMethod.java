package org.eclipse.ceylon.model.loader.model;

import static org.eclipse.ceylon.model.typechecker.model.DeclarationFlags.FunctionFlags.*;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ceylon.model.loader.mirror.MethodMirror;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.Scope;

/**
 * Instance method that allows us to remember the exact method name
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class JavaMethod extends Function implements LocalDeclarationContainer {

    private String realName;
    private boolean defaultedAnnotation;
    public final MethodMirror mirror;
    private Map<String,Declaration> localDeclarations;
    
    @Override
    protected Class<?> getModelClass() {
        return getClass().getSuperclass(); 
    }
    
    public JavaMethod(MethodMirror mirror){
        this.mirror = mirror;
    }
    
    public void setRealName(String name) {
        this.realName = name;
    }

    public String getRealName(){
        return realName;
    }
    
    @Override
    public boolean isVariadic() {
        return (flags&VARIADIC)!=0;
    }
    
    public void setVariadic(boolean variadic) {
        if (variadic) {
            flags|=VARIADIC;
        }
        else {
            flags&=(~VARIADIC);
        }
    }
    
    /**
     * If this is a method on an annotation type, whether the method has a 
     * {@code default} expression;
     */
    public boolean isDefaultedAnnotation() {
        return defaultedAnnotation;
    }
    
    public void setDefaultedAnnotation(boolean defaultedAnnotation) {
        this.defaultedAnnotation = defaultedAnnotation;
    }

    @Override
    public Declaration getLocalDeclaration(String name) {
        if(localDeclarations == null)
            return null;
        return localDeclarations.get(name);
    }

    @Override
    public void addLocalDeclaration(Declaration declaration) {
        if(localDeclarations == null)
            localDeclarations = new HashMap<String, Declaration>();
        localDeclarations.put(declaration.getPrefixedName(), declaration);
    }

    @Override
    public boolean isJava() {
        Scope container = getContainer();
        while(container != null && container instanceof Declaration == false)
            container = container.getContainer();
        return container != null ? ((Declaration) container).isJava() : false;
    }
}
