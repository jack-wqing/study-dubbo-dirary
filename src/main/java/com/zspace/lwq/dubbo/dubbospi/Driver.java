package com.zspace.lwq.dubbo.dubbospi;

import org.apache.dubbo.common.extension.SPI;

@SPI
public interface Driver {

    String connect();

}