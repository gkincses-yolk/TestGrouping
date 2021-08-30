package yolk.digital.test.group;

import org.junit.runners.model.FrameworkMethod;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class TestGroupInclusionValidator {
    private String propertyValue = System.getProperty("digital.yolk.test.group");
    private List<String> currentGroups = propertyValue == null ? Collections.EMPTY_LIST :
            Collections.list(new StringTokenizer(propertyValue, ",")).stream().map(token -> (String) token).collect(Collectors.toList());

    public boolean isValid(Class<?> clazz) {
        return isValid(clazz.getAnnotations());
    }

    public boolean isValid(FrameworkMethod testMethod) {
        return isValid(testMethod.getAnnotations());
    }

    private boolean isValid(Annotation[] annotations) {
        return Arrays.stream(annotations).anyMatch(annotation ->
                annotation.annotationType().equals(TestGroup.class) &&
                        Arrays.stream(((TestGroup) annotation).value()).anyMatch(s -> currentGroups.isEmpty() || currentGroups.contains(s))
        );
    }
}
