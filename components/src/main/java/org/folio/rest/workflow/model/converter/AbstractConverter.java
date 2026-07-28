package org.folio.rest.workflow.model.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;

/**
 * This converts the value into a JSON representation stored as a single string tin the database.
 */
public abstract class AbstractConverter<T> implements AttributeConverter<T, String> {

  private ObjectMapper objectMapper = JsonMapper.builder()
    .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
    .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .disable(MapperFeature.REQUIRE_TYPE_ID_FOR_SUBTYPES)
    .disable(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY)
    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .addModule(new JavaTimeModule())
    .build();

  @Override
  public String convertToDatabaseColumn(T attribute) {
    if (attribute == null) return null;

    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public T convertToEntityAttribute(String dbData) {
    if (dbData == null) return null;

    try {
      return objectMapper.readValue(dbData, getTypeReference());
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public abstract TypeReference<T> getTypeReference();

}
