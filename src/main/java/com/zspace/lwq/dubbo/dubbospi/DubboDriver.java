package com.zspace.lwq.dubbo.dubbospi;

public class DubboDriver implements Driver {
    @Override
    public String connect() {
        return "dubbo spi";
    }
}