package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NCharacterStreamAdapter extends AbstractAdapter<Reader> {

    public NCharacterStreamAdapter() {
        super(Reader.class);
    }

    @Override
    public Reader getValue(ResultSet result, String label) throws SQLException {
        return result.getNCharacterStream(label);
    }

    @Override
    public void setValue(PreparedStatement statement, int index, Reader value) throws SQLException {
        statement.setNCharacterStream(index, value);
    }

}
