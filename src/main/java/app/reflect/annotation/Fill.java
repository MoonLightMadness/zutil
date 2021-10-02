package app.reflect.annotation;

import java.lang.annotation.*;

/**
 * @author zhl
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Fill {
    String value() default "";
}