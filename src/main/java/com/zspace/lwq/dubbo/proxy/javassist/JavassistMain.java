package com.zspace.lwq.dubbo.proxy.javassist;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.lang.reflect.InvocationTargetException;

public class JavassistMain {

    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException, CannotCompileException, NotFoundException, NoSuchMethodException {

        JavassistUpdateProgram javassistUpdateProgram = new JavassistUpdateProgram();
        //javassistUpdateProgram.updateCoding();
        javassistUpdateProgram.newMethod();

    }

}