package com.ypt.springboot.Controller;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 功能描述: Ftp文件接口类
 *
 * @Author: ypt
 * @Date: 2020/10/19 9:27
 */
public interface FtpFileService {
    void initFtpClient();

    List<FtpFile> getFileList(String path, String name);

    com.ypt.springboot.Controller.JsonResult removeFile(String path);

    com.ypt.springboot.Controller.JsonResult removeDir(String pathName);

    InputStream downloadFile(String remoteFileName, String remotePath);

    com.ypt.springboot.Controller.JsonResult downLoadDirectory(String localDirPath, String remoteDir);

    com.ypt.springboot.Controller.JsonResult dirCopy(String oldPath, String newPath);

    com.ypt.springboot.Controller.JsonResult fileCopy(String fileName, String path, String newPath);

    com.ypt.springboot.Controller.JsonResult uploadFile(MultipartFile[] files, String pathname);

    JsonResult newFolder(String path, String fileName);

    void updateFileName(String path, String oldName, String newName) throws IOException;

    void moveFile(FtpFile file);

    void disconnect();

    void toZip(FtpFile srcDir, OutputStream out, boolean KeepDirStructure);
}
