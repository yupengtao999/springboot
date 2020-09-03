package com.ypt.springboot;

import com.alibaba.fastjson.JSONArray;
import com.ypt.springboot.Controller.UserController;
import com.ypt.springboot.Mapper.UserMapper;
import com.ypt.springboot.bean.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class server {
    @Autowired
    UserController userController;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserMapper userMapper;
    @Test
    public void allUser() throws Exception {
        try {
            String msg = mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            System.out.println("rest请求结果如下：\n"+msg);
            List<User> a = JSONArray.parseArray(msg,User.class);
            for (User u:a){
                System.out.println(u.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Test
    public void b(){
        List<User> users = userMapper.findAll();
        System.out.println(users.toString());
    }
}
