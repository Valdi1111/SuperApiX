package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface ColumnAdapter<Type> {

    /**
     * Read the value from a given result set and a give column name.
     * @param result the result set
     * @param label the column name
     * @return the value
     * @throws SQLException any exception that can be thrown by the ResultSet
     */
    Type getValue(ResultSet result, String label) throws SQLException;

    /**
     * Write a value in the given statement at the given index.
     * @param statement the statement
     * @param index the index
     * @param value the value
     * @throws SQLException any exception that can be thrown by the PreparedStatement
     */
    void setValue(PreparedStatement statement, int index, Type value) throws SQLException;

}
