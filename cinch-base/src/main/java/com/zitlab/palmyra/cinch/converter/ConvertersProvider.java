package com.zitlab.palmyra.cinch.converter;

import java.util.Map;


public interface ConvertersProvider {
    void fill(Map<Class<?>,Converter<?>> mapToFill);
}
