package com.ypt.springboot.Controller;

import com.alibaba.fastjson.JSONObject;
import com.ypt.springboot.bean.User;
import com.ypt.springboot.ftpTest.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

@RequestMapping(value = "/redis")
@RestController
public class RedisController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisController.class);
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

    @RequestMapping(value = "/redis", method = RequestMethod.POST)
    public String redis() {
        redisTemplate.opsForValue().set("redis", "redis测试", 10, TimeUnit.SECONDS);
        redisTemplate.opsForValue().get("redis");
        System.out.println(redisTemplate.opsForValue().get("redis"));
//        redisTemplate.delete("key");
        return null;
    }

    @GetMapping("/testRedis")
    public String test1(HttpSession session) throws Exception {
        //RedisCache.setStr(String.valueOf(utils.getNowTimeSecond()),(String) SecurityUtils.getSubject().getSession().getId());
        //RedisCache.setStr(String.valueOf(utils.getNowTimeSecond()),String.valueOf(session.getId()));

        session.setAttribute("admin", "haha");
        String haha = (String) session.getAttribute("admin");
        return new String(session.getId() + "----" + haha);
    }

    @GetMapping("/getRedis")
    public String test2(HttpSession session) throws Exception {
        //RedisCache.setStr(String.valueOf(utils.getNowTimeSecond()),(String) SecurityUtils.getSubject().getSession().getId());
        //RedisCache.setStr(String.valueOf(utils.getNowTimeSecond()),String.valueOf(session.getId()));
        String haha = (String) session.getAttribute("admin");
        return haha;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "name") String name, @RequestParam(value = "pass") String pass,
                        HttpServletRequest request) {
        if ("1234".equals(name) && "123456".equals(pass)) {
            /*如果已经存在Session的话，直接返回它；没有就创建一个，再返回
             * 当然Session是自动放在response中的Header中的，这里不用做其他处理*/
            request.getSession();
        } else {
            return "failed";
        }
        return "success";
    }

    @RequestMapping(value = "/isValid", method = RequestMethod.GET)
    public String isSessionValid(HttpServletRequest request) {
        //简化if-else表达式（其实很多地方可以简化的，这里为了方便新手朋友可以看得顺畅点，我尽量不简化）
        return request.isRequestedSessionIdValid() ? "ok" : "no";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.invalidate();//使Session变成无效，及用户退出
        return "logout";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public User test() {
        String jsonStr = "{\"id\":1,\"username\":\"known\"}";
        JSONObject obj = JSONObject.parseObject(jsonStr);
        obj.put("sex", "男");
        System.out.println(obj.toJSONString());
        User user = obj.toJavaObject(User.class);
        System.out.println(user.toString());
        return user;
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public String downloadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam String path) throws IOException {
        String fileName = path.substring(path.lastIndexOf("/")+1);
        //设置文件路径
//        File file = new File("d:/2.txt");
        //File file = new File(realPath , fileName);
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
        Test t = new Test();
        t.initFtpClient();
        t.ftpClient.changeWorkingDirectory(path);
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            is = t.ftpClient.retrieveFileStream("2");
            bis = new BufferedInputStream(is);
            OutputStream out = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = bis.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "false";
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return "false";
                }
            }
        }
        t.ftpClient.disconnect();
        return "true";
    }

    @PostMapping(value = "/uploadFile")
    public JSONObject uploadFile(@RequestParam MultipartFile[] files, @RequestParam String pathname){
        JSONObject jsonString = new JSONObject();
        Test t = new Test();
        t.initFtpClient();
        try {
            t.uploadFile(files,pathname);
        } catch (IOException e) {
            e.printStackTrace();
            jsonString.put("msg",false);
            return jsonString;
        }
        jsonString.put("msg",true);
        return jsonString;
    }

    //用Element方式
    public static void element(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            NodeList childNodes = element.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                    //获取节点
                    System.out.print(childNodes.item(j).getNodeName() + ":");
                    //获取节点值
                    System.out.println(childNodes.item(j).getFirstChild().getNodeValue());
                }
            }
        }
    }

    public static void node(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            NodeList childNodes = node.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                    System.out.print(childNodes.item(j).getNodeName() + ":");
                    System.out.println(childNodes.item(j).getFirstChild().getNodeValue());
                }
            }
        }
    }

    public static void main(String[] args) {
        //1.创建DocumentBuilderFactory对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //2.创建DocumentBuilder对象
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document d = builder.parse("src/main/resources/demo.xml");
            NodeList sList = d.getElementsByTagName("student");
            element(sList);
//            node(sList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
