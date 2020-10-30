package com.ypt.springboot.Controller;

import com.alibaba.fastjson.JSONObject;
import com.ypt.springboot.Config.JsonResult;
import com.ypt.springboot.Mapper.UserMapper;
import com.ypt.springboot.bean.User;
import com.ypt.springboot.ftpTest.FileTree;
import com.ypt.springboot.ftpTest.Test;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequestMapping(value = "/redis")
@RestController
public class RedisController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisController.class);
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserMapper userMapper;
    @Resource
    Test t;

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

    @PostMapping(value = "/uploadFile")
    public JsonResult uploadFile(@RequestParam(value = "fileName", required = false) MultipartFile[] files) throws IOException {
        t.initFtpClient();
        String pathname = "/1/111";
        JsonResult msg = t.uploadFile(files, pathname);
        return msg;
    }

    @PostMapping("/fileList")
    public JsonResult fileList(@RequestParam(value = "path", required = false) String path) throws IOException {
        t.initFtpClient();
        try {
            if (null == path) {
                path = "/";
            }
            List<FileTree> list = t.getFileList(path);
            return new JsonResult(true, list);
        } catch (IOException e) {
            LOGGER.info("error", e);
            return new JsonResult(false, "error");
        } finally {
            t.ftpClient.disconnect();
        }
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<User> userList = userMapper.findAll();
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();
        Row row = null;
        XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        Cell cell = null;
        int rowNum = userList.size() + 1;
        int colNum = 5;
        String[][] list = new String[rowNum][colNum];
        for (int i = 0; i < userList.size(); i++) {
            String[] data = list[i];
            if (i == 0) {
                data[0] = "ID";
                data[1] = "姓名";
                data[2] = "生日";
                data[3] = "性别";
                data[4] = "地址";
            } else {
                User u = userList.get(i - 1);
                data[0] = u.getId().toString();
                data[1] = u.getUsername();
                data[2] = u.getBirthday().toString();
                data[3] = u.getSex();
                data[4] = u.getAddress();
            }
        }
        for (int i = 0; i < rowNum; i++) {
            row = sheet.createRow(i);
            for (int j = 0; j < 5; j++) {
                cell = row.createCell(j);
                cell.setCellValue(list[i][j]);
                cell.setCellStyle(style);
            }
        }
        OutputStream out = null;
        String fileName = "信息表";
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        out = response.getOutputStream();
        wb.write(out);
        if (null != out) {
            out.close();
        }
    }

    @PostMapping("/down")
    public void downZip(HttpServletResponse response){
        try {
            String fileName = "信息表";
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".zip");
            OutputStream fos = response.getOutputStream();
            ZipUtils.toZip("D:/新建文件夹",fos,true);

            if (null != fos){
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    //解析xml
//    public static void main(String[] args) {
//        //1.创建DocumentBuilderFactory对象
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        //2.创建DocumentBuilder对象
//        try {
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document d = builder.parse("src/main/resources/demo.xml");
//            NodeList sList = d.getElementsByTagName("student");
//            element(sList);
////            node(sList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
