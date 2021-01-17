package com.zitlab.palmyra.cinch.converter;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Static class used to register new converters.
 * This class is based on sql2o library
 */
@SuppressWarnings("unchecked")
public class Convert {

    private static final ReentrantReadWriteLock rrwl = new ReentrantReadWriteLock();
    private static final ReentrantReadWriteLock.ReadLock rl = rrwl.readLock();

    private static Map<Class<?>, Converter<?>> registeredConverters = new HashMap<Class<?>, Converter<?>>();

    private static void processProvider(ConvertersProvider convertersProvider) {
        convertersProvider.fill(registeredConverters);
    }

    private static void fillDefaults(Map<Class<?>, Converter<?>> mapToFill) {
        mapToFill.put(Integer.class, IntConverter.instance());
        mapToFill.put(int.class, IntConverter.instance());

        mapToFill.put(Double.class, DoubleConverter.instance());
        mapToFill.put(double.class, DoubleConverter.instance());

        mapToFill.put(Float.class, FloatConverter.instance());
        mapToFill.put(float.class, FloatConverter.instance());

        mapToFill.put(Long.class, LongConverter.instance());
        mapToFill.put(long.class, LongConverter.instance());

        mapToFill.put(Short.class, ShortConverter.instance());
        mapToFill.put(short.class, ShortConverter.instance());

        mapToFill.put(Byte.class, ByteConverter.instance());
        mapToFill.put(byte.class, ByteConverter.instance());

        mapToFill.put(BigDecimal.class, BigDecimalConverter.instance());

        mapToFill.put(String.class, StringConverter.instance());

        mapToFill.put(java.util.Date.class,DateConverter.instance());
        mapToFill.put(java.sql.Date.class, SqlDateConverter.instance());
        mapToFill.put(java.sql.Time.class, TimeConverter.instance());
        mapToFill.put(java.sql.Timestamp.class,TimestampConverter.instance());

        
        mapToFill.put(Boolean.class, BooleanConverter.instance());
        mapToFill.put(boolean.class, BooleanConverter.instance());

        
        mapToFill.put(byte[].class, BytesConverter.instance());

        
        mapToFill.put(InputStream.class, AsciiStreamConverter.instance());
        mapToFill.put(ByteArrayInputStream.class, BinaryStreamConverter.instance());

        mapToFill.put(UUID.class, UUIDConverter.instance());

    }


    static {
        fillDefaults(registeredConverters);
        ServiceLoader<ConvertersProvider> loader = ServiceLoader.load(ConvertersProvider.class);
        for (ConvertersProvider provider : loader) {
            processProvider(provider);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @Deprecated
    public static Converter getConverter(Class clazz) throws ConverterException {
        return throwIfNull(clazz, getConverterIfExists(clazz));
    }

    public static <E> Converter<E> throwIfNull(Class<E> clazz, Converter<E> converter) throws ConverterException {
        if (converter == null) {
            throw new ConverterException("No converter registered for class: " + clazz.getName());
        }
        return converter;
    }

    public static <E> Converter<E> getConverterIfExists(Class<E> clazz) {
        Converter c;
        rl.lock();
        try {
            c = registeredConverters.get(clazz);
        } finally {
            rl.unlock();
        }
        return c;
    }

    private static void registerConverter0(Class clazz, Converter converter) {
        registeredConverters.put(clazz, converter);
    }

}
