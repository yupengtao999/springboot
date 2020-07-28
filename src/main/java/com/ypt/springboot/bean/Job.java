package com.ypt.springboot.bean;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Job {
    public void execute() {
        System.out.println("任务调度1" + (new Date().toString()));
    }

    public void work() {
        System.out.println("任务调度2" + (new Date().toString()));
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Test start");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:quartz.xml");
        System.out.println("Test end");
        Set<String> set = new HashSet<>();
        set.add("111");
        set.add("222");
        for (String a:set){
            System.out.println(a);
        }
    }
}
