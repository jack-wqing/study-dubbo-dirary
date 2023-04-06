package com.zspace.lwq.dubbo.complier.jdk;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * java class对象可能的生成方式：
 *    1、自己手写字节码，然后类加载器进行加载
 *    2、已有的操作字节码的库来操作字节码，创建class,如：CGLib和Javassist两种
 *    3、 1、使用Javac的命令生成class文件，然后加载  java.tools包
 *        2、生成字节码的方式放置到内存中，然后加载生成class
 * 编译内容：
 *    编译器：涉及接口和类如下：
 *           JavaCompiler : 通过ToolProvider来获取
 *           JavaCompiler.CompilationTask : 通过ToolProvider来获取，然后执行编译任务
 *           ToolProvider : 工具箱类，可以获取JavaCompiler 和 JavaCompiler.CompilationTask
 *    源文件管理的类：涉及接口和类如下：
 *           FileObject
 *           ForwardingFileObject
 *           JavaFileObject
 *           JavaFileObject.Kind
 *           ForwardingJavaFileObject
 *           SimpleJavaFileObject
 *           FileObject代表了对文件的一种抽象
 *    文件的创建和管理：涉及接口和类如下：
 *           JavaFileManager
 *           JavaFileManger.Location
 *           StandardJavaFileManager
 *           ForwardingJavaFileManger
 *           StandardLocation
 *           JavaFileManger用来创建JavaFileObject，包括从特定位置输出和输入一个JavaFileObject
 *    编译选项的管理：
 *           OptionChecker
 *    诊断信息的收集:涉及接口和类如下：
 *           Diagnostic
 *           DiagnosticListener
 *           Diagnostic.Kind
 *           DiagnosticCollector
 *
 */
public class CustomStringJavaCompiler {
    //类全名
    private String fullClassName;
    //源码字符串
    private String sourceCode;
    //存放编译之后的字节码(key:类名， value:编译之后输出的字节码)
    private Map<String, ByteJavaFileObject> javaFileObjectMap = new ConcurrentHashMap<>();
    //获取Java的编译器
    private JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    //存放编译过程中输出的信息
    private DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
    //存放执行结果
    private String runResult;
    //编译耗时
    private long compilerTakeTime;
    //运行耗时
    private long runTakeTime;

    public CustomStringJavaCompiler(String sourceCode) {
        this.sourceCode = sourceCode;
        this.fullClassName = getFullClassName(sourceCode);
    }

    /**
     * 编译字符串源代码，编译失败在DiagnosticCollector中获取提示信息
     * return : true编译成功   false:编译失败
     */
    public boolean compiler(){
        long startTime = System.currentTimeMillis();
        //标准的JavaFileObject对象管理器，更换为自己的实现，覆盖部分方法
        StandardJavaFileManager standardJavaFileManager = compiler.getStandardFileManager(diagnosticCollector, null, null);
        JavaFileManager javaFileManager = new StringJavaFileManger(standardJavaFileManager);
        //构建源代码对象
        JavaFileObject javaFileObject = new StringJavaFileObject(fullClassName, sourceCode);
        //获取一个编译任务
        JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, javaFileManager, diagnosticCollector, null, null, Arrays.asList(javaFileObject));
        //编译时间
        compilerTakeTime = System.currentTimeMillis() - startTime;
        return compilerTask.call();
    }

    /**
     * 执行main方法， 重定向System.out.print
     */
    public void runMainMethod(){
        PrintStream out = System.out;
        try{
            long startTime = System.currentTimeMillis();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            System.setOut(printStream);
            StringClassLoader scl = new StringClassLoader();
            Class<?> aClass = scl.findClass(fullClassName);
            Method main = aClass.getMethod("main", String[].class);
            Object[] pars = new Object[1];
            pars[0] = new String[]{};
            main.invoke(null, pars);   //调用main方法
            //设置运行耗时
            runTakeTime = System.currentTimeMillis() - startTime;
            //设置打印结果
            runResult = new String(outputStream.toByteArray(), "utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.setOut(out);
        }
    }

    public String getRunResult() {
        return runResult;
    }

    public long getCompilerTakeTime() {
        return compilerTakeTime;
    }

    public long getRunTakeTime() {
        return runTakeTime;
    }

    /**
     * 获取编译信息(错误，警告)
     * @return
     */
    public String getCompilerMessage(){
        StringBuilder sb = new StringBuilder();
        List<Diagnostic<? extends  JavaFileObject>> diagnostics = diagnosticCollector.getDiagnostics();
        for (Diagnostic diagnostic: diagnostics) {
            sb.append(diagnostic.toString() + "\r\n");
        }
        return sb.toString();
    }

    /**
     * 通过package + className 来生成全类名
     */
    public static String getFullClassName(String sourceCode){
        String className = "";
        Pattern pattern = Pattern.compile("package\\s+\\S+\\s*;");
        Matcher matcher = pattern.matcher(sourceCode);
        if(matcher.find()){
            className = matcher.group().replaceFirst("package", "").replace(";", "").trim() + ".";
        }
        pattern = Pattern.compile("class\\s+\\S+\\s+\\{");
        matcher = pattern.matcher(sourceCode);
        if(matcher.find()){
            className += matcher.group().replaceFirst("class", "").replace("{", "").trim();
        }
        return className;
    }


    /**
     * 自定义一个字符串源码对象
     */
    private class StringJavaFileObject extends SimpleJavaFileObject {
        //等待编译的源码内容
        private String content;
        //Java 源代码 => StringJavaFileManager对象的时候使用
        protected StringJavaFileObject(String className, String content) {
            super(URI.create("string:////" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
            this.content = content;
        }
        //字符串源码会调用该方法
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return content;
        }
    }


    /**
     * 重新定义JavaFileObject
     */
    private class ByteJavaFileObject extends SimpleJavaFileObject{
        //存放编译后得字节码对象
        private ByteArrayOutputStream outputStream;
        /**
         * @param className  字符串代码的类名称
         * @param kind the kind of this file object
         */
        protected ByteJavaFileObject(String className, Kind kind) {
            super(URI.create("string:////" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension), kind);
        }

        //StringJavaFileManage编译之后的字节码输出会调用该方法(把字节码输出到OutputStream)
        @Override
        public OutputStream openOutputStream() throws IOException {
            outputStream = new ByteArrayOutputStream();
            return outputStream;
        }
        //在类加载器加载的时候会用到
        public byte[] getCompilerBytes(){
            return outputStream.toByteArray();
        }
    }

    /**
     * 自定义一个JavaFileManger来控制编译之后字节码的输出位置
     */
    private class StringJavaFileManger extends ForwardingJavaFileManager{
        /**
         * Creates a new instance of ForwardingJavaFileManager.
         *
         * @param fileManager delegate to this file manager
         */
        protected StringJavaFileManger(JavaFileManager fileManager) {
            super(fileManager);
        }

        /**
         * 获取输出的文件对象，它表示给定位置处指定类型的指定类
         */
        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            ByteJavaFileObject javaFileObject = new ByteJavaFileObject(className, kind);
            javaFileObjectMap.put(className, javaFileObject);
            return javaFileObject;
        }
    }

    /**
     * 自定义类加载器
     */
    private class StringClassLoader extends ClassLoader{
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            ByteJavaFileObject javaFileObject = javaFileObjectMap.get(name);
            if(javaFileObject != null){
                byte[] bytes = javaFileObject.getCompilerBytes();
                return defineClass(name, bytes, 0, bytes.length);
            }
            try{
                return ClassLoader.getSystemClassLoader().loadClass(name);
            }catch (Exception e){
                return super.findClass(name);
            }
        }
    }

}