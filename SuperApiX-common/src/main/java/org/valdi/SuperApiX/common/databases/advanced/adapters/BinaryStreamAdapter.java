package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BinaryStreamAdapter extends AbstractAdapter<InputStream> {

    public BinaryStreamAdapter() {
        super(InputStream.class);
    }

    @Override
    public InputStream getValue(ResultSet result, String label) throws SQLException {
        return result.getBinaryStream(label);
    }

    @Override
    public void setValue(PreparedStatement statement, int index, InputStream value) throws SQLException {
        statement.setBinaryStream(index, value);
    }

}
