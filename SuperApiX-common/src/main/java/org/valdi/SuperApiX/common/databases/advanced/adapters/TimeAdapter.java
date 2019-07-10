package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

public class TimeAdapter extends AbstractAdapter<Time> {

    public TimeAdapter() {
        super(Time.class);
    }

    @Override
    public Time getValue(ResultSet result, String label) throws SQLException {
        return result.getTime(label);
    }

    @Override
    public void setValue(PreparedStatement statement, int index, Time value) throws SQLException {
        statement.setTime(index, value);
    }

}
