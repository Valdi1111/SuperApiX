package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LongAdapter extends AbstractAdapter<Long> {

    public LongAdapter() {
        super(long.class);
    }

    @Override
    public Long getValue(ResultSet result, String label) throws SQLException {
        return result.getLong(label);
    }

    @Override
    public void setValue(PreparedStatement statement, int index, Long value) throws SQLException {
        statement.setLong(index, value);
    }

}
