package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShortAdapter extends AbstractAdapter<Short> {

    public ShortAdapter() {
        super(short.class);
    }

    @Override
    public Short getValue(ResultSet result, String label) throws SQLException {
        return result.getShort(label);
    }

    @Override
    public void setValue(PreparedStatement statement, int index, Short value) throws SQLException {
        statement.setShort(index, value);
    }

}
