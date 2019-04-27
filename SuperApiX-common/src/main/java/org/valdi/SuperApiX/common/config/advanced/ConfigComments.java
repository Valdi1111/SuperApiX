package org.valdi.SuperApiX.common.config.advanced;

import org.valdi.SuperApiX.common.config.advanced.ConfigComment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ FIELD, METHOD, TYPE })
public @interface ConfigComments {

    ConfigComment[] value();

}
