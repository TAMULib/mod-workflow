package org.folio.rest.workflow.model.converter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Security;
import java.util.Base64;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.folio.rest.workflow.exception.SecureOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

  private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
  private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
  private static final int KEY_SIZE = 128;

  private static final String ENCRYPTION_SECRET_PROPERTY = "encryption.secret";

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  private static Environment environment;

  @Autowired
  public void setEnvironment(Environment env) {
    environment = env;
  }

  @Override
  public String convertToDatabaseColumn(String entityValue) {
    if (StringUtils.isEmpty(entityValue)) {
      return entityValue;
    }
    return encrypt(entityValue);
  }

  @Override
  public String convertToEntityAttribute(String dbValue) {
    if (StringUtils.isEmpty(dbValue)) {
      return dbValue;
    }
    return decrypt(dbValue);
  }

  public String encrypt(String value) {
    try {
      Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
      byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      throw new SecureOperationException("Encryption failed", e);
    }
  }

  public String decrypt(String value) {
    try {
      Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
      byte[] decoded = Base64.getDecoder().decode(value);
      byte[] decrypted = cipher.doFinal(decoded);
      return new String(decrypted, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new SecureOperationException("Decryption failed", e);
    }
  }

  private Cipher getCipher(int mode) throws Exception {
    String secret = environment.getProperty(ENCRYPTION_SECRET_PROPERTY);

    if (secret == null || secret.length() != KEY_SIZE / 8) {
      throw new IllegalArgumentException("Invalid secret length. Expected " + (KEY_SIZE / 8) + " characters.");
    }

    Key key = createKey(secret);

    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    cipher.init(mode, key);
    return cipher;
  }

  private Key createKey(String secret) throws Exception {
    SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
    PBEKeySpec spec = new PBEKeySpec(secret.toCharArray(), secret.getBytes(StandardCharsets.UTF_8), 65536, KEY_SIZE);
    SecretKey tmp = factory.generateSecret(spec);
    return new SecretKeySpec(tmp.getEncoded(), "AES");
  }

}
