package com.zitlab.palmyra.cinch.quirks.db2;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import com.zitlab.palmyra.cinch.converter.Converter;
import com.zitlab.palmyra.cinch.quirks.NoQuirks;


public class Db2Quirks extends NoQuirks {
    public Db2Quirks() {
        super();
    }

    public Db2Quirks(Map<Class, Converter> converters) {
        super(converters);
    }

    @Override
    public String getColumnName(ResultSetMetaData meta, int colIdx) throws SQLException {
        return meta.getColumnName(colIdx);
    }
}
