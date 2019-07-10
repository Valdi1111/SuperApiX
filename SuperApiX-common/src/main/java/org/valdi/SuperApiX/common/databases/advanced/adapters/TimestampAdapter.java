package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TimestampAdapter extends AbstractAdapter<Timestamp> {

    public TimestampAdapter() {
        super(Timestamp.class);
    }

    @Override
    public Timestamp getValue(ResultSet result, String label) throws SQLException {
        return result.getTimestamp(label);
    }

    @Override
    public void setValue(PreparedStatement statement, int index, Timestamp value) throws SQLException {
        statement.setTimestamp(index, value);
    }

}
