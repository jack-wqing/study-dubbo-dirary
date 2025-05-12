package com.zspace.lwq.dubbo.source.metadata;


import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

/**
 * Metadata APT
 * 1、需要打包独立引入注解处理器
 */
public class MyAnnotationProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> type = new HashSet<>();
        type.add("com.zspace.lwq.dubbo.source.metadata.MyAnnotation");
        return type;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : elements) {
                if (element.getKind() == ElementKind.CLASS) {
                    TypeElement classElement = (TypeElement) element;
                    MyAnnotation classInfo = classElement.getAnnotation(MyAnnotation.class);
                    try {
                        String fileName = classElement.getSimpleName() + ".txt";
                        Writer writer = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", fileName).openWriter();
                        writer.write("Class Name: " + classElement.getQualifiedName() + "\n");
                        writer.write("Author: " + classInfo.author() + "\n");
                        writer.write("Date: " + classInfo.date() + "\n");
                        writer.write("Description: " + classInfo.description() + "\n");
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }
}
