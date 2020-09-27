package com.example.payments;

import com.example.util.CryptoUtils;
import com.example.util.JsonUtils;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import org.springframework.stereotype.Component;

@Component
public class PaymentService {

  public void processRefunds(Path refundsFile, String key, String salt) {
    try {
      JWEObject jweObject =  JWEObject.parse(Files.readString(refundsFile));
      jweObject.decrypt(new DirectDecrypter(CryptoUtils.deriveKey(key,salt)));
      Payload payload = jweObject.getPayload();
      String refundsJson = payload.toString();
      System.out.println("Issuing Refund to");
      System.out.println(refundsJson);
    } catch (IOException | ParseException | JOSEException e) {
      throw new RuntimeException(e);
    }
  }
}
