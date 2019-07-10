package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanAdapter extends AbstractAdapter<Boolean> {

    public BooleanAdapter() {
        super(boolean.class);
    }

    @Override
    public Boolean getValue(ResultSet result, String label) throws SQLException {
        return result.getBoolean(label);
    }

    @Override
    public void setValue(PreparedStatement statement, int index, Boolean value) throws SQLException {
        statement.setBoolean(index, value);
    }

}
