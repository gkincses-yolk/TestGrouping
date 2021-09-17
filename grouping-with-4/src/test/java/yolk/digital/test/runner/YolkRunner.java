package yolk.digital.test.runner;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import yolk.digital.test.InnerTestBase;
import yolk.digital.test.group.*;

import java.lang.reflect.Method;

@Slf4j
public class YolkRunner extends BlockJUnit4ClassRunner {

    public YolkRunner(Class<?> testClass) throws InitializationError {
        super(findActualClass(testClass));
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
                    .annotateType(innerClassToRun.getAnnotations())
                    .make();
            Class<?> completeInnerClass = unloadedType.load(YolkRunner.class.getClassLoader()).getLoaded();
            return completeInnerClass;
        } else {
            // not abstract, just let it through
            return innerClassToRun;
        }
    }

    @Override
    protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
        Class<?> clazz = this.getTestClass().getJavaClass();
        Boolean isTestClassDisabled  = checkDisabled(clazz.getAnnotation(DisabledIfSystemProperty.class));
        Boolean isTestClassEnabled   = checkEnabled(clazz.getAnnotation(EnabledIfSystemProperty.class));
        Boolean isTestMethodDisabled = checkDisabled(method.getMethod().getAnnotation(DisabledIfSystemProperty.class));
        Boolean isTestMethodEnabled  = checkEnabled(method.getMethod().getAnnotation(EnabledIfSystemProperty.class));

        Boolean isDisabled;
        if (isTestClassDisabled == null) {
            isDisabled = isTestMethodDisabled;
        } else {
            if (isTestMethodDisabled == null) {
                isDisabled = isTestClassDisabled.booleanValue();
            } else {
                isDisabled = isTestMethodDisabled.booleanValue() || isTestClassDisabled.booleanValue();
            }
        }
        Boolean isEnabled;
        if (isTestClassEnabled == null) {
            isEnabled = isTestMethodEnabled;
        } else {
            if (isTestMethodEnabled == null) {
                isEnabled = isTestClassEnabled.booleanValue();
            } else {
                isEnabled = isTestMethodEnabled.booleanValue() || isTestClassEnabled.booleanValue();
            }
        }
        if (Boolean.TRUE.equals(isEnabled)) {
            super.runChild(method, notifier);
        }
        if (Boolean.FALSE.equals(isEnabled) || Boolean.TRUE.equals(isDisabled)) {
            skipTest(method, notifier);
            return;
        }
        super.runChild(method, notifier);
    }

    private void skipTest(final FrameworkMethod method, RunNotifier notifier) {
        notifier.fireTestIgnored(describeChild(method));
    }

    private Boolean checkDisabled(DisabledIfSystemProperty annotation) {
        if (annotation == null) {
            return null;
        }
        String propertyValue = System.getProperty(annotation.property());
        // LOCAL defaults to ON
        if (annotation.property().equalsIgnoreCase("yolk.digital.junit.local")) {
            if (propertyValue == null) {
                return Boolean.FALSE;
            }
            if (propertyValue.equalsIgnoreCase("false")) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } else { // others default to OFF
            if (propertyValue == null) {
                return Boolean.TRUE;
            }
            if (!propertyValue.equalsIgnoreCase("true")) {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
    }

    private Boolean checkEnabled(EnabledIfSystemProperty annotation) {
        if (annotation == null) {
            return null;
        }
        String propertyValue = System.getProperty(annotation.property());
        // others default to OFF
        if (!annotation.property().equalsIgnoreCase("yolk.digital.junit.local")) {
            if (propertyValue == null) {
                return Boolean.FALSE;
            }
            if (propertyValue.equalsIgnoreCase("true")) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } else { // LOCAL defaults to ON
            if (propertyValue == null) {
                return Boolean.TRUE;
            }
            if (propertyValue.equalsIgnoreCase("false")) {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
    }

}
