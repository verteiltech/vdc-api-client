package com.verteil.apiclient.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ObjectMapperConfig {

    private ObjectMapperConfig() {
    }

    public static ObjectMapperConfig getInstance() {
        return LazyHolder.INSTANCE;
    }

    public ObjectMapper createMapper() {
        ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, true)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        var typeFactory = objectMapper.getTypeFactory();
        var annotationIntrospect = new JaxbAnnotationIntrospector(typeFactory, false);
        objectMapper.setAnnotationIntrospector(annotationIntrospect);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(dateFormat);
        return objectMapper;
    }

    private static class LazyHolder {
        static final ObjectMapperConfig INSTANCE = new ObjectMapperConfig();
    }
}
