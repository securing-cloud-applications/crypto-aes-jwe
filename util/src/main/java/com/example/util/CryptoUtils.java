package com.example.util;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {

  public static String decryptJwe(String jwe, String key, String salt) {
    try {
      JWEObject jweObject = JWEObject.parse(jwe);
      jweObject.decrypt(new DirectDecrypter(deriveKey(key, salt)));
      Payload payload = jweObject.getPayload();
      return payload.toString();
    } catch (ParseException | JOSEException e) {
      throw new RuntimeException(e);
    }
  }

  public static String encryptAsJwe(String content, String key, String salt) {
    try {
      JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256GCM);
      Payload payload = new Payload(content);
      JWEObject jweObject = new JWEObject(header, payload);
      jweObject.encrypt(new DirectEncrypter(deriveKey(key, salt)));
      return jweObject.serialize();
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }

  private static SecretKeySpec deriveKey(String password, String salt) {
    try {
      PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      SecretKey derivedKey = keyFactory.generateSecret(keySpec);
      return new SecretKeySpec(derivedKey.getEncoded(), "AES");
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }
  }
}
