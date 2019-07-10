package org.valdi.SuperApiX.common.databases.advanced.query;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Queries.class)
@Target(ElementType.TYPE)
public @interface Query {

    String value();

    QueryType type();

}
