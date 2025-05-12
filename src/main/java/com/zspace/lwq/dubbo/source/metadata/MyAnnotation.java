package com.zspace.lwq.dubbo.source.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.TYPE)
public @interface MyAnnotation {

    String author() default "";

    String date();

    String description() default "";

}
