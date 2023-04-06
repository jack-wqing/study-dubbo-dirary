package com.zspace.lwq.dubbo.javaspi;

import java.sql.Driver;
import java.sql.SQLException;
import java.util.ServiceLoader;

/**
 * java SPI 机制
 *
 *   会在resource/META-INF/services 下寻找满足协议的实现类
 *
 */

public class TestJavaSpi {

    public static void main(String[] args) {

        final ServiceLoader<Driver> load = ServiceLoader.load(Driver.class);
        load.forEach(driver -> {
            try {
                System.out.println(driver.connect(null, null));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

}