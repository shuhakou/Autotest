package cn.ce.api.service.Listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class AnnotationTransformerImpl implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass,Constructor testConstructor, Method testMethod){
        //失败用例自动retry
        annotation.setRetryAnalyzer(RetryAnalyzerImpl.class);
    }
}
