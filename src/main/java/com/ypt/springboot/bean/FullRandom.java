package com.ypt.springboot.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FullRandom {
    static Servers servers = new Servers();
    static Random random = new Random();
    static int index;

    public int add(int a ,int b){
        return a+b;
    }
    public static String go(){
//        List<String> ipList = new ArrayList<>();
//        for (Map.Entry<String,Integer> map : servers.list.entrySet()){
//            for (int i = 0;i<map.getValue();i++){
//                ipList.add(map.getKey());
//            }
//        }
//        int allWeight = servers.list.values().stream().mapToInt(a -> a).sum();
//        int number = random.nextInt(allWeight);
//        return ipList.get(number);
        if (index == servers.list.size()){
            index = 0;
        }
        return servers.list.get(index++);
    }

    public static void main(String[] args) {
        for (int i = 0;i< 15 ;i++){
            System.out.println(go());
        }
    }
}
