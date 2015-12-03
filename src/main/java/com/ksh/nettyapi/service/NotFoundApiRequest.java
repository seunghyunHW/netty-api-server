package com.ksh.nettyapi.service;

import com.ksh.nettyapi.core.ApiRequestTemplate;
import com.ksh.nettyapi.exception.RequestParamException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-03
 * Time : 오후 3:12
 * To change this template use File | Settings | File and Code Templates.
 */
@Service("notFound")
@Scope("prototype")
public class NotFoundApiRequest extends ApiRequestTemplate {

    public NotFoundApiRequest(Map<String, String> reqData) {
        super(reqData);
    }

    @Override
    public void requestParamValidation() throws RequestParamException {

    }

    @Override
    public void service() {
        this.apiResult.addProperty("resultCode", "404");
    }
}