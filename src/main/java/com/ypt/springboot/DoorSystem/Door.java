package com.ypt.springboot.DoorSystem;

public final class Door {
    public boolean ifOpenDoor(boolean confirm){
        if (confirm) {
            return true;
        }else {
            return false;
        }
    }
//    public static String OPEN = "门已开启";
//    public static String CLOSE = "门已关闭";
//    private String state = CLOSE;
//    private boolean bellState = false;
//
//    public Door(){
//        this.setState(CLOSE);
//    }
//    public void OPEN(){
//        this.setState(OPEN);
//    }
//    public void CLOSE(){
//        this.setState(CLOSE);
//    }
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }
//
//    public boolean isBellState() {
//        return bellState;
//    }
//
//    public void setBellState(boolean bellState) {
//        this.bellState = bellState;
//    }
}
