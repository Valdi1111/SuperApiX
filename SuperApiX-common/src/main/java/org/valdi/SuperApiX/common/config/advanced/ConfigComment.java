package org.valdi.SuperApiX.common.config.advanced;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ConfigComments.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
public @interface ConfigComment {

    String value();

}
