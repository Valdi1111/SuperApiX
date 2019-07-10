package org.valdi.SuperApiX.common.databases.advanced.column;

import org.valdi.SuperApiX.common.databases.advanced.adapters.ColumnAdapter;
import org.valdi.SuperApiX.common.databases.advanced.query.QueryType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ColumnLabels.class)
@Target(ElementType.FIELD)
public @interface ColumnLabel {

    /**
     * The table's column name.
     * @return the name of the table column
     */
    String value();

    /**
     * The type of the query.
     * @return query type
     */
    QueryType type();

    /**
     * The adapter assigned for the serialization of the result set.
     * @return the adapter
     */
    Class<? extends ColumnAdapter> adapter();

}
