package com.ypt.springboot.bean;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;


public class DemoQuartz {
    public static void main(String[] args) throws SchedulerException {
        //1.创建scheduler的工厂
        SchedulerFactory sf = new StdSchedulerFactory();
        //2.从工厂获取调度器实例
        Scheduler scheduler = sf.getScheduler();
        //3.创建JobDetail(作业信息)
        JobDetail jb = JobBuilder.newJob(MyJob.class)
                .withDescription("this is a test job") //job的描述
                .withIdentity("testJob", "testGroup") //job 的name和group
                .build();

        //向任务传递数据
        JobDataMap jobDataMap = jb.getJobDataMap();
        jobDataMap.put("uname", "张三");

        //任务运行的时间，SimpleSchedle类型触发器有效
        long time = System.currentTimeMillis() + 3 * 1000L; //3秒后启动任务
        Date statTime = new Date(time);

        //4.创建Trigger
        //使用SimpleScheduleBuilder或者CronScheduleBuilder
        Trigger t = TriggerBuilder.newTrigger()
                .withDescription("")
                .withIdentity("ramTrigger", "ramTriggerGroup")
                .startAt(statTime)  //默认当前时间启动
                //普通计时器
                //.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).withRepeatCount(3))//间隔3秒，重复3次
                //表达式计时器
                .withSchedule(CronScheduleBuilder.cronSchedule("0/3 * * * * ?")) //3秒执行一次
                .build();

        //5.注册任务和定时器
        scheduler.scheduleJob(jb, t);

        //6.启动 调度器
        scheduler.start();

    }

}
