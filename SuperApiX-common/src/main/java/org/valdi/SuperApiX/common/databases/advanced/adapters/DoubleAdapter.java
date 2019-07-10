package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoubleAdapter extends AbstractAdapter<Double> {

    public DoubleAdapter() {
        super(double.class);
    }

    @Override
    public Double getValue(ResultSet result, String label) throws SQLException {
        return result.getDouble(label);
    }

    @Override
    public void setValue(PreparedStatement statement, int index, Double value) throws SQLException {
        statement.setDouble(index, value);
    }

}
