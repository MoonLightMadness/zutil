package app.net.annotation;

import java.lang.annotation.*;

/**
 * @author zhl
 */
@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface NotNull {
}
