package com.ypt.springboot.DoorSystem;

public class Admin extends Employee {
    public Admin(String info, String password) {
        super(info, password);
    }
    public void setEmployeePassword(String newPassword){
        super.password = newPassword;
    }

    public void workDoor(ControlSys controlSys, Guest guest){
        if (controlSys.getRing()){
            controlSys.setOpenSign();
            controlSys.guestSetRing();
        }
    }
}
