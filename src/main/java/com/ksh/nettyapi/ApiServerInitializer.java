package com.ksh.nettyapi;

import com.ksh.nettyapi.core.ApiRequestParser;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-02
 * Time : 오전 11:35
 * To change this template use File | Settings | File and Code Templates.
 */
public class ApiServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public ApiServerInitializer(SslContext sslCtx){
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        if(sslCtx != null){
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }

        p.addLast(new HttpRequestDecoder());
        p.addLast(new HttpObjectAggregator(65536));
        p.addLast(new HttpResponseEncoder());
        p.addLast(new HttpContentCompressor());
        p.addLast(new ApiRequestParser());

    }
}
