package com.example.warehouse;

import com.example.util.CryptoUtils;
import com.example.util.JsonUtils;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RefundGenerationService {

  public void generateReport(Path refundsFile, List<Refund> refunds, String key, String salt) {
    try {
      String refundsJson = JsonUtils.toJson(refunds);
      JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256GCM);
      Payload payload = new Payload(refundsJson);

      JWEObject jweObject = new JWEObject(header, payload);
      jweObject.encrypt(new DirectEncrypter(CryptoUtils.deriveKey(key,salt)));
      String jweString = jweObject.serialize();
      Files.writeString(refundsFile, jweString);
    } catch (IOException | JOSEException e) {
      throw new RuntimeException(e);
    }
  }
}
