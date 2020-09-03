package com.ypt.springboot.Config;

import java.util.concurrent.*;

public class ExecutorTest implements Callable,ThreadFactory {
    @Override
    public Object call() throws Exception {
        String a = "123";
        return a;
    }

    public static void main(String[] args) {
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(5, 10, 100,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));
        FutureTask<String> futureTask = new FutureTask<String>(new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    String a = "return String";
                    return a;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "exception";
                }

            }
        });
//        Future future = tpe.submit(new ExecutorTest() {
//        });
        futureTask.run();
        Thread thread = new ExecutorTest().newThread(new Runnable() {
            @Override
            public void run() {

            }
        });
        try {
            System.out.println(futureTask.get());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tpe.shutdown();
        }
    }

    @Override
    public Thread newThread(Runnable r) {
        return null;
    }
}
