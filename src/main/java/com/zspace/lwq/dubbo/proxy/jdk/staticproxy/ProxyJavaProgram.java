package com.zspace.lwq.dubbo.proxy.jdk.staticproxy;


import com.zspace.lwq.dubbo.proxy.Program;

public class ProxyJavaProgram implements Program {
    @Override
    public void coding() {
        JavaProgram javaProgram = new JavaProgram();
        System.out.println("proxy start ...");
        javaProgram.coding();
        System.out.println("proxy end ...");
    }
}