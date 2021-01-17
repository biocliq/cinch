package com.zitlab.palmyra.cinch.quirks.oracle;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.zitlab.palmyra.cinch.converter.Converter;
import com.zitlab.palmyra.cinch.converter.ConverterException;
import com.zitlab.palmyra.cinch.converter.UUIDConverter;


public class OracleUUIDConverter implements Converter<UUID> {

    private final Converter<UUID> baseConverter = UUIDConverter.instance();

    @Override
    public UUID convert(Object val) throws ConverterException {
    	if(null == val)
    		return null;
    	
        if (val instanceof byte[]) {
            ByteBuffer bb = ByteBuffer.wrap((byte[])val);

            long mostSignigcant = bb.getLong();
            long leastSignificant = bb.getLong();

            return new UUID(mostSignigcant, leastSignificant);
        } else {
            return baseConverter.convert(val);
        }
    }

    @Override
    public Object toDatabaseParam(UUID val) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(val.getMostSignificantBits());
        bb.putLong(val.getLeastSignificantBits());
        return bb.array();
    }

	@Override
	public UUID read(ResultSet rs, int columnIndex) throws SQLException {
		return convert(rs.getObject(columnIndex));
	}
}
