package com.ypt.springboot.Controller;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 功能描述: Ftp文件实体
 * @Author: ypt
 * @Date: 2020/10/19 9:29
 */
public class FtpFile implements Serializable {
    private static final long serialVersionUID = 2880050038498701724L;
    private String fileName;//文件名
    private String size;
    private Long time;
    private Integer count;//当前目录文件数量
    private String relTime;
    private Integer type;//0是文件，1是文件夹
    private String path;//文件路径
    private String newPath;
    private String newName;
    private ArrayList<FtpFile> trees;

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    @Override
    public String toString() {
        return "FtpFile{" +
                "fileName='" + fileName + '\'' +
                ", size='" + size + '\'' +
                ", time=" + time +
                ", count=" + count +
                ", relTime='" + relTime + '\'' +
                ", type=" + type +
                ", path='" + path + '\'' +
                ", newPath='" + newPath + '\'' +
                ", newName='" + newName + '\'' +
                ", trees=" + trees +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getRelTime() {
        return relTime;
    }

    public void setRelTime(String relTime) {
        this.relTime = relTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ArrayList<FtpFile> getTrees() {
        if (trees == null){
            trees = new ArrayList<>();
        }
        return trees;
    }

    public void setTrees(ArrayList<FtpFile> trees) {
        if (trees==null){
            trees = new ArrayList<>();
        }
        this.trees = trees;
    }
    public FtpFile(){

    }

}
