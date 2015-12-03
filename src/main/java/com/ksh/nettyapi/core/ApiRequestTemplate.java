package com.ksh.nettyapi.core;

import com.google.gson.JsonObject;
import com.ksh.nettyapi.exception.RequestParamException;
import com.ksh.nettyapi.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-02
 * Time : 오후 5:48
 * To change this template use File | Settings | File and Code Templates.
 */
public abstract class ApiRequestTemplate implements ApiRequest {

    protected Logger logger;

    protected Map<String,String> reqData;

    protected JsonObject apiResult;

    public ApiRequestTemplate(Map<String,String> reqData){
        this.logger = LoggerFactory.getLogger(ApiRequestTemplate.class);
        this.apiResult = new JsonObject();
        this.reqData = reqData;

        logger.info("request data : " + this.reqData);
    }

    public void executeService(){
        try{
            this.requestParamValidation();
            this.service();
        }catch (RequestParamException e){
            logger.error(e.toString());
            this.apiResult.addProperty("resultCode" , "405");
        }catch(ServiceException e){
            logger.error(e.toString());
            this.apiResult.addProperty("resultCode" , "501");
        }
    }

    public JsonObject getApiResult(){
        return this.apiResult;
    }


    //TODO : 이것 책에 없는 내용.

//    @Override
//    public void requestParamValidation() throws RequestParamException {
//        if (getClass().getClasses().length == 0) {
//            return;
//        }
//
//        // // TODO 이건 꼼수 바꿔야 하는데..
//        // for (Object item :
//        // this.getClass().getClasses()[0].getEnumConstants()) {
//        // RequestParam param = (RequestParam) item;
//        // if (param.isMandatory() && this.reqData.get(param.toString()) ==
//        // null) {
//        // throw new RequestParamException(item.toString() +
//        // " is not present in request param.");
//        // }
//        // }
//    }
//
//    public final <T extends Enum<T>> T fromValue(Class<T> paramClass, String paramValue) {
//        if (paramValue == null || paramClass == null) {
//            throw new IllegalArgumentException("There is no value with name '" + paramValue + " in Enum "
//                    + paramClass.getClass().getName());
//        }
//
//        for (T param : paramClass.getEnumConstants()) {
//            if (paramValue.equals(param.toString())) {
//                return param;
//            }
//        }
//
//        throw new IllegalArgumentException("There is no value with name '" + paramValue + " in Enum "
//                + paramClass.getClass().getName());
//    }



}
