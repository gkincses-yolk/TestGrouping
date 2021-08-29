package yolk.digital.test.group;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class TestGroupInclusionValidator implements ConstraintValidator<TestGroup, Class<?>> {
    private String currentGroup = System.getProperty("digital.yolk.test.group");

    @Override
    public boolean isValid(Class<?> testClass, ConstraintValidatorContext context) {
        return Arrays.stream(testClass.getAnnotations()).anyMatch(annotation ->
                annotation.annotationType().equals(TestGroup.class) &&
                        Arrays.stream(((TestGroup) annotation).value()).anyMatch(s -> s.equals(currentGroup))
        );
    }
}
