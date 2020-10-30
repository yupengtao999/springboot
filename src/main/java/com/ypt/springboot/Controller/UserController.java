package com.ypt.springboot.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ypt.springboot.Config.MqttGateway;
import com.ypt.springboot.Mapper.UserMapper;
import com.ypt.springboot.bean.JwtUtil;
import com.ypt.springboot.bean.User;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Resource
    UserMapper userMapper;
    @Resource
    MqttGateway mqttGateway;

    @GetMapping("/user")
    public List<User> findAll(){
        return userMapper.findAll();
    }
    @GetMapping("/user/{id}")
    public User findUserById(@PathVariable("id") Integer id){
        return userMapper.findUserById(id);
    }

    @PutMapping("/user")
    public String update(@RequestParam("id") String id,
                         @RequestParam("username") String username,
                         @RequestParam("birthday") String birthday,
                         @RequestParam("sex") String sex,
                         @RequestParam("address") String address) throws ParseException {
        User user = new User();
        user.setId(Integer.parseInt(id));
        user.setUsername(username);
        //对日期进行格式转换
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(birthday);
        user.setBirthday(date);
        user.setSex(sex);
        user.setAddress(address);
        return userMapper.updateUser(user) == 1 ? "success" : "failed";
    }

    @PostMapping("/user")
    public String insertUser(@RequestParam("username") String username,
                             @RequestParam("birthday") String birthday,
                             @RequestParam("sex") String sex,
                             @RequestParam("address") String address) throws ParseException {
        User user = new User();
        user.setUsername(username);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(birthday);
        user.setBirthday(date);
        user.setSex(sex);
        user.setAddress(address);
        user.setId(confirm_id());
        JSONObject user1 = JSONObject.parseObject(JSON.toJSONString(user));
        System.out.println(user1);
        return userMapper.addUser(user) == 1 ? "success":"failed";
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") Integer id){
        LOGGER.info("删除成功");
        return userMapper.deleteUser(id) == 1 ? "success" : "failed";
    }

    public int confirm_id(){
        List<User> list = userMapper.findAll();							//先查询所有条目，以确定id
        int id = list.get(list.size()-1).getId() +1;					//如果不存在id乱序现象,则新条目id为最后一条的id+1
        for(int i=0;i<list.size();i++){
            if((i+1) != list.get(i).getId()){
                id=i+1;
            }
        }
        return id;
    }
    @GetMapping("/test")
    public String sendMqtt(@RequestParam("data") String data,@RequestParam("topic") String topic){
        mqttGateway.sendToMqtt(data,topic);
        return "ok";
    }
    @PostMapping("/login")
    public String login(@RequestParam("username") String username){
        User user = userMapper.findByName(username);
        if (null != user){
            String token = JwtUtil.sign(username,user.getId().toString());
            if (token != null){
                return "登陆成功"+token;
            }
        }
        return "登陆失败";
    }
    @PostMapping("/photo")
    public String photo(@RequestBody MultipartFile file) throws IOException {
        CommonsMultipartFile cmf = (CommonsMultipartFile) file;
        DiskFileItem dfi = (DiskFileItem) cmf.getFileItem();
        File f = dfi.getStoreLocation();
        byte[] photo = cmf.getBytes();
        userMapper.addPhoto(1,photo);
        return "成功";
    }
}
