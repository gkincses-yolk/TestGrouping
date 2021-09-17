package yolk.digital.test.group;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DisabledIfSystemProperty {

    String property() default "";
}
