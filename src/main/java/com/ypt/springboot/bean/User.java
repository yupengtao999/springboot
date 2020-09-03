package com.ypt.springboot.bean;

import java.util.*;

public class User {
    private Integer id;
    private String username;
    private Date birthday;
    private String sex;
    private String address;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", birthday=" + birthday +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public User(Integer id, String username, Date birthday, String sex, String address) {
        this.id = id;
        this.username = username;
        this.birthday = birthday;
        this.sex = sex;
        this.address = address;
    }

    public User() {
    }
    public User(Integer id,String username){
        this.id = id;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static void main(String[] args) {
//        int a[] = {2,3,1,1,4};
//        System.out.println(CanOut1(a));
        Map<String, String> map = new HashMap<String, String>();
        map.put("c", "ccccc");
        map.put("a", "aaaaa");
        map.put("b", "bbbbb");
        map.put("d", "ddddd");

        List<Map.Entry<String, String>> list = new ArrayList<>(map.entrySet());
        //升序排序
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));

        for (Map.Entry<String, String> mapping : list) {
            System.out.println(mapping.getKey() + ":" + mapping.getValue());
        }
    }

    public static int CanOut1(int[] a) {

        if (a.length == 1) return 0;
        int l = 0, r = 0, step = 0;

        while (l <= r) {

            int max_r = 0;
            for (int i = l; i <= r; i++) {
                max_r = Math.max(max_r, i + a[i]);
            }
            l = r + 1;
            r = max_r;
            step++;
            if (r >= a.length - 1) break;
        }
        return step;
    }
}