package com.ypt.springboot.DoorSystem;

public final class Computer {
    private String input;

    Computer(){
        this.input = null;
    }

    private void inputFormConfirm(String inputString){
        if (inputString.substring(0, 2).equals("pa")){
            this.input = inputString.substring(2);
        }else if (inputString.substring(0, 2).equals("ca")){
            this.input = inputString.substring(2);
        }else if (inputString.substring(0, 2).equals("fi")){
            this.input = inputString.substring(2);
        }else{
            this.input = null;
        }
    }

    public boolean confirm(String inputString){
        inputFormConfirm(inputString);
        if (this.input == null){
            return false;
        }else{
            return true;
        }
    }
}
