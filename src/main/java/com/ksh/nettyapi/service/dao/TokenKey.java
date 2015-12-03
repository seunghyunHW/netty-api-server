package com.ksh.nettyapi.service.dao;

import com.ksh.nettyapi.core.KeyMaker;
import redis.clients.util.MurmurHash;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-03
 * Time : 오후 12:10
 * To change this template use File | Settings | File and Code Templates.
 */
public class TokenKey implements KeyMaker {
    static final int SEED_MURMURHASH = 0x1234ABCD;

    private String email;
    private long issueDate;


    public TokenKey(String email, long issueDate){
        this.email = email;
        this.issueDate = issueDate;
    }


    /**
     * 키 생성기로부터 만들어진 키를 가져온다.
     *
     * @return 만들어진 키
     */
    @Override
    public String getKey() {
        String source = email + String.valueOf(issueDate);

        return Long.toString(MurmurHash.hash64A(source.getBytes(), SEED_MURMURHASH), 16);
    }
}
