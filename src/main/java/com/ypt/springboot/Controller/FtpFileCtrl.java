package com.ypt.springboot.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 功能描述: 通过Ftp方式操作文件
 * @Author: ypt
 * @Date: 2020/10/19 9:28
 */
@Controller
@Scope("prototype")
@RequestMapping(value = "/fileFunc",method = RequestMethod.POST)
public class FtpFileCtrl {

    final
    FtpFileService ftpService;

    public FtpFileCtrl(FtpFileService ftpService) {
        this.ftpService = ftpService;
    }

    /**
     * 功能描述: 获取文件目录
     * @param file
     * @return: 文件目录
     * @Date: 2020/10/19 10:31
     */
    @RequestMapping(value = "/getFileList",method = RequestMethod.POST)
    public @ResponseBody JsonResult getFileList(@RequestBody FtpFile file){
        try {
            ftpService.initFtpClient();
            if (null == file.getPath()) {
                file.setPath("/");
            }
            List<FtpFile> result = ftpService.getFileList(file.getPath(),null);
            if (result.size() == 0){
                return new JsonResult(result);
            }
            return new JsonResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            ftpService.disconnect();
        }
        return new JsonResult("请求失败！",false);
    }

    /**
     * 功能描述: 删除文件及文件夹
     * @param files
     * @Date: 2020/10/19 11:15
     */
    @RequestMapping(value = "/removeDir",method = RequestMethod.POST)
    public @ResponseBody JsonResult removeDir(@RequestBody List<FtpFile> files) {
        try {
            ftpService.initFtpClient();
            for (FtpFile file:files){
                if (file.getType() == 1){
                    ftpService.removeDir(file.getPath());
                }else {
                    ftpService.removeFile(file.getPath());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult("删除失败！",false);
        }finally {
            ftpService.disconnect();
        }
        return new JsonResult("删除成功！",true);
    }

    /**
     * 功能描述: 下载文件及文件夹
     * @param files
     * @Date: 2020/10/19 11:16
     */
    @RequestMapping(value = "/downloadFile",method = RequestMethod.POST)
    public @ResponseBody void downloadFile(@RequestBody List<FtpFile> files, HttpServletResponse response) {
        try {
            ftpService.initFtpClient();
            for (FtpFile file:files){
                InputStream is  = ftpService.downloadFile(file.getFileName(),file.getPath());
                OutputStream out = null;
                String fileName = URLEncoder.encode(file.getFileName(), "UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/x-download");
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
                out = response.getOutputStream();
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                if (null != out){
                    out.close();
                }else if (null != is){
                    is.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ftpService.disconnect();
        }
    }

    /**
     * 功能描述: 文件及文件夹复制
     * @param files
     */
    @RequestMapping(value = "/fileCopy",method = RequestMethod.POST)
    public @ResponseBody JsonResult fileCopy(@RequestBody List<FtpFile> files) {
        try {
            ftpService.initFtpClient();
            for (FtpFile file:files){
                if (file.getType() == 0){
                    ftpService.fileCopy(file.getFileName(),file.getPath(),file.getNewPath());
                }else {
                    ftpService.dirCopy(file.getPath(),file.getNewPath());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult("复制失败！",false);
        }finally {
            ftpService.disconnect();
        }
        return new JsonResult("复制成功！",true);
    }

    /**
     * 功能描述: 上传文件
     * @param files
     * @param path
     */
    @RequestMapping(value = "/uploadFile",method = RequestMethod.POST)
    @Scope("prototype")
    public @ResponseBody synchronized JsonResult uploadFile(@RequestParam("files") MultipartFile[] files,
                                               @RequestParam("uploadPath") String path) {
        ftpService.initFtpClient();
        JsonResult result = ftpService.uploadFile(files,path);
        ftpService.disconnect();
        return new JsonResult(result.getInfo(),result.isSuccess());
    }

    /**
     * 功能描述: 新建文件夹
     */
    @RequestMapping(value = "/newFolder",method = RequestMethod.POST)
    public @ResponseBody JsonResult newFolder(@RequestBody FtpFile file) {
        ftpService.initFtpClient();
        JsonResult result = ftpService.newFolder(file.getNewPath(),file.getNewName());
        ftpService.disconnect();
        return new JsonResult(result.getInfo(),result.isSuccess());
    }

    /**
     * 功能描述: 修改文件名
     * @param file
     */
    @RequestMapping(value = "/updateFileName",method = RequestMethod.POST)
    public @ResponseBody JsonResult updateFileName(@RequestBody FtpFile file){
        ftpService.initFtpClient();
        try {
            ftpService.updateFileName(file.getPath(),file.getFileName(),file.getNewName());
            return new JsonResult("修改成功",true);
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonResult(e.getMessage(),false);
        }finally {
            ftpService.disconnect();
        }
    }

    /**
     * 功能描述: 移动文件
     */
    @RequestMapping(value = "/moveFile",method = RequestMethod.POST)
    public @ResponseBody JsonResult moveFile(@RequestBody List<FtpFile> files){
        try {
            ftpService.initFtpClient();
            for (FtpFile file:files){
                ftpService.moveFile(file);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult("移动文件失败",false);
        }
        return new JsonResult("移动文件成功",true);
    }

}