package org.valdi.SuperApiX.common.config.advanced;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines where this data object will be stored.
 * @author david
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StoreTo {

    /**
     * Filename
     */
    String filename();

    /**
     * Path where this will be stored. If blank, it will be the default jar folder.
     */
    String path() default "";

}
