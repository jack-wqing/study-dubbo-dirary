package com.zspace.lwq.dubbo.proxy.javassist;

import javassist.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class JavassistUpdateProgram {

    public void updateCoding() throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("com.zspace.javaknowledge.proxy.javassist.JavassistProgram");
        CtMethod updateCoding = cc.getDeclaredMethod("coding");
        updateCoding.insertBefore("System.out.println(\"start...\");");
        updateCoding.insertAfter("System.out.println(\"end...\");");
        final Object proxyClass = cc.toClass().newInstance();
        Method method = proxyClass.getClass().getMethod("coding");
        method.invoke(proxyClass);
    }

    public void newMethod() throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("com.zspace.javaknowledge.proxy.javassist.JavassistProgram");
        CtMethod ctMethod = new CtMethod(CtClass.voidType, "newMethod", new CtClass[]{}, ctClass);
        ctMethod.setModifiers(Modifier.PUBLIC);
        ctMethod.setBody("{System.out.println(\"i am new method\");}");
        ctClass.addMethod(ctMethod);
        final Object proxyClass = ctClass.toClass().newInstance();
        Method newMethod = proxyClass.getClass().getMethod("newMethod");
        newMethod.invoke(proxyClass, null);
    }

}