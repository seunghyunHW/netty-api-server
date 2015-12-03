package com.ksh.nettyapi.core;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-02
 * Time : 오후 5:57
 * To change this template use File | Settings | File and Code Templates.
 */
@Component
public class ServiceDispatcher {
    private static ApplicationContext springContext;

    @Autowired
    public void init(ApplicationContext springContext){
        ServiceDispatcher.springContext = springContext;
    }

    static org.slf4j.Logger logger = LoggerFactory.getLogger(ServiceDispatcher.class);

    public static ApiRequest dispatch(Map<String, String> requestMap){
        String serviceUri = requestMap.get("REQUEST_URI");
        String beanName = null;

        if(serviceUri == null){
            beanName = "notFound";
        }
        if(serviceUri.startsWith("/tokens")){
            String httpMethod = requestMap.get("REQUEST_METHOD");

            switch(httpMethod) {
                case "POST":
                    beanName = "tokenIssue";
                    break;
                case "DELETE":
                    beanName = "tokenExpired";
                    break;
                case "GET":
                    beanName = "tokenVerify";
                    break;
                default:
                    beanName = "notFound";
                    break;
            }
        }else if(serviceUri.startsWith("/users")){
            beanName = "users";
        }else{
            beanName = "notFound";
        }

        ApiRequest service = null;
        try{
            service = (ApiRequest)springContext.getBean(beanName,requestMap);
        }catch (Exception e){
            e.printStackTrace();
            service = (ApiRequest)springContext.getBean("notFound",requestMap);
        }

        return service;
    }
}
