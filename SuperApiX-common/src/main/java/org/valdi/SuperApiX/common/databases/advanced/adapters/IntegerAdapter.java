package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerAdapter extends AbstractAdapter<Integer> {

    public IntegerAdapter() {
        super(int.class);
    }

    @Override
    public Integer getValue(ResultSet result, String label) throws SQLException {
        return result.getInt(label);
    }

    @Override
    public void setValue(PreparedStatement statement, int index, Integer value) throws SQLException {
        statement.setInt(index, value);
    }

}
