package org.valdi.SuperApiX.common.databases.advanced.adapters;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BigDecimalAdapter extends AbstractAdapter<BigDecimal> {

    public BigDecimalAdapter() {
        super(BigDecimal.class);
    }

    @Override
    public BigDecimal getValue(ResultSet result, String label) throws SQLException {
        return result.getBigDecimal(label);
    }

    @Override
    public void setValue(PreparedStatement statement, int index, BigDecimal value) throws SQLException {
        statement.setBigDecimal(index, value);
    }

}
