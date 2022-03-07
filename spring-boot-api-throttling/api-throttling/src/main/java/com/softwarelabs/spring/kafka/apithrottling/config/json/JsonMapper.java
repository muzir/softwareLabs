package com.softwarelabs.spring.kafka.apithrottling.config.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;

import static com.fasterxml.jackson.core.json.JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static java.util.Objects.isNull;

public interface JsonMapper {

    JsonMapper DEFAULT = () -> {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.getDeserializationConfig().withFeatures(ALLOW_NON_NUMERIC_NUMBERS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new ParameterNamesModule());
        mapper.registerModule(new Jdk8Module());
        return mapper;
    };

    ObjectMapper newMapper();

    default <T> T read(String str, Class<T> type) {
        if (isNull(str)) {
            return null;
        }
        try {
            return newMapper().readValue(str, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
