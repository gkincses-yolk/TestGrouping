package yolk.digital.runner;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;
import yolk.digital.test.group.TestGroupInclusionValidator;

@Slf4j
public class YolkRunner extends BlockJUnit4ClassRunner {
    private TestGroupInclusionValidator v = new TestGroupInclusionValidator();

    public YolkRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }
    public YolkRunner(TestClass testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected boolean isIgnored(FrameworkMethod child) {
        return child.getAnnotation(Ignore.class) != null
                || !v.isValid(child.getDeclaringClass()) && !v.isValid(child);
    }

}
