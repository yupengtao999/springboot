package com.ypt.springboot.DoorSystem;

public class Test {
    public static void main(String[] args) {
//        Guest guest = new Guest();
//        Admin admin = new Admin("pa","12345");
        Employee employee = new Employee("pa","12345");
        ControlSys controlSys = new ControlSys();
        System.out.println("测试");
//        guest.pressRing(controlSys);
//        admin.workDoor(controlSys,guest);
        employee.InputInfo(controlSys);
        try {
            controlSys.work();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
