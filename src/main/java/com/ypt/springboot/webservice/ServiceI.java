package com.ypt.springboot.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface ServiceI {
    @WebMethod
    String HelloWorld(String name);

}
