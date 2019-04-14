package org.valdi.SuperApiX.common.config.advanced;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author david
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigEntry {

    String path();
    String since() default "1.0";
    boolean overrideOnChange() default false;
    boolean experimental() default false;
    boolean needsReset() default false;

    /**
     * Sets whether this config entry should be printed in the final config file or not.
     * @return {@code true} if this config entry should be printed in the final config file, {@code false} otherwise.
     */
    boolean hidden() default false;

}