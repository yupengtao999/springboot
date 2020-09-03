package com.ypt.springboot.ftpTest;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Test {
    //ftp服务器地址
    public String hostname = "172.16.6.80";
    //ftp服务器端口号默认为21
    public Integer port = 21;
    //ftp登录账号
    public String username = "test";
    //ftp登录密码
    public String password = "123456";

    public FTPClient ftpClient = null;

    /**
     * 初始化ftp服务器
     */
    public void initFtpClient() {
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding("GBK");
        FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
        conf.setServerLanguageCode("zh");

        try {
//            System.out.println("connecting...ftp服务器:" + this.hostname + ":" + this.port);
            ftpClient.connect(hostname, port); //连接ftp服务器
            ftpClient.login(username, password); //登录ftp服务器
            int replyCode = ftpClient.getReplyCode(); //是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
//                System.out.println("connect failed...ftp服务器:" + this.hostname + ":" + this.port);
            }
//            System.out.println("connect successful...ftp服务器:" + this.hostname + ":" + this.port);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<FileTree> getFileList(String path) throws IOException {
        List<FileTree> trees = new ArrayList<>();
        initFtpClient();
        FTPFile[] list = ftpClient.listFiles(path);
        if (null != list) {
            for (FTPFile file : list) {
                FileTree tree = new FileTree();
                tree.setFileName(file.getName());
                String size = sizeShow(file.getSize());
                tree.setSize(size);
                tree.setCount(list.length);
                tree.setTime(file.getTimestamp().getTimeInMillis());
                if (file.isDirectory()) {
                    tree.setSize("-");
                    tree.getTrees().addAll(getFileList(path + "\\" + file.getName()));
                }
                trees.add(tree);
            }
        }
        sortList(trees);
        timeFormat(trees);
        return trees;
    }

    public String sizeShow(Long size) {
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return size / 1024 + "K";
        } else if (size < 1024 * 1024 * 1024) {
            return size / 1024 / 1024 + "M";
        } else if (size > 1024 * 1024 * 1024) {
            return size / 1024 / 1024 + "G";
        } else {
            return "0B";
        }
    }

    public void sortList(List<FileTree> trees) {
        if (trees != null) {
            trees.sort((o1, o2) -> o2.getTime().compareTo(o1.getTime()));
            for (FileTree tree : trees) {
                if (tree.getTrees() != null) {
                    sortList(tree.getTrees());
                }
            }
        }
    }

    public void timeFormat(List<FileTree> trees) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (trees != null) {
            for (FileTree tree : trees) {
                tree.setRelTime(dateFormat.format(tree.getTime()));
                if (tree.getTrees() != null) {
                    timeFormat(tree.getTrees());
                }
            }
        }
    }

    public String removeDirectoryALLFile(String pathName) {
        try {
            initFtpClient();
            FTPFile[] files = ftpClient.listFiles(pathName);
            if (null != files && files.length > 0) {
                for (FTPFile file : files) {
                    if (file.isDirectory()) {
                        removeDirectoryALLFile(pathName + "\\" + file.getName());
                        // 切换到父目录，不然删不掉文件夹
                        ftpClient.changeWorkingDirectory(pathName.substring(0, pathName.lastIndexOf("\\")));
                        ftpClient.removeDirectory(pathName);
                    } else {
                        if (!ftpClient.deleteFile(pathName + "\\" + file.getName())) {
                            return "删除文件" + pathName + "\\" + file.getName() + "失败!";
                        }
                    }
                }
            }
            // 切换到父目录，不然删不掉文件夹
            ftpClient.changeWorkingDirectory(pathName.substring(0, pathName.lastIndexOf("\\")));
            ftpClient.removeDirectory(pathName);
            ftpClient.logout();
        } catch (IOException e) {
            System.out.println("删除文件夹" + pathName + "失败：" + e);
            e.printStackTrace();
            return "删除文件夹" + pathName + "失败：" + e;
        }
        return "true";
    }

    public boolean downFile(String newFileName, String fileName, String downUrl) throws IOException {
        initFtpClient();
        boolean isTrue = false;
        OutputStream os;
        File localFile = new File(downUrl + "\\" + newFileName);
        os = new FileOutputStream(localFile);
        isTrue = ftpClient.retrieveFile(new String(fileName.getBytes(), StandardCharsets.ISO_8859_1), os);
        os.close();
        return isTrue;
    }

    public boolean downloadFile(String remoteFileName, String localDirs, String remotePath) {
        initFtpClient();
        String strFilePath = localDirs + "\\" + remoteFileName;
        BufferedOutputStream outStream = null;
        boolean success = false;
        try {
            ftpClient.changeWorkingDirectory(remotePath);
            outStream = new BufferedOutputStream(new FileOutputStream(strFilePath));
            System.out.println(remoteFileName + "开始下载....");
            success = ftpClient.retrieveFile(remoteFileName, outStream);
            if (success) {
                System.out.println(remoteFileName + "成功下载到" + strFilePath);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(remoteFileName + "下载失败");
        } finally {
            if (null != outStream) {
                try {
                    outStream.flush();
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!success) {
            System.out.println(remoteFileName + "下载失败!!!");
        }
        return success;
    }

    public boolean downLoadDirectory(String localDirPath, String remoteDir) {
        try {
            initFtpClient();
            String fileName = new File(remoteDir).getName();
            localDirPath = localDirPath + "\\" + fileName;
            new File(localDirPath).mkdirs();
            FTPFile[] allFile = this.ftpClient.listFiles(remoteDir);
            for (FTPFile file:allFile) {
                if (!file.isDirectory()) {
                    downloadFile(file.getName(), localDirPath, remoteDir);
                }else {
                    String remoteDirPath = remoteDir + "\\" + file.getName();
                    downLoadDirectory(localDirPath, remoteDirPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("下载文件夹失败");
            return false;
        }
        return true;
    }


    public boolean uploadFile(MultipartFile[] files, String pathname) throws IOException {
        initFtpClient();
        InputStream is;
        try {
            for (MultipartFile file : files) {
                is = file.getInputStream();
                ftpClient.storeFile(pathname, is);
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            ftpClient.logout();
        }
        return true;
    }

    public void addFile(String path) throws IOException {
        initFtpClient();
        ftpClient.makeDirectory(path);
    }

    public void updateFileName(String path,String newName) throws IOException {
        initFtpClient();
        ftpClient.changeWorkingDirectory(path);
        ftpClient.rename(path,newName);
        ftpClient.logout();
        ftpClient.disconnect();
    }


    public static void main(String[] args) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Test t = new Test();
        long start = System.currentTimeMillis();
        List<FileTree> tree = t.getFileList("\\");
        System.out.println(tree);
        for (FileTree l : tree) {
            System.out.println(l);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
//        t.addFile("\\");


//        List<JSONObject> objectList = new ArrayList<>();
//        for(FTPFile f:files){
//            if (f.isDirectory()){
//                size = "-";
//            }else {
//                size = f.getSize() +"B";
//            }
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("name",f.getName());
//            jsonObject.put("time",dateFormat.format(f.getTimestamp().getTime()));
//            jsonObject.put("size",size);
//            objectList.add(jsonObject);
//            System.out.println(jsonObject.toJSONString());
//        }
//        System.out.println(objectList.toString());
//        System.out.println("总数:"+files.length);

//        t.removeDirectoryALLFile("\\1\\3");
//        t.downFile("1.txt","3.txt","d:");
        t.downloadFile("2.txt","d:","\\1");
//        List<String> path = new ArrayList<>();
//        path.add("\\1\\2");
//        path.add("\\1\\2\\3");
//        for (String p:path){
//            t.downLoadDirectory("d:",p);
//        }

//        t.updateFileName("\\3.txt","4.txt");
    }
}
