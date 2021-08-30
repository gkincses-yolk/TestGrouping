package yolk.digital.test.runner;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import yolk.digital.test.InnerTestBase;
import yolk.digital.test.group.TestGroupInclusionValidator;

import java.lang.reflect.Method;

@Slf4j
public class YolkRunner extends BlockJUnit4ClassRunner {
    private TestGroupInclusionValidator v = new TestGroupInclusionValidator();

    public YolkRunner(Class<?> testClass) throws InitializationError {
        super(findActualClass(testClass));
    }

    @Override
    protected boolean isIgnored(FrameworkMethod child) {
        return !v.isValid(child.getDeclaringClass()) && !v.isValid(child);
    }

    private static Class<?> findActualClass(Class<?> outerClass) {
        for (Method method: outerClass.getMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                return outerClass;
            }
        }
        Class<? extends Runner> outerRunner = outerClass.getAnnotation(RunWith.class).value();

        Class<?> innerClassToRun = null;
        for (Class<?> innerClass : outerClass.getClasses()) {
            RunWith runWithAnnotation = innerClass.getAnnotation(RunWith.class);
            if (runWithAnnotation == null) {
                continue;
            }
            if (runWithAnnotation.value() != outerRunner) {
                throw new UnsupportedOperationException(String.format("Unsupported @RunWith(%s.class) on %s", runWithAnnotation.value().getSimpleName(), outerClass.getName()));
            }
            if (innerClassToRun != null) {
                throw new UnsupportedOperationException(String.format("%s has more than one inner class with @RunWith(%s.class)", outerClass.getName(), outerRunner.getSimpleName()));
            }
            innerClassToRun = innerClass;
        }
        if (innerClassToRun == null) {
            return outerClass;
        }

        if (InnerTestBase.class.isAssignableFrom(innerClassToRun)) {
            // abstract inner class is augmented to become concrete
            DynamicType.Unloaded unloadedType = new ByteBuddy()
                    .subclass(innerClassToRun)
                    .method(ElementMatchers.hasMethodName("foo"))
                    .intercept(FixedValue.value("Hello ByteBuddy!"))
                    .make();
            Class<?> completeInnerClass = unloadedType.load(YolkRunner.class.getClassLoader()).getLoaded();
            return completeInnerClass;
        } else {
            // not abstract, just let it through
            return innerClassToRun;
        }
    }
}
