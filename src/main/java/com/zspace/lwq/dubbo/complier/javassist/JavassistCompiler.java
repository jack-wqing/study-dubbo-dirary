package com.zspace.lwq.dubbo.complier.javassist;

import javassist.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 主要的类：
 *   ClassPool: javassist得类池，使用ClassPool类可以跟踪和控制所有操作的类，工作方式与JVM类装载器非常相似
 *   CtClass: CtClass提供了检查类数据(如字段和方法)以及在类中添加新字段，方法和构造函数，以及改变类，父类和接口的方法，Javasssist并为提供删除类中字段，方法或这构造函数的任何方法
 *   CtField: 用来访问域
 *   CtMethod:用来访问接口
 *   CtConstructor: 用来访问构造器
 */
public class JavassistCompiler {

    public static void main(String[] args) throws CannotCompileException, NotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //获取到class定义的容器ClassPool
        ClassPool pool = ClassPool.getDefault();
        //创建类
        CtClass ss = pool.makeClass("cn.liu.javassist.zzClass");
        //创建属性
        CtField f1 = CtField.make("private int empno;", ss);
        CtField f2 = CtField.make("private String ename;", ss);
        ss.addField(f1);
        ss.addField(f2);
        //创建方法
        CtMethod m1 = CtMethod.make("public int getEmpno(){return this.empno;}", ss);
        CtMethod m2 = CtMethod.make("public void setEmpno(int empno){this.empno = empno;}", ss);
        ss.addMethod(m1);
        ss.addMethod(m2);
        //添加构造器方法
        CtConstructor constructor = new CtConstructor(new CtClass[]{CtClass.intType, pool.get("java.lang.String")}, ss);
        constructor.setBody("{$0.empno = $1; $0.ename = $2;}");
        ss.addConstructor(constructor);
        //后去Class对象
        Class<?> aClass = ss.toClass();
        //构建执行对象
        Constructor<?> constructor1 = aClass.getConstructor(int.class, String.class);
        Object instance = constructor1.newInstance(1, "王五");
        //Method setEmpno = aClass.getMethod("setEmpno", int.class);
        //setEmpno.invoke(instance, 2);
        Method getEmpno = aClass.getMethod("getEmpno", null);
        Object invoke = getEmpno.invoke(instance, null);
        System.out.println(invoke);

    }




}