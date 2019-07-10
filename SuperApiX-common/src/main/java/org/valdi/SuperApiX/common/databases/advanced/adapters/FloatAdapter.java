package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FloatAdapter extends AbstractAdapter<Float> {

    public FloatAdapter() {
        super(float.class);
    }

    @Override
    public Float getValue(ResultSet result, String label) throws SQLException {
        return result.getFloat(label);
    }

    @Override
    public void setValue(PreparedStatement statement, int index, Float value) throws SQLException {
        statement.setFloat(index, value);
    }

}
