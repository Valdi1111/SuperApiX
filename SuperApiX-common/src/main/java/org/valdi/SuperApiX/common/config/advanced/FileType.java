package org.valdi.SuperApiX.common.config.advanced;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.valdi.SuperApiX.common.config.types.ConfigType;

/**
 * Defines where this data object will be stored.
 * @author david
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FileType {
    
    ConfigType value() default ConfigType.YAML;

}
