package com.ksh.nettyapi.core;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-03
 * Time : 오전 11:57
 * To change this template use File | Settings | File and Code Templates.
 */
public interface KeyMaker {
    /**
     * 키 생성기로부터 만들어진 키를 가져온다.
     * @return 만들어진 키
     */
    public String getKey();
}
