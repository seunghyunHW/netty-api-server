package com.ksh.nettyapi.core;

import com.google.gson.JsonObject;
import com.ksh.nettyapi.exception.RequestParamException;
import com.ksh.nettyapi.exception.ServiceException;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-02
 * Time : 오후 5:45
 * To change this template use File | Settings | File and Code Templates.
 */
public interface ApiRequest {
    //API를 호출하는 HTTP 요청의 파라미터 값이 입력되었는지 검증하는 메서드
    public void requestParamValidation() throws RequestParamException;
    //각 서비스에 따른 개별 구별 구현 메서드
    public void service() throws ServiceException;
    //서비스 API의 호출 시작 메서드
    public void executeService();
    //API 서비스의 처리 결과를 조회하는 메서드
    public JsonObject getApiResult();
}
