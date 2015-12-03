package com.ksh.nettyapi.service;

import com.ksh.nettyapi.core.ApiRequestTemplate;
import com.ksh.nettyapi.exception.RequestParamException;
import com.ksh.nettyapi.exception.ServiceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-02
 * Time : 오후 6:54
 * To change this template use File | Settings | File and Code Templates.
 */
@Service("users")
@Scope("prototype")
public class UserInfo extends ApiRequestTemplate {
    @Autowired
    private SqlSession sqlSession;

    public UserInfo(Map<String,String> reqData){
        super(reqData);
    }

    @Override
    public void requestParamValidation() throws RequestParamException {
        if(StringUtils.isEmpty(this.reqData.get("email"))){
            throw new RequestParamException("email이 없습니다.");
        }
    }

    @Override
    public void service() throws ServiceException {
        Map<String,Object> result = sqlSession.selectOne("users.userInfoByEmail" , this.reqData);

        if(result != null){
            String userNo = String.valueOf(result.get("USERNO"));

            this.apiResult.addProperty("resultCode" , "200");
            this.apiResult.addProperty("message" , "Success");
            this.apiResult.addProperty("userNo" , userNo);
        }else{
            this.apiResult.addProperty("resultCode" , "404");
            this.apiResult.addProperty("message" , "Fail");
        }
    }
}
