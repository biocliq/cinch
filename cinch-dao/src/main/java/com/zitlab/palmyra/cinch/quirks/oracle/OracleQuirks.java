
package com.zitlab.palmyra.cinch.quirks.oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.zitlab.palmyra.cinch.converter.Converter;
import com.zitlab.palmyra.cinch.quirks.NoQuirks;

public class OracleQuirks extends NoQuirks {
    public OracleQuirks() {
        super(new HashMap<Class, Converter>() {{
            put(UUID.class, new OracleUUIDConverter());
        }});
    }

    public OracleQuirks(Map<Class, Converter> converters) {
        super(converters);
    }

    @Override
    public Object getRSVal(ResultSet rs, int idx) throws SQLException {
        Object o = super.getRSVal(rs, idx);

        if (o != null && o.getClass().getCanonicalName().startsWith("oracle.sql.TIMESTAMP")){
            o = rs.getTimestamp(idx);
        }
        return o;
    }

    @Override
    public void setParameter(PreparedStatement statement, int paramIdx, UUID value) throws SQLException {
        statement.setBytes(paramIdx, (byte[])new OracleUUIDConverter().toDatabaseParam(value));
    }
}
