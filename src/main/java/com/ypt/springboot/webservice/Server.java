package com.ypt.springboot.webservice;

import javax.jws.WebService;

@WebService
public class Server implements ServiceI {

    @Override
    public String HelloWorld(String name) {
        name = "hello"+name;
        return name;
    }
}
