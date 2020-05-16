package com.training.mina.tcp.server;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class TCPServer {

    final static int _TCP_PORT = 60000;
    public static void main(String[] args) {
        IoAcceptor acceptor = new NioSocketAcceptor();
        // acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new TextLineCodecFactory(Charset.forName("UTF-8"))));
        acceptor.setHandler(new ServerReadWriteLockHandler());
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 100);
        try {
            acceptor.bind(new InetSocketAddress(_TCP_PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
