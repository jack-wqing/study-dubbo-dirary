package com.zspace.lwq.dubbo.proxy.jdk.staticproxy;


import com.zspace.lwq.dubbo.proxy.Program;

public class JavaProgram implements Program {

    @Override
    public void coding() {
        System.out.println("java");
    }

}