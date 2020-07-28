package com.ypt.springboot.DoorSystem;

public final class InputEquip {
    private String input;

    public InputEquip() {
        this.input = null;
    }
    public void inputString(String inputString, String password){
        this.input = inputString + password;
    }
    public String outputString(){
        return this.input;
    }
}
