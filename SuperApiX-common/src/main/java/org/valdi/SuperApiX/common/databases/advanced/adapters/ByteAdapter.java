package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteAdapter extends AbstractAdapter<Byte> {

    public ByteAdapter() {
        super(byte.class);
    }

    @Override
    public Byte getValue(ResultSet result, String label) throws SQLException {
        return result.getByte(label);
    }

    @Override
    public void setValue(PreparedStatement statement, int index, Byte value) throws SQLException {
        statement.setByte(index, value);
    }

}
