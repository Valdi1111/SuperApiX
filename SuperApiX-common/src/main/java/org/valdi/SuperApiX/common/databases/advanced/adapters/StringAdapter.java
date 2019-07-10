package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class StringAdapter extends AbstractAdapter<String> {

    public StringAdapter() {
        super(String.class);
    }

    @Override
    public String getValue(ResultSet result, String label) throws SQLException {
        return result.getString(label);
    }

    @Override
    public void setValue(PreparedStatement statement, int index, String value) throws SQLException {
        statement.setString(index, value);
    }
}
