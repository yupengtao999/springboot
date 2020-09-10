package com.ypt.springboot.ftpTest;

import java.util.ArrayList;

public class FileTree {
    private String fileName;
    private String size;
    private Long time;
    private Integer count;
    private String relTime;
    private Integer type;//0是文件，1是文件夹

    @Override
    public String toString() {
        return "FileTree{" +
                "fileName='" + fileName + '\'' +
                ", size='" + size + '\'' +
                ", time=" + time +
                ", count=" + count +
                ", relTime='" + relTime + '\'' +
                ", type=" + type +
                ", trees=" + trees +
                '}';
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    private ArrayList<FileTree> trees;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<FileTree> getTrees() {
        if (trees == null){
            trees = new ArrayList<>();
        }
        return trees;
    }

    public void setTrees(ArrayList<FileTree> trees) {
        if (trees==null){
            trees = new ArrayList<>();
        }
        this.trees = trees;
    }
    public FileTree(){

    }

    public FileTree(String fileName, ArrayList<FileTree> trees) {
        this.fileName = fileName;
        this.trees = trees;
    }

}
