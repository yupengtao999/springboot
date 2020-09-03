package com.ypt.springboot;

import com.ypt.springboot.Mapper.UserMapper;
import com.ypt.springboot.bean.FullRandom;
import com.ypt.springboot.bean.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class test {
    @Autowired
    UserMapper userMapper;
    @Test
    public void a(){
        Assert.assertEquals(2,new FullRandom().add(1,1));
    }
    @Test
    public void b(){
        List<User> users = userMapper.findAll();
        System.out.println(users.toString());
    }
}
