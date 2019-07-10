package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DateAdapter extends AbstractAdapter<Date> {

    public DateAdapter() {
        super(Date.class);
    }

    @Override
    public Date getValue(ResultSet result, String label) throws SQLException {
        return result.getDate(label);
    }

    @Override
    public void setValue(PreparedStatement statement, int index, Date value) throws SQLException {
        statement.setDate(index, value);
    }

}
