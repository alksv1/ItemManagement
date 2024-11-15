package com.rao.userservice;

import com.rao.common.util.ForFilterUtil;
import com.rao.userservice.util.AESUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.util.Objects;

//@SpringBootTest
class UserServiceApplicationTests {

    private AESUtil aesUtil = new AESUtil("1234567890123456", 300000);

    UserServiceApplicationTests() throws Exception {

    }

    @Test
    void contextLoads() throws IllegalBlockSizeException, BadPaddingException {
        String s = aesUtil.generateEncryptString("3292736805@qq.com", "123456");
        System.out.println(s);
        String decrypt = aesUtil.decrypt(s);
        System.out.println(decrypt);
        String decrypt1 = aesUtil.decrypt("f5Gse92H3lWn+jdtNpQvKGRWQe/0NAE+BoUl5aGZuN/DqPo/10r5iJLAY69lEurPxUDbts3riZcZkw+xwxL5AQrrQ4sJpGper8HkMFHLqECZsmzyInytod16i4skUyJH/FsQhRU3hnTclQA7Ws5kcEYtydmoIuudv2PwYuqOIhODnQ7OOO3njN1WDg8tD8yP");
        System.out.println(decrypt1);
    }

}
