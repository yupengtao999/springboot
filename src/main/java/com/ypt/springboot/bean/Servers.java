package com.ypt.springboot.bean;

import java.util.ArrayList;
import java.util.List;

public class Servers {
    public List<String> list = new ArrayList<String>(){
        {
            add("192.168.1.1");
            add("192.168.1.2");
            add("192.168.1.3");
        }
    };
}
