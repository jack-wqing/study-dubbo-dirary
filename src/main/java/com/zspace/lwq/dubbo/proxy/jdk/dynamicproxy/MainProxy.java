package com.zspace.lwq.dubbo.proxy.jdk.dynamicproxy;


import com.zspace.lwq.dubbo.proxy.Program;

import java.lang.reflect.Proxy;

/**
 * Java动态代理的实现机制
 */
public class MainProxy {

    public static void main(String[] args) {
        JavaProgram javaProgram = new JavaProgram();
        JdkProxy jdkProxy = new JdkProxy(javaProgram);
        final Program program = (Program)Proxy.newProxyInstance(Program.class.getClassLoader(), new Class[]{Program.class}, jdkProxy);
        program.coding();
    }

}