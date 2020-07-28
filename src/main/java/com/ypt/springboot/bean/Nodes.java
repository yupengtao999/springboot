package com.ypt.springboot.bean;

import java.util.List;

public class Nodes {
    private String id;
    //父节点
    private String pid;
    private String name;
    List<Nodes> childNodes;

    Nodes(String id, String pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Nodes> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<Nodes> childNodes) {
        this.childNodes = childNodes;
    }

    @Override
    public String toString() {
        return "Nodes{" +
                "id='" + id + '\'' +
                ", pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", childNodes=" + childNodes +
                '}';
    }
}
