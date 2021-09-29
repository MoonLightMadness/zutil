package app.net.annotation;

import java.lang.annotation.*;

/**
 * @author zhl
 */
@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Valid {
}