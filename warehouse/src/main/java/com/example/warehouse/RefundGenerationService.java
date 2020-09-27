package com.example.warehouse;

import com.example.util.CryptoUtils;
import com.example.util.JsonUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RefundGenerationService {

  public void generateReport(Path refundsFile, List<Refund> refunds, byte[] key) {
    try {
      String refundsJson = JsonUtils.toJson(refunds);
      String jwe = CryptoUtils.encryptAsJwe(refundsJson, key);
      Files.writeString(refundsFile, jwe);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
