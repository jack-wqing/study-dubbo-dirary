package com.zspace.lwq.dubbo.dubbospi;

import org.apache.dubbo.common.extension.ExtensionLoader;

/**
 * Dubbo SPI机制
 *
 */

public class DubboDriverTest {

    public static void main(String[] args) {

        final ExtensionLoader<Driver> extensionLoader = ExtensionLoader.getExtensionLoader(Driver.class);

        final Driver dubboDriver = extensionLoader.getExtension("dubboDriver");

        System.out.println(dubboDriver.connect());

    }

}