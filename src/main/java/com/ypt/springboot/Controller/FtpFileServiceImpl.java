package com.ypt.springboot.Controller;

import org.apache.commons.net.ftp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author: ypt
 * @Date: 2020/10/19 9:39
 */
@Service
public class FtpFileServiceImpl implements FtpFileService {

    //ftp服务器地址
    @Value("${ftp.host}")
    public String hostname;
    //ftp服务器端口号默认为21
    @Value("${ftp.port}")
    public Integer port;
    //ftp登录账号
    @Value("${ftp.name}")
    public String username;
    //ftp登录密码
    @Value("${ftp.password}")
    public String password;

    public FTPClient ftpClient = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(FtpFileServiceImpl.class);

    /**
     * 功能描述: 初始化FTP
     */
    @Override
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
                LOGGER.info("connect failed...ftp服务器:{}:{}", this.hostname, this.port);
            }
            LOGGER.info("connect successful...ftp服务器:{}:{}", this.hostname, this.port);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("连接异常", e);
        }
    }

    /**
     * 功能描述: 获取文件目录
     * @param path
     */
    @Override
    public List<FtpFile> getFileList(String path,String name) {
        List<FtpFile> trees = new ArrayList<>();
        FTPFile[] list = new FTPFile[0];
        try {
            list = ftpClient.listFiles(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != list) {
            for (FTPFile file : list) {
                FtpFile tree = new FtpFile();
                tree.setFileName(file.getName());
                String size = sizeShow(file.getSize());
                tree.setSize(size);
                tree.setCount(list.length);
                tree.setTime(file.getTimestamp().getTimeInMillis());
                tree.setType(file.getType());
                name = "/"+file.getName();
                if (path.equals("/")){
                    path = "";
                }
                tree.setPath(path+name);

                if (file.isDirectory()) {
                    tree.setSize("-");
//                    tree.getTrees().addAll(getFileList(path + "/" + file.getName(),path+name));
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

    public void sortList(List<FtpFile> trees) {
        if (trees != null) {
            trees.sort((o1, o2) -> o2.getTime().compareTo(o1.getTime()));
            for (FtpFile tree : trees) {
                if (tree.getTrees() != null) {
                    sortList(tree.getTrees());
                }
            }
        }
    }

    public void timeFormat(List<FtpFile> trees) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (trees != null) {
            for (FtpFile tree : trees) {
                tree.setRelTime(dateFormat.format(tree.getTime()));
                if (tree.getTrees() != null) {
                    timeFormat(tree.getTrees());
                }
            }
        }
    }

    /**
     * 功能描述: 删除文件
     * @param path
     */
    @Override
    public JsonResult removeFile(String path) {
        try {
            ftpClient.deleteFile(path);
            return new JsonResult("删除文件成功", true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonResult("删除文件失败", false);
        }
    }

    /**
     * 功能描述: 删除文件夹
     * @param pathName
     */
    @Override
    public JsonResult removeDir(String pathName) {
        try {
            FTPFile[] files = ftpClient.listFiles(pathName);
            if (null != files && files.length > 0) {
                for (FTPFile file : files) {
                    if (file.isDirectory()) {
                        removeDir(pathName + "/" + file.getName());
                    } else {
                        if (!ftpClient.deleteFile(pathName + "/" + file.getName())) {
                            return new JsonResult("删除文件" + pathName + "/" + file.getName() + "失败!", false);
                        }
                    }
                }
            }
            // 切换到父目录，不然删不掉文件夹
            ftpClient.changeWorkingDirectory(pathName.substring(0, pathName.lastIndexOf("/")));
            ftpClient.removeDirectory(pathName);
        } catch (IOException e) {
            LOGGER.info("删除文件夹失败", e);
            return new JsonResult("删除文件夹" + pathName + "失败：" + e, false);
        }
        return new JsonResult("删除成功", true);
    }

    /**
     * 功能描述: 下载文件
     * @param remoteFileName
     * @param remotePath
     */
    @Override
    public InputStream downloadFile(String remoteFileName, String remotePath) {
        InputStream is = null;
        try {
            is = ftpClient.retrieveFileStream(remotePath);
        }catch (IOException e){
            e.printStackTrace();
        }
        return is;
    }

    /**
     * 功能描述: 下载文件夹
     * @param localDirPath
     * @param remoteDir
     */
    @Override
    public JsonResult downLoadDirectory(String localDirPath, String remoteDir) {
        try {
            String fileName = new File(remoteDir).getName();
            localDirPath = localDirPath + "/" + fileName;
            new File(localDirPath).mkdirs();
            FTPFile[] allFile = ftpClient.listFiles(remoteDir);
            for (FTPFile file : allFile) {
                if (!file.isDirectory()) {
//                    downloadFile(file.getName(), localDirPath, remoteDir);
                } else {
                    String remoteDirPath = remoteDir + "/" + file.getName();
                    downLoadDirectory(localDirPath, remoteDirPath);
                }
            }
        } catch (IOException e) {
            LOGGER.info("下载文件夹失败", e);
            return new JsonResult("下载文件夹失败", false);
        }
        return new JsonResult("下载成功", true);
    }

    /**
     * 功能描述: 复制文件夹
     * @param oldPath
     * @param newPath
     */
    @Override
    public JsonResult dirCopy(String oldPath, String newPath) {
        try {
            FTPFile[] files = ftpClient.listFiles(newPath);
            if (files.length >= 100) {
                return new JsonResult("文件数量过多", false);
            } else {
                String fileName = oldPath.substring(oldPath.lastIndexOf("/") + 1);
                newPath = newPath + "/" + fileName;
                ftpClient.makeDirectory(newPath);
                FTPFile[] allFile = ftpClient.listFiles(oldPath);
                for (FTPFile file : allFile) {
                    if (!file.isDirectory()) {
                        fileCopy(file.getName(), oldPath+"/"+file.getName(), newPath);
                    } else {
                        dirCopy(oldPath + "/" + file.getName(), newPath);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.info("复制文件失败", e);
            return new JsonResult("复制文件失败", false);
        }
        return new JsonResult("复制文件成功", true);
    }

    /**
     * 功能描述: 复制文件
     * @param fileName
     * @param path
     * @param newPath
     */
    @Override
    public JsonResult fileCopy(String fileName, String path, String newPath) {
        try {
            FTPFile[] files = ftpClient.listFiles(path);
            if (files.length >= 100) {
                return new JsonResult("文件数量过多", false);
            } else {
                ByteArrayOutputStream fos = new ByteArrayOutputStream();
                ftpClient.retrieveFile(path, fos);
                ByteArrayInputStream in = new ByteArrayInputStream(fos.toByteArray());
                ftpClient.storeFile(newPath + "/" + fileName, in);
                fos.close();
                in.close();
                return new JsonResult("复制文件成功", true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonResult("复制文件失败", false);
        }
    }

    /**
     * 功能描述: 上传文件
     * @param files
     * @param pathname
     * @return
     */
    @Override
    public JsonResult uploadFile(MultipartFile[] files, String pathname) {
        InputStream is;
        try {
            FTPFile[] ftpFiles = ftpClient.listFiles(pathname);
            if (ftpFiles.length + files.length >= 100) {
                return new JsonResult("文件数已满", false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonResult("上传失败", false);
        }

        for (MultipartFile file : files) {
            try {
                if (file.isEmpty()) {
                    return new JsonResult("上传文件为空", false);
                }
                is = file.getInputStream();
                String fileName = file.getOriginalFilename();
                ftpClient.storeFile(pathname + "/" + fileName, is);
                is.close();
            } catch (Exception e) {
                LOGGER.error("error", e);
                return new JsonResult("文件" + file.getOriginalFilename() + "上传失败", false);
            }
        }
        return new JsonResult("上传成功", true);
    }

    /**
     * 功能描述: 新建文件夹
     * @param path
     * @param fileName
     * @return
     */
    @Override
    public JsonResult newFolder(String path, String fileName) {
        try {
            FTPFile[] files = ftpClient.listFiles(path);
            if (files.length >= 100) {
                return new JsonResult("文件数已满", false);
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
            while (true) {
                if (!names.contains(name)) {
                    ftpClient.makeDirectory(path + "/" + name);
                    return new JsonResult("创建成功", true);
                } else if (names.contains(name + " (" + num + ")")) {
                    num++;
                } else {
                    name = name + " (" + num + ")";
                    ftpClient.makeDirectory(path + "/" + name);
                    return new JsonResult("创建成功", true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonResult("创建失败", false);
        }
    }

    /**
     * 功能描述: 修改文件名
     * @param path
     * @param oldName
     * @param newName
     */
    @Override
    public void updateFileName(String path, String oldName, String newName) throws IOException {
        ftpClient.changeWorkingDirectory(path);
        ftpClient.rename(oldName, newName);
    }

    /**
     * 功能描述: 移动文件或文件夹
     * @param file
     */
    @Override
    public void moveFile(FtpFile file) {
        if (file.getType() == 1){
            if (dirCopy(file.getPath(), file.getNewPath()).isSuccess()) {
                removeDir(file.getPath());
            }
        }else {
            if (fileCopy(file.getFileName(), file.getPath(),file.getNewPath()).isSuccess()) {
                removeFile(file.getPath());
            }
        }
    }

    /**
     * 功能描述: 断开连接
     */
    @Override
    public void disconnect() {
        try {
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
//        FtpFileServiceImpl service = new FtpFileServiceImpl();
//        service.initFtpClient();
//        List<FtpFile> list = service.getFileList("/",null);
//        System.out.println(list);
//        service.disconnect();
//        JsonResult result = service.dirCopy("/1/2", "/1/111");
//        service.newFolder("/1","  ");
//        service.disconnect();
        String name = "/df/df";
        System.out.println(name.substring(0,name.lastIndexOf("/")));
    }

    /**
     * 功能描述:
     * @param srcDir 源文件路径
     * @param out
     * @param KeepDirStructure
     * @return: void
     */
    @Override
    public void toZip(FtpFile srcDir, OutputStream out, boolean KeepDirStructure)
            throws RuntimeException{

        long start = System.currentTimeMillis();
        ZipOutputStream zos = null ;
        try {
            zos = new ZipOutputStream(out);
            FTPFile[] ftpFiles = ftpClient.listFiles(srcDir.getPath().substring(0,srcDir.getPath().lastIndexOf("/")));

            for (FTPFile file:ftpFiles){
                if (file.getName().equals(srcDir.getFileName())){
                    compress(file,zos,srcDir.getFileName(),KeepDirStructure,srcDir.getPath());
                }
            }
//            compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) +" ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils",e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    private final static int BUFFER_SIZE = 2*1024;
    private void compress(FTPFile sourceFile, ZipOutputStream zos, String name,
                                 boolean KeepDirStructure,String path) throws Exception{
        byte[] buf = new byte[BUFFER_SIZE];
        if(sourceFile.isFile()){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
//            FileInputStream in = new FileInputStream(sourceFile);
            ftpClient.changeWorkingDirectory(path);
            InputStream in = ftpClient.getInputStream();
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            if(ftpClient.list(path) == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(KeepDirStructure){
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }

            }else {
                FTPFile[] files = ftpClient.listFiles(path);
                for (FTPFile file :files ) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure,path+"/"+file.getName());
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure,path);
                    }

                }
            }
        }
    }
}
