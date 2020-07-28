package com.ypt.springboot.DoorSystem;

public class Employee extends Guest {
    private String info;
    protected String password;
    public Employee(String info,String password){
        this.info = info;
        this.password = password;
    }
    public void InputInfo(ControlSys controlSys){
        controlSys.employeeSetInfo(this.info,this.password);
    }
}
