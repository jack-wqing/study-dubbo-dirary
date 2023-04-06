package com.zspace.lwq.dubbo.proxy.cglib;

import com.zspace.lwq.dubbo.proxy.Program;
import net.sf.cglib.proxy.Enhancer;

public class CglibMain {

    public static void main(String[] args) {

        CglibProgram cglibProgram = new CglibProgram();
        Hacker hacker = new Hacker();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cglibProgram.getClass());
        enhancer.setCallback(hacker);
        Program programProxy = (Program) enhancer.create();
        programProxy.coding();

    }

}