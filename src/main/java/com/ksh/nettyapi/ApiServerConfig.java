package com.ksh.nettyapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.net.InetSocketAddress;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-02
 * Time : 오전 11:15
 * To change this template use File | Settings | File and Code Templates.
 */
@Configuration
@ImportResource("classpath:spring/hsqlApplicationContext.xml")
@ComponentScan("com.ksh.nettyapi.core,com.ksh.nettyapi,com.ksh.nettyapi.service")
@PropertySource("classpath:api-server.properties")
public class ApiServerConfig {
    @Value("${boss.thread.count}")
    private int bossThreadCount;

    @Value("${worker.thread.count}")
    private int workerThreadCount;

    @Value("${tcp.port}")
    private int tcpPort;

    @Bean(name="bossThreadCount")
    public int getBossThreadCount(){
        return bossThreadCount;
    }

    @Bean(name="workerThreadCount")
    public int getWorkerThreadCount(){
        return workerThreadCount;
    }

    @Bean(name="tcpPort")
    public int getTcpPort(){
        return tcpPort;
    }

    @Bean(name="tcpSocketAddress")
    public InetSocketAddress tcpPort(){
        return new InetSocketAddress(tcpPort);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }
}
