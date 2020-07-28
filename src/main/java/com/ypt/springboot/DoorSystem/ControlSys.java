package com.ypt.springboot.DoorSystem;

public class ControlSys {
    private Door door;
    private Computer computer;
    private InputEquip inputEquip;
    private boolean ring;
    private boolean openSign;
    public ControlSys(){
        this.ring = false;
        this.openSign = false;
        door = new Door();
        computer = new Computer();
        inputEquip = new InputEquip();
    }
    public void guestSetRing(){
        if(this.ring == false){
            this.ring = true;
        }else {
            this.ring = false;
        }
    }
    public boolean getRing(){
        return this.ring;
    }
    public boolean setOpenSign(){
        return this.openSign = true;
    }
    public void employeeSetInfo(String inputString, String password){
        inputEquip.inputString(inputString, password);
        String string = inputEquip.outputString();
        boolean conf = computer.confirm(string);
        this.openSign = door.ifOpenDoor(conf);
    }

    public void work() throws InterruptedException{
        if(this.openSign == true){
            System.out.println("Door Open.");
            java.lang.Thread.sleep(5000);
            System.out.println("Door Close.");
            this.openSign = false;
        }else{
            System.out.println("Identity Verificate Failure.");
        }
    }
}
