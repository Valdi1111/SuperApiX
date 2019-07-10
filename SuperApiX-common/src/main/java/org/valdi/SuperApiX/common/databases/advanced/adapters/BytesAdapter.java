package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BytesAdapter extends AbstractAdapter<byte[]> {

    public BytesAdapter() {
        super(byte[].class);
    }

    @Override
    public byte[] getValue(ResultSet result, String label) throws SQLException {
        return result.getBytes(label);
    }

    @Override
    public void setValue(PreparedStatement statement, int index, byte[] value) throws SQLException {
        statement.setBytes(index, value);
    }

}
