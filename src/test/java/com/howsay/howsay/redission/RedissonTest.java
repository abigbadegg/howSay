package com.howsay.howsay.redission;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedissonTest {

    @Autowired
    private RedissonClient client;

    @Test
    public void test() {
        client.getBucket("hello").set("bug");

        String test = (String) client.getBucket("hello").get();
        System.out.println(test);
    }
}
