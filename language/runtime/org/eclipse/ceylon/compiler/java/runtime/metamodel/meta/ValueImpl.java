/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.metamodel.meta;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.eclipse.ceylon.common.Nullable;
import org.eclipse.ceylon.compiler.java.Util;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.metadata.Variance;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.MethodHandleUtil;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.decl.ValueDeclarationImpl;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;
import org.eclipse.ceylon.model.loader.NamingBase;
import org.eclipse.ceylon.model.loader.impl.reflect.mirror.ReflectionClass;
import org.eclipse.ceylon.model.loader.model.FieldValue;
import org.eclipse.ceylon.model.loader.model.JavaBeanValue;
import org.eclipse.ceylon.model.loader.model.LazyValue;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypedReference;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.ClassOrInterfaceImpl;
import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.ValueImpl;

import ceylon.language.null_;
import ceylon.language.meta.model.IncompatibleTypeException;
import ceylon.language.meta.model.MutationException;
import ceylon.language.meta.model.StorageException;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Get", variance = Variance.OUT),
    @TypeParameter(value = "Set", variance = Variance.IN),
})
public class ValueImpl<Get, Set> 
        implements ceylon.language.meta.model.Value<Get, Set>, ReifiedType {

    private static final Class<?>[] NO_PARAMS = new Class<?>[0];
    
    private final ceylon.language.meta.model.Type<Get> type;
    @Ignore
    protected final TypeDescriptor $reifiedGet;
    @Ignore
    protected final TypeDescriptor $reifiedSet;
    protected final ValueDeclarationImpl declaration;
    private boolean isSuppressed = false;
    private MethodHandle getter;
    private MethodHandle setter;
    private final Object instance;
    private final ceylon.language.meta.model.Type<?> container;
    protected final Type producedType;

    public ValueImpl(@Ignore TypeDescriptor $reifiedGet, @Ignore TypeDescriptor $reifiedSet,
            ValueDeclarationImpl value, TypedReference valueTypedReference, 
            ceylon.language.meta.model.Type<?> container, Object instance) {
        this.producedType = valueTypedReference.getType();
        this.container = container;
        this.type = Metamodel.getAppliedMetamodel(producedType);
        this.$reifiedGet = $reifiedGet;
        this.$reifiedSet = $reifiedSet;
        this.declaration = value;
        this.instance = instance;
        
        initField(instance, producedType);
    }

    private void initField(Object instance, Type valueType) {
        org.eclipse.ceylon.model.typechecker.model.Value decl = (org.eclipse.ceylon.model.typechecker.model.Value) declaration.declaration;
        String name = decl.getName();
        if(decl instanceof JavaBeanValue){
            java.lang.Class<?> javaClass = Metamodel.getJavaClass((org.eclipse.ceylon.model.typechecker.model.ClassOrInterface)decl.getContainer());
            if(javaClass == ceylon.language.Object.class
                    || javaClass == ceylon.language.Basic.class
                    || javaClass == ceylon.language.Identifiable.class){
                if("string".equals(name)
                        || "hash".equals(name)){
                    // look it up on j.l.Object, getterName should work
                    javaClass = java.lang.Object.class;
                }else{
                    throw Metamodel.newModelError("Object/Basic/Identifiable member not supported: "+name);
                }
            } else if (javaClass == ceylon.language.Throwable.class) {
                if("cause".equals(name)
                        || "message".equals(name)
                        || "suppressed".equals(name)){
                    javaClass = instance.getClass();
                    isSuppressed = "suppressed".equals(name);
                }
            }
            String getterName = ((JavaBeanValue) decl).getGetterName();
            try {
                Class<?>[] params;
                if (!declaration.getStatic()) {
                    params = NO_PARAMS; 
                } else {
                    int numCapturedTypeParams = ((ClassOrInterface)declaration.declaration.getContainer()).getTypeParameters().size();
                    params = new Class[numCapturedTypeParams];
                    Arrays.fill(params, TypeDescriptor.class);
                }
                boolean isJavaArray = MethodHandleUtil.isJavaArray(javaClass);
                if(isJavaArray)
                    params = MethodHandleUtil.getJavaArrayGetArrayParameterTypes(javaClass, getterName);
                // if it is shared we may want to get an inherited getter, but if it's private we need the declared method to return it
                Method m = decl.isShared() ? javaClass.getMethod(getterName, params) : javaClass.getDeclaredMethod(getterName, params);
                m.setAccessible(true);
                getter = MethodHandles.lookup().unreflect(m);
                java.lang.Class<?> getterType = m.getReturnType();
                getter = MethodHandleUtil.boxReturnValue(getter, getterType, valueType);
                if(instance != null 
                        // XXXArray.getArray is static but requires an instance as first param
                        && (isJavaArray || !Modifier.isStatic(m.getModifiers()))) {
                    getter = getter.bindTo(instance);
                }
                
                if (declaration.getStatic()) {
                    getter = getter.asType(MethodType.methodType(Object.class, params));
                    TypeDescriptor[] typeArguments = ((TypeDescriptor.Class)((ClassOrInterfaceImpl<?>) container).$reifiedType).getTypeArguments();
                    getter = getter.asSpreader(TypeDescriptor[].class, typeArguments.length);
                    getter = getter.bindTo(typeArguments);
                } else {
                    // we need to cast to Object because this is what comes out when calling it in $call
                    getter = getter.asType(MethodType.methodType(Object.class));
                }

                initSetter(decl, javaClass, getterType, instance, valueType);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException e) {
                throw Metamodel.newModelError("Failed to find getter method "+getterName+" for: "+decl, e);
            }
        }else if(decl instanceof LazyValue){
            LazyValue lazyDecl = (LazyValue) decl;
            java.lang.Class<?> javaClass = ((ReflectionClass)lazyDecl.classMirror).klass;
            // FIXME: we should really save the getter name in the LazyDecl
            String getterName = NamingBase.getGetterName(lazyDecl);
            try {
                // toplevels don't have inheritance
                Method m = javaClass.getDeclaredMethod(getterName);
                m.setAccessible(true);
                getter = MethodHandles.lookup().unreflect(m);
                java.lang.Class<?> getterType = m.getReturnType();
                getter = MethodHandleUtil.boxReturnValue(getter, getterType, valueType);
                // we need to cast to Object because this is what comes out when calling it in $call
                getter = getter.asType(MethodType.methodType(Object.class));

                initSetter(decl, javaClass, getterType, null, valueType);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException e) {
                throw Metamodel.newModelError("Failed to find getter method "+getterName+" for: "+decl, e);
            }
        }else if(decl instanceof FieldValue){
            FieldValue fieldDecl = (FieldValue) decl;
            java.lang.Class<?> javaClass = Metamodel.getJavaClass((org.eclipse.ceylon.model.typechecker.model.ClassOrInterface)decl.getContainer());
            String fieldName = fieldDecl.getRealName();
            if(MethodHandleUtil.isJavaArray(javaClass)){
                try {
                    Method method = Array.class.getDeclaredMethod("getLength", Object.class);
                    getter = MethodHandles.lookup().unreflect(method);
                    java.lang.Class<?> getterType = method.getReturnType();
                    getter = MethodHandleUtil.boxReturnValue(getter, getterType, valueType);
                    // this one is static but requires an instance a first param
                    if(instance != null)
                        getter = getter.bindTo(instance);
                    // we need to cast to Object because this is what comes out when calling it in $call
                    getter = getter.asType(MethodType.methodType(Object.class));
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException e) {
                    throw Metamodel.newModelError("Failed to find Array.getLength method for: "+decl, e);
                }
            }else{
                try {
                    // fields are not inherited
                    Field f = javaClass.getDeclaredField(fieldName);
                    f.setAccessible(true);
                    getter = MethodHandles.lookup().unreflectGetter(f);
                    java.lang.Class<?> getterType = f.getType();
                    getter = MethodHandleUtil.boxReturnValue(getter, getterType, valueType);
                    if(instance != null && !Modifier.isStatic(f.getModifiers()))
                        getter = getter.bindTo(instance);
                    // we need to cast to Object because this is what comes out when calling it in $call
                    getter = getter.asType(MethodType.methodType(Object.class));

                    initSetter(decl, javaClass, getterType, instance, valueType);
                } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
                    throw Metamodel.newModelError("Failed to find field "+fieldName+" for: "+decl, e);
                }
            }
        } else if (ModelUtil.isEnumeratedConstructor(decl)) {
            java.lang.Class<?> javaClass = Metamodel.getJavaClass((org.eclipse.ceylon.model.typechecker.model.ClassOrInterface)decl.getContainer());
            String getterName = NamingBase.getGetterName(decl);
            try {
                Class<?>[] params = NO_PARAMS;
                // if it is shared we may want to get an inherited getter, but if it's private we need the declared method to return it
                Method m = decl.isShared() ? javaClass.getMethod(getterName, params) : javaClass.getDeclaredMethod(getterName, params);
                m.setAccessible(true);
                getter = MethodHandles.lookup().unreflect(m);
                java.lang.Class<?> getterType = m.getReturnType();
                getter = MethodHandleUtil.boxReturnValue(getter, getterType, valueType);
                if(instance != null 
                        // XXXArray.getArray is static but requires an instance as first param
                        && (!Modifier.isStatic(m.getModifiers()))) {
                    getter = getter.bindTo(instance);
                }
                // we need to cast to Object because this is what comes out when calling it in $call
                getter = getter.asType(MethodType.methodType(Object.class));

                initSetter(decl, javaClass, getterType, instance, valueType);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException e) {
                throw Metamodel.newModelError("Failed to find getter method "+getterName+" for: "+decl, e);
            }
        }else
            throw new StorageException("Attribute "+name+" is neither captured nor shared so it has no physical storage allocated and cannot be read by the metamodel");
    }

    private void initSetter(org.eclipse.ceylon.model.typechecker.model.Value decl, java.lang.Class<?> javaClass, 
                            java.lang.Class<?> getterReturnType, Object instance, Type valueType) {
        
        if(!decl.isVariable() && !decl.isLate())
            return;
        if (ModelUtil.isEnumeratedConstructor(decl)) {
            return;
        }
        if(decl instanceof JavaBeanValue){
            String setterName = ((JavaBeanValue) decl).getSetterName();
            if (setterName==null)
                return;
            try {
                Method m = javaClass.getMethod(setterName, getterReturnType);
                m.setAccessible(true);
                setter = MethodHandles.lookup().unreflect(m);
                if(instance != null && !Modifier.isStatic(m.getModifiers()))
                    setter = setter.bindTo(instance);
                setter = setter.asType(MethodType.methodType(void.class, getterReturnType));
                setter = MethodHandleUtil.unboxArguments(setter, 0, 0, new java.lang.Class[]{getterReturnType}, Arrays.asList(valueType));
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException e) {
                throw Metamodel.newModelError("Failed to find setter method "+setterName+" for: "+decl, e);
            }
        }else if(decl instanceof LazyValue){
            // FIXME: we should really save the getter name in the LazyDecl
            String setterName = NamingBase.getSetterName(decl);
            try {
                Method m = javaClass.getMethod(setterName, getterReturnType);
                m.setAccessible(true);
                setter = MethodHandles.lookup().unreflect(m);
                setter = setter.asType(MethodType.methodType(void.class, getterReturnType));
                setter = MethodHandleUtil.unboxArguments(setter, 0, 0, new java.lang.Class[]{getterReturnType}, Arrays.asList(valueType));
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException e) {
                throw Metamodel.newModelError("Failed to find setter method "+setterName+" for: "+decl, e);
            }
        }else if(decl instanceof FieldValue){
            String fieldName = ((FieldValue) decl).getRealName();
            try {
                Field f = javaClass.getField(fieldName);
                f.setAccessible(true);
                setter = MethodHandles.lookup().unreflectSetter(f);
                if(instance != null && !Modifier.isStatic(f.getModifiers()))
                    setter = setter.bindTo(instance);
                setter = setter.asType(MethodType.methodType(void.class, getterReturnType));
                setter = MethodHandleUtil.unboxArguments(setter, 0, 0, new java.lang.Class[]{getterReturnType}, Arrays.asList(valueType));
            } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
                throw Metamodel.newModelError("Failed to find field "+fieldName+" for: "+decl, e);
            }
        }else
            throw Metamodel.newModelError("Unsupported attribute type: "+decl);
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::ValueDeclaration")
    public ceylon.language.meta.declaration.ValueDeclaration getDeclaration() {
        return declaration;
    }

    @Override
    public Get get() {
        if($reifiedGet.equals(null_.$TypeDescriptor$))
            return null;
        try {
            java.lang.Object result = getter.invokeExact();
            if (isSuppressed) {
                Throwable[] array = (Throwable[]) result;
                result = Util.sequentialWrapperCopy(
                        ceylon.language.Throwable.$TypeDescriptor$, 
                        array);
            }
            return (Get) result;
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }
    }

    @Override
    public Object set(Set value) {
        if(!(declaration.getVariable()
                || declaration.getLate())) {
            throw new MutationException("Value is neither variable nor late");
        }
        try {
            setter.invokeExact(value);
            return null;
        } catch (Throwable e) {
            Util.rethrow(e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public java.lang.Object $setIfAssignable(@Name("newValue") @TypeInfo("ceylon.language::Anything") @Nullable java.lang.Object newValue){
        Type newValueType = Metamodel.getProducedType(newValue);
        if(!newValueType.isSubtypeOf(this.producedType))
            throw new IncompatibleTypeException("Invalid new value type: "+newValueType+", expecting: "+this.producedType);
        return set((Set) newValue);
    }

    @Override
    @TypeInfo("ceylon.language.meta.model::Type<Get>")
    public ceylon.language.meta.model.Type<? extends Get> getType() {
        return type;
    }

    @Override
    public int hashCode() {
        int result = 1;
        // in theory, if our instance is the same, our containing type should be the same
        // and if we don't have an instance we're a toplevel and have no containing type
        result = 37 * result + (instance == null ? 0 : instance.hashCode());
        result = 37 * result + getDeclaration().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ValueImpl == false)
            return false;
        ValueImpl<?,?> other = (ValueImpl<?,?>) obj;
        // in theory, if our instance is the same, our containing type should be the same
        // and if we don't have an instance we're a toplevel and have no containing type
        return Util.eq(instance, other.instance)
                && getDeclaration().equals(other.getDeclaration());
    }

    @Override
    public String toString() {
        return Metamodel.toTypeString(this);
    }


    @Override
    @TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>|ceylon.language::Null")
    public ceylon.language.meta.model.Type<?> getContainer(){
        return container;
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(ValueImpl.class, $reifiedGet, $reifiedSet);
    }
}
