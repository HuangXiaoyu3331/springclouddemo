package com.hxy.user.server.service.impl;

import com.hxy.user.server.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void checkValid() {
        userService.register(null);
    }
}