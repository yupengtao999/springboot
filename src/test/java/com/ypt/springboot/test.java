package com.ypt.springboot;

import com.ypt.springboot.bean.FullRandom;
import org.junit.Assert;
import org.junit.Test;

public class test {
    @Test
    public void a(){
        Assert.assertEquals(2,new FullRandom().add(1,1));
    }
}
