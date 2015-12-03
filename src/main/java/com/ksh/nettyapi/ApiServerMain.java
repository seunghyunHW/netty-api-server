package com.ksh.nettyapi;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-02
 * Time : 오전 11:13
 * To change this template use File | Settings | File and Code Templates.
 */
public class ApiServerMain{
    public static void main(String[] args) {
        AbstractApplicationContext springContext = null;
        try{
            springContext = new AnnotationConfigApplicationContext(ApiServerConfig.class);
            springContext.registerShutdownHook();

            ApiServer server = springContext.getBean(ApiServer.class);
            server.start();
        }finally {
            springContext.close();
        }
    }
}
