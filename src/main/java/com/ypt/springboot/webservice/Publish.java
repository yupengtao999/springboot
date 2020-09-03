package com.ypt.springboot.webservice;

import com.sun.tools.javac.util.List;
import com.ypt.springboot.bean.User;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.web.multipart.MultipartFile;
import sun.net.ftp.FtpClient;

import javax.xml.ws.Endpoint;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Publish {
    //    public static void main(String[] args) {
//        String address = "http://127.0.0.1:8087/webservice/service";
//        Endpoint.publish(address,new Server());
//        System.out.println("发布成功");
//    }
//public static void main(String[] args) throws Exception {
//    String re = "\\d";
//    for (String s : List.of("010-12345678", "020-9999999", "0755-7654321")) {
//        if (!s.matches(re)) {
//            System.out.println("测试失败: " + s);
//            return;
//        }
//    }
//    for (String s : List.of("010 12345678", "A20-9999999", "0755-7654.321")) {
//        if (s.matches(re)) {
//            System.out.println("测试失败: " + s);
//            return;
//        }
//    }
//    System.out.println("测试成功!");
//}
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        File file = new File("d:/test" + File.separator);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (file.isDirectory()) {
            File res[] = file.listFiles();
            for (int x = 0; x < res.length; x++) {
                System.out.println((res[x].isDirectory() ? "文件夹名称：" : "文件名称") + res[x].getName() + "\t\t修改日期：" +
                        format.format(new Date(res[x].lastModified())) + "\t" + "文件大小为：" + (res[x].length()) + "B" + "\t" + res[x].getPath() + "\t" +"文件数：" + res.length);
//                if (res[x].getName().equals("test")){
//                    System.out.println(res[x].delete());
//                }
//                System.out.println(res[x].delete());
            }
//            DeleteFolder("d:/test/1");
            FileUtils.deleteDirectory(new File("d:/test/1"));
        }
        ArrayList<User> users = new ArrayList<>();
        User user1 = new User(2, "a");
        User user2 = new User(4, "b");
        User user3 = new User(6, "c");
        users.add(user1);
        users.add(user2);
        users.add(user3);
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.getId().compareTo(o1.getId());
            }
        });
        String a = "asdfad";
        System.out.println(a.indexOf("3"));
        System.out.println(users.toString());
    }
    public static boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }


}
