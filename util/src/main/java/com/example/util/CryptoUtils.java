package com.example.util;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import java.text.ParseException;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {

  public static String decryptJwe(String jwe, byte[] key) {
    try {
      JWEObject jweObject = JWEObject.parse(jwe);
      SecretKeySpec aesKey = new SecretKeySpec(key, "AES");
      jweObject.decrypt(new DirectDecrypter(aesKey));
      Payload payload = jweObject.getPayload();
      return payload.toString();
    } catch (ParseException | JOSEException e) {
      throw new RuntimeException(e);
    }
  }

  public static String encryptAsJwe(String content, byte[] key) {
    try {
      JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256GCM);
      Payload payload = new Payload(content);
      JWEObject jweObject = new JWEObject(header, payload);
      SecretKeySpec aesKey = new SecretKeySpec(key, "AES");
      jweObject.encrypt(new DirectEncrypter(aesKey));
      return jweObject.serialize();
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }
}
