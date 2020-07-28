package com.ypt.springboot.Controller;

import com.alibaba.fastjson.JSONObject;
import com.ypt.springboot.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@RequestMapping(value = "/redis")
@RestController
public class RedisController {
    @Autowired
    RedisTemplate redisTemplate;
    @GetMapping(value = "/setSession")
    public String session(HttpSession session) {
        //设置session
        session.setAttribute("website", "www.phpsong.com");
        //获取session
        String website = session.getAttribute("website").toString();
        return website;
    }
    @RequestMapping(value = "/redis",method = RequestMethod.POST)
    public String redis(){
        redisTemplate.opsForValue().set("redis","redis测试",10, TimeUnit.SECONDS);
        redisTemplate.opsForValue().get("redis");
        System.out.println(redisTemplate.opsForValue().get("redis"));
//        redisTemplate.delete("key");
        return null;
    }
    @GetMapping("/testRedis")
    public String test1(HttpSession session) throws Exception{
        //RedisCache.setStr(String.valueOf(utils.getNowTimeSecond()),(String) SecurityUtils.getSubject().getSession().getId());
        //RedisCache.setStr(String.valueOf(utils.getNowTimeSecond()),String.valueOf(session.getId()));

        session.setAttribute("admin","haha");
        String haha = (String) session.getAttribute("admin");
        return new String(session.getId()+"----"+haha);
    }

    @GetMapping("/getRedis")
    public String test2(HttpSession session) throws Exception{
        //RedisCache.setStr(String.valueOf(utils.getNowTimeSecond()),(String) SecurityUtils.getSubject().getSession().getId());
        //RedisCache.setStr(String.valueOf(utils.getNowTimeSecond()),String.valueOf(session.getId()));
        String haha = (String) session.getAttribute("admin");
        return haha;
    }
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(@RequestParam(value = "name") String name, @RequestParam(value = "pass") String pass,
                        HttpServletRequest request){
        if ("1234".equals(name) && "123456".equals(pass)){
            /*如果已经存在Session的话，直接返回它；没有就创建一个，再返回
             * 当然Session是自动放在response中的Header中的，这里不用做其他处理*/
            request.getSession();
        }else {
            return "failed";
        }
        return "success";
    }
    @RequestMapping(value = "/isValid",method = RequestMethod.GET)
    public String isSessionValid(HttpServletRequest request){
        //简化if-else表达式（其实很多地方可以简化的，这里为了方便新手朋友可以看得顺畅点，我尽量不简化）
        return request.isRequestedSessionIdValid() ? "ok":"no";
    }
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String logout(HttpSession session){
        session.invalidate();//使Session变成无效，及用户退出
        return "logout";
    }
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public User test(){
        String jsonStr = "{\"id\":1,\"username\":\"known\"}";
        JSONObject obj = JSONObject.parseObject(jsonStr);
        obj.put("sex","男");
        System.out.println(obj.toJSONString());
        User user = obj.toJavaObject(User.class);
        System.out.println(user.toString());
        return user;
    }
}
