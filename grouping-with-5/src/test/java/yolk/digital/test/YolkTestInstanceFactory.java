package yolk.digital.test;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;

import java.lang.reflect.*;
import java.util.*;

public class YolkTestInstanceFactory implements TestInstanceFactory {

    @Override
    public Object createTestInstance(TestInstanceFactoryContext testInstanceFactoryContext, ExtensionContext extensionContext) throws TestInstantiationException {
        try {
            Class<?> clazz = findActualClass(extensionContext.getTestClass().get());
            Class[] args = { Void.class };
            return clazz.getDeclaredConstructor(args).newInstance();
        } catch (ReflectiveOperationException e) {
            throw new TestInstantiationException("Couldn't create instance of " + extensionContext.getTestClass().get());
        }
    }

    private static Class<?> findActualClass(Class<?> outerClass) {
        for (Method method: outerClass.getMethods()) {
            if (method.isAnnotationPresent(Test.class) && !method.getName().equals("testFoo")) {
                return outerClass;
            }
        }
        Class<? extends Extension>[] outerExtensions = outerClass.getAnnotation(ExtendWith.class).value();

        Class<?> innerClassToRun = null;
        for (Class<?> innerClass : outerClass.getClasses()) {
            ExtendWith extendWithAnnotation = innerClass.getAnnotation(ExtendWith.class);
            if (extendWithAnnotation == null) {
                continue;
            }
            // TODO: valid comparison with ordering (TreeSets)
//            if (Arrays.compare(extendWithAnnotation.value(), outerExtensions) != 0) {
//                throw new UnsupportedOperationException(String.format("Unsupported @ExtendWith(%s.class) on %s", Arrays.asList(extendWithAnnotation.value()), outerClass.getName()));
//            }
            if (innerClassToRun != null) {
                throw new UnsupportedOperationException(String.format("%s has more than one inner class with @RunWith(%s.class)", outerClass.getName(), Arrays.asList(outerExtensions)));
            }
            innerClassToRun = innerClass;
        }
        if (innerClassToRun == null) {
            return outerClass;
        }

        // will need to construct an outer class with all the inner class methods
        if (InnerTestBase.class.isAssignableFrom(innerClassToRun)) {
            // abstract inner class is augmented to become concrete
            DynamicType.Unloaded unloadedType = new ByteBuddy()
                    .subclass(innerClassToRun)
                    .method(ElementMatchers.hasMethodName("foo"))
                    .intercept(FixedValue.value("Hello ByteBuddy!"))
                    .make();
            Class<?> completeInnerClass = unloadedType.load(YolkTestInstanceFactory.class.getClassLoader()).getLoaded();
            return completeInnerClass;
        } else {
            // not abstract, just let it through
            return innerClassToRun;
        }
    }
}
