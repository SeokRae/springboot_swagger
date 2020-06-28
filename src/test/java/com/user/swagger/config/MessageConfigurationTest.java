package com.user.swagger.config;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

public class MessageConfigurationTest {

    @Value("${server.port}")
    String basename;

    @Test
    public void testProp() {
        System.out.println(basename);
    }
}