package com.ksh.nettyapi;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;

/**
 * Created by Helloworld
 * User : USER
 * Date : 2015-12-02
 * Time : 오전 11:15
 * To change this template use File | Settings | File and Code Templates.
 */
@Component
public class ApiServer {
    @Autowired
    @Qualifier("tcpSocketAddress")
    private InetSocketAddress address;

    @Autowired
    @Qualifier("bossThreadCount")
    private int bossThreadCount;

    @Autowired
    @Qualifier("workerThreadCount")
    private int workerThreadCount;

//TODO: 이거 사용해야 하는거 아닌가?
    @Autowired
    @Qualifier("tcpPort")
    private int tcpPort;

    public void start(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(bossThreadCount);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerThreadCount);
        ChannelFuture channelFuture = null;

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ApiServerInitializer(null));
            Channel ch = b.bind(tcpPort).sync().channel();
            channelFuture = ch.closeFuture();
            //HTTP
            //channelFuture.sync();

            final SslContext sslCtx;
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContext.newServerContext(ssc.certificate(),ssc.privateKey());

            ServerBootstrap b2 = new ServerBootstrap();
            b2.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ApiServerInitializer(sslCtx));

            Channel ch2 = b2.bind(8443).sync().channel();

            channelFuture = ch2.closeFuture();
            channelFuture.sync();

        }catch (InterruptedException | CertificateException e){
            e.printStackTrace();
        }catch (SSLException e){
            e.printStackTrace();
        }finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
