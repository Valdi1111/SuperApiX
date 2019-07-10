package org.valdi.SuperApiX.common.databases.advanced.statement;

import org.valdi.SuperApiX.common.databases.advanced.adapters.ColumnAdapter;
import org.valdi.SuperApiX.common.databases.advanced.query.QueryType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(StatementIndexes.class)
@Target(ElementType.FIELD)
public @interface StatementIndex {

    /**
     * The index to write the value at.
     * @return the index
     */
    int value();

    /**
     * The type of the query.
     * @return query type
     */
    QueryType type();

    /**
     * The adapter assigned for the serialization of the statement.
     * @return the adapter
     */
    Class<? extends ColumnAdapter> adapter();

}
