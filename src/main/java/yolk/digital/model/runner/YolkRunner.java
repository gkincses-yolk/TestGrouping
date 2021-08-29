package yolk.digital.model.runner;

import lombok.extern.slf4j.Slf4j;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;
import yolk.digital.test.group.TestGroupInclusionValidator;

@Slf4j
public class YolkRunner extends BlockJUnit4ClassRunner {
    private static TestGroupInclusionValidator v = new TestGroupInclusionValidator();

    public YolkRunner(Class<?> testClass) throws InitializationError {
        super(checkClass(testClass));
    }
    public YolkRunner(TestClass testClass) throws InitializationError {
        super(checkTestClass(testClass));
    }

    private static Class<?> checkClass(Class<?> testClass) {
        log.debug("Validating " + testClass);

        if (!v.isValid(testClass, null)) {
            throw new IllegalArgumentException("Skipping " + testClass);
        }
        log.info("Including " + testClass);
        return testClass;
    }

    private static TestClass checkTestClass(TestClass testClass) {
        throw new IllegalStateException("Validating " + testClass + " is not supported");
    }
}
