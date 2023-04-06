package com.zspace.lwq.dubbo.proxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class Hacker implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("proxy start ...");
        proxy.invokeSuper(obj, args);
        System.out.println("proxy end ...");

        return null;
    }
}