package com.ksh.nettyapi.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ksh.nettyapi.core.ApiRequestTemplate;
import com.ksh.nettyapi.core.JedisHelper;
import com.ksh.nettyapi.exception.RequestParamException;
import com.ksh.nettyapi.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-03
 * Time : 오전 11:36
 * To change this template use File | Settings | File and Code Templates.
 */
@Service("tokenVerify")
@Scope("prototype")
public class TokenVerify extends ApiRequestTemplate {

    static Logger logger = LoggerFactory.getLogger(TokenExpire.class);
    private static final JedisHelper helper = JedisHelper.getInstance();

    public TokenVerify(Map<String,String> reqData) {
        super(reqData);
    }

    @Override
    public void requestParamValidation() throws RequestParamException {
        if(StringUtils.isEmpty(this.reqData.get("token"))){
            throw new RequestParamException("token이 없습니다.");
        }
    }

    @Override
    public void service() throws ServiceException {
        Jedis jedis = null;
        try{
            jedis = helper.getConnection();
            String  tokenString = jedis.get(this.reqData.get("token"));

            //helper
            if(tokenString == null){
                this.apiResult.addProperty("resultCode","404");
                this.apiResult.addProperty("message","Fail");
            }else{
                Gson gson = new Gson();
                JsonObject token = gson.fromJson(tokenString , JsonObject.class);

                this.apiResult.addProperty("resultCode","200");
                this.apiResult.addProperty("message","Success");
                this.apiResult.addProperty("issueDate",this.reqData.get("issueDate"));
                this.apiResult.addProperty("email",this.reqData.get("email"));
                this.apiResult.addProperty("userNo",this.reqData.get("userNo"));
            }

        }catch (Exception e){
            helper.returnResource(jedis);
        }
    }
}