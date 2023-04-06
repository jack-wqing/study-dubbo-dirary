package com.zspace.lwq.dubbo.proxy.cglib;


import com.zspace.lwq.dubbo.proxy.Program;

public class CglibProgram implements Program {
    @Override
    public void coding() {
        System.out.println("i am coding");
    }
}