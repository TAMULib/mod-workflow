package org.folio.rest.workflow.model.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import org.folio.rest.workflow.exception.SecureOperationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class CryptoConverterTest {

  @Mock
  private Environment environment;

  @InjectMocks
  private CryptoConverter cryptoConverter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    cryptoConverter.setEnvironment(environment);
    lenient()
        .when(environment.getProperty(eq("encryption.secret")))
        .thenReturn("T0pS3cr3tK3y2024");
  }

  @Test
  void testConvertToDatabaseColumn() {
    String input = "testValue";
    String encrypted = cryptoConverter.convertToDatabaseColumn(input);
    assertNotNull(encrypted);
    assertNotEquals(input, encrypted);
  }

  @Test
  void testConvertToEntityAttribute() {
    String input = "encryptedValue";
    String result = cryptoConverter.convertToEntityAttribute(input);
    assertEquals(input, result);
  }

  @Test
  void testEncryptionAndDecryption() {
    String originalValue = "sensitiveData";
    String encrypted = cryptoConverter.convertToDatabaseColumn(originalValue);
    String decrypted = cryptoConverter.decrypt(encrypted);
    assertEquals(originalValue, decrypted);
  }

  @Test
  void testEmptyValue() {
    String emptyValue = "";
    String result = cryptoConverter.convertToDatabaseColumn(emptyValue);
    assertEquals(emptyValue, result);
  }

  @Test
  void testNullValue() {
    String nullValue = null;
    String result = cryptoConverter.convertToDatabaseColumn(nullValue);
    assertNull(result);
  }

  @Test
  void testInvalidDecryption() {
    String invalidEncrypted = "invalidEncryptedValue";
    assertThrows(SecureOperationException.class, () -> cryptoConverter.decrypt(invalidEncrypted));
  }

  @Test
  void testDifferentSecretKeys() {
    String input = "testValue";
    String encrypted1 = cryptoConverter.convertToDatabaseColumn(input);

    when(environment.getProperty(eq("encryption.secret")))
        .thenReturn("T0pS3cr3tK3y2025");

    String encrypted2 = cryptoConverter.convertToDatabaseColumn(input);
    assertNotEquals(encrypted1, encrypted2);
  }

}
