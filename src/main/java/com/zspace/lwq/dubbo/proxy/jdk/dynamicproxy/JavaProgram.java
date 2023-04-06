package com.zspace.lwq.dubbo.proxy.jdk.dynamicproxy;


import com.zspace.lwq.dubbo.proxy.Program;

public class JavaProgram implements Program {

    @Override
    public void coding() {
        System.out.println("java");
    }

}