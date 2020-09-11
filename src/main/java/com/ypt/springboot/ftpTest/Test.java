package com.ypt.springboot.ftpTest;

import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Test {
    //ftp服务器地址
    public String hostname = "172.16.6.44";
    //ftp服务器端口号默认为21
    public Integer port = 21;
    //ftp登录账号
    public String username = "test";
    //ftp登录密码
    public String password = "123456";

    public FTPClient ftpClient = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(Test.class);

    /**
     * 初始化ftp服务器
     */
    public void initFtpClient() {
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding("GBK");
        FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
        conf.setServerLanguageCode("zh");

        try {
            ftpClient.connect(hostname, port); //连接ftp服务器
            ftpClient.login(username, password); //登录ftp服务器
            int replyCode = ftpClient.getReplyCode(); //是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                LOGGER.info("connect failed...ftp服务器:{} :{}", this.hostname,this.port);
            }
            LOGGER.info("connect successful...ftp服务器:{} :{}", this.hostname,this.port);
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
                tree.setType(file.getType());
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
        } catch (IOException e) {
            LOGGER.info("删除文件夹失败",e);
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
        isTrue = ftpClient.retrieveFile(new
                String(fileName.getBytes(), StandardCharsets.ISO_8859_1), os);
        os.close();
        return isTrue;
    }

    public boolean downloadFile(String remoteFileName, String localDirs, String remotePath) throws IOException {
        initFtpClient();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
        String strFilePath = localDirs + "\\" + remoteFileName;
        BufferedOutputStream outStream = null;
        boolean success = false;
        try {
            ftpClient.changeWorkingDirectory(remotePath);
            outStream = new BufferedOutputStream(new FileOutputStream(strFilePath));
            success = ftpClient.retrieveFile(remoteFileName, outStream);
        } catch (Exception e) {
            e.printStackTrace();
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
        return success;
    }

    public boolean downLoadDirectory(String localDirPath, String remoteDir) {
        try {
            initFtpClient();
            String fileName = new File(remoteDir).getName();
            localDirPath = localDirPath + "\\" + fileName;
            new File(localDirPath).mkdirs();
            FTPFile[] allFile = ftpClient.listFiles(remoteDir);
            for (FTPFile file : allFile) {
                if (!file.isDirectory()) {
                    downloadFile(file.getName(), localDirPath, remoteDir);
                } else {
                    String remoteDirPath = remoteDir + "\\" + file.getName();
                    downLoadDirectory(localDirPath, remoteDirPath);
                }
            }
        } catch (IOException e) {
            LOGGER.info("下载文件夹失败",e);
            return false;
        }
        return true;
    }

    public boolean dirCopy(String oldPath, String newPath) {
        try {
            initFtpClient();
            FTPFile[] files = ftpClient.listFiles(newPath);
            if (files.length >= 100) {
                return false;
            } else {
                String fileName = oldPath.substring(oldPath.lastIndexOf("\\") + 1);
                newPath = newPath + "\\" + fileName;
                ftpClient.makeDirectory(newPath);
                FTPFile[] allFile = ftpClient.listFiles(oldPath);
                for (FTPFile file : allFile) {
                    if (!file.isDirectory()) {
                        fileCopy(file.getName(), oldPath, newPath);
                    } else {
                        dirCopy(oldPath + "\\" + file.getName(), newPath);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.info("复制文件夹失败",e);
            return false;
        }
        return true;
    }

    public boolean fileCopy(String fileName, String path, String newPath) throws IOException {
        FTPFile[] files = ftpClient.listFiles(path);
        if (files.length >= 100) {
            return false;
        } else {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ByteArrayOutputStream fos = new ByteArrayOutputStream();
            ftpClient.retrieveFile(path + "\\" + fileName, fos);
            ByteArrayInputStream in = new ByteArrayInputStream(fos.toByteArray());
            ftpClient.storeFile(newPath + "\\" + fileName, in);
            fos.close();
            in.close();
            return true;
        }
    }


    public boolean uploadFile(MultipartFile[] files, String pathname) throws IOException {
        InputStream is;
        FTPFile[] ftpFiles = ftpClient.listFiles(pathname);
        if (ftpFiles.length + files.length >= 100) {
            return false;
        }
        try {
            for (MultipartFile file : files) {
                is = file.getInputStream();
                String fileName = file.getOriginalFilename();
                ftpClient.storeFile(pathname+"\\"+fileName, is);
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            ftpClient.logout();
            ftpClient.disconnect();
        }
        return true;
    }

    public boolean addDir(String path, String fileName) throws IOException {
        initFtpClient();
        FTPFile[] files = ftpClient.listFiles(path);
        if (files.length >= 100) {
            return false;
        }
        String name;
        int num = 1;
        if (fileName.isEmpty() || fileName.trim().equals("")) {
            name = "新建文件夹";
        } else {
            name = fileName;
        }
        List<String> names = new ArrayList<>();
        for (FTPFile file : files) {
            names.add(file.getName());
        }
        try {
            while (true) {
                if (!names.contains(name)) {
                    ftpClient.makeDirectory(path + "\\" + name);
                    return true;
                } else if (names.contains(name + " (" + num + ")")) {
                    num++;
                } else {
                    name = name + " (" + num + ")";
                    ftpClient.makeDirectory(path + "\\" + name);
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void updateFileName(String path, String oldName, String newName) throws IOException {
        initFtpClient();
        ftpClient.changeWorkingDirectory(path);
        ftpClient.rename(oldName, newName);
        ftpClient.logout();
        ftpClient.disconnect();
    }

    public void moveFile(String oldPath, String newPath) throws IOException {
        if (dirCopy(oldPath, newPath)) {
            removeDirectoryALLFile(oldPath);
        }
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

//        t.removeDirectoryALLFile("\\1\\2");
//        t.downFile("1.txt","3.txt","d:");
//        t.downloadFile("2.txt", "d:", "\\1");
//        List<String> path = new ArrayList<>();
//        path.add("\\1\\2");
//        path.add("\\1\\2\\3");
//        for (String p:path){
//            t.downLoadDirectory("d:","\\1\\2");
//        }
//t.fileCopy("2.txt","\\1","\\1\\111");
//        t.updateFileName("\\1","2.txt","4.txt");
//        t.dirCopy("\\1\\2", "\\1\\111");
//        t.moveFile("\\1\\2", "\\1\\111");
        t.addDir("\\1", "   ");
    }
}
