package com.ksh.nettyapi.core;


import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-02
 * Time : 오전 11:39
 * To change this template use File | Settings | File and Code Templates.
 */
public class ApiRequestParser extends SimpleChannelInboundHandler<FullHttpMessage> {

    static org.slf4j.Logger logger = LoggerFactory.getLogger(ApiRequestParser.class);

    private HttpRequest request;

    private JsonObject apiResult;

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

    private HttpPostRequestDecoder decoder;

    private Map<String,String> reqData = new HashMap<String,String>();

    private static final Set<String> usingHeader = new HashSet<String>();
    static{
        usingHeader.add("token");
        usingHeader.add("email");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpMessage msg) throws Exception {
        //Request header 처리
        if(msg instanceof HttpRequest){
            this.request = (HttpRequest)msg;

            if(HttpHeaders.is100ContinueExpected(request)){
                send100Continue(ctx);
            }

            HttpHeaders httpHeaders =request.headers();
            if(!httpHeaders.isEmpty()){
                for(Map.Entry<String,String> h : httpHeaders){
                    String key = h.getKey();
                    if(usingHeader.contains(key)){
                        reqData.put(key, h.getValue());
                    }
                }
            }

            reqData.put("REQUEST_URI",request.getUri());
            reqData.put("REQUEST_METHOD",request.getMethod().name());
        }

        // Request content 처리.
        if(msg instanceof HttpContent){
//            HttpContent httpContent = (HttpContent)msg;
//            ByteBuf content = httpContent.content();
            if(msg instanceof LastHttpContent){
                logger.debug("LastHttpContent message received!!"+ request.getUri());

                LastHttpContent trailer = (LastHttpContent)msg;

                readPostData();

                ApiRequest service = ServiceDispatcher.dispatch(reqData);
                try{
                    service.executeService();

                    apiResult = service.getApiResult();
                }finally {
                    reqData.clear();
                }

                if(!writeResponse(trailer, ctx)){
                    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                            .addListener(ChannelFutureListener.CLOSE);
                }
                reset();
            }
        }
    }

    private void readPostData() {
        try{
            decoder = new HttpPostRequestDecoder(factory,request);
            for(InterfaceHttpData data : decoder.getBodyHttpDatas()){
                if(InterfaceHttpData.HttpDataType.Attribute == data.getHttpDataType()){
                    try{
                        Attribute attribute = (Attribute)data;
                        reqData.put(attribute.getName(), attribute.getValue());
                    }catch (IOException e){
                        logger.error("BODY Attribute: " + data.getHttpDataType().name(),e);
                        return;
                    }
                }else{
                    logger.info("BODY data : " + data.getHttpDataType().name() + " : " + data);
                }
            }
        }catch (HttpPostRequestDecoder.ErrorDataDecoderException e){
            logger.error(e.toString());
        }finally{
            if(decoder != null)
                decoder.destroy();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("요청 처리 완료");
        ctx.flush();
    }

    ///TODO : 책에 없는 내용 확인 하기
    private void reset() {
        request = null;
    }


    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause.toString());
        ctx.close();
    }

    private boolean writeResponse(HttpObject currentObj, ChannelHandlerContext ctx) {
        // Decide whether to close the connection or not.
        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                currentObj.getDecoderResult().isSuccess() ? OK : BAD_REQUEST, Unpooled.copiedBuffer(
                apiResult.toString(), CharsetUtil.UTF_8));

        response.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            // Add keep alive header as per:
            // -
            // http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        // Write the response.
        ctx.write(response);

        return keepAlive;
    }
}
