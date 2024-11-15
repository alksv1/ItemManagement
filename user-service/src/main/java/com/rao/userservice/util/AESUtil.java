package com.rao.userservice.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

public class AESUtil {

    private final Integer EXPIRE;

    private final Cipher encodeCipher;

    private final Cipher decodeCipher;

    public AESUtil(String AES_KEY, Integer EXPIRE) throws Exception {
        this.EXPIRE = EXPIRE;
        SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        encodeCipher = Cipher.getInstance("AES");
        encodeCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        decodeCipher = Cipher.getInstance("AES");
        decodeCipher.init(Cipher.DECRYPT_MODE, secretKey);
    }

    public String encrypt(String data) throws IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedData = encodeCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public String decrypt(String encryptedData) throws IllegalBlockSizeException, BadPaddingException {
        byte[] decodeData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = decodeCipher.doFinal(decodeData);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    public String generateEncryptString(String email, String captcha) throws IllegalBlockSizeException, BadPaddingException {
        LocalDateTime issueTime = LocalDateTime.now();
        LocalDateTime expireTime = issueTime.plus(Duration.ofMillis(EXPIRE));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("captcha", captcha);
        jsonObject.put("issueTime", issueTime);
        jsonObject.put("expireTime", expireTime);

        String JSONString = JSON.toJSONString(jsonObject);

        return encrypt(JSONString);
    }

}
