package com.zitlab.palmyra.cinch.converter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Used by sql2o to convert a value from the database into a {@link UUID}.
 */
public class UUIDConverter implements Converter<UUID> {
	
	private static final Converter<UUID> instance = new UUIDConverter();
	
	private UUIDConverter() {
		
	}
	
	public static Converter<UUID> instance() {
		return instance;
	}
	
	
    public UUID convert(Object val) throws ConverterException {
        if (val == null){
            return null;
        }

        if (val instanceof UUID){
            return (UUID)val;
        }

        if(val instanceof String){
            return UUID.fromString((String) val);
        }

        throw new ConverterException("Cannot convert type " + val.getClass() + " " + UUID.class);
    }

	@Override
	public UUID read(ResultSet rs, int columnIndex) throws SQLException {
		return convert(rs.getObject(columnIndex));
	}
}

