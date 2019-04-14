package org.valdi.SuperApiX.common.config.advanced.adapters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Denotes which adapter should be used to serialize or deserialize this field
 * @author david
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Adapter {

    Class<? extends AdapterInterface> value();

}