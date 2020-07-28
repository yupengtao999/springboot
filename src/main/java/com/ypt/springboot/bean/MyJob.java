package com.ypt.springboot.bean;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

public class MyJob extends QuartzJobBean {
    private static int count = 1;
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        System.out.println("定时器任务执行"+new Date(System.currentTimeMillis()));
//        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
//        System.out.println(map.get("uname"));
//    }

    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        System.out.println(new Date(System.currentTimeMillis())+"Myjob开始执行了。。。。"+arg0.getTrigger().getKey().getName());
        ApplicationContext applicationContext=(ApplicationContext)arg0.getJobDetail().getJobDataMap()
                .get("applicationContext");
        System.out.println(new Date(System.currentTimeMillis())+"获取到的容器是："+(count++)+"|"+applicationContext);
    }

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:application.xml");
    }
}
