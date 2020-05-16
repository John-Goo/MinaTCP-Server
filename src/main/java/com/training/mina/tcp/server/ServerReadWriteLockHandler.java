package com.training.mina.tcp.server;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class ServerReadWriteLockHandler implements IoHandler {
    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        //System.out.println("[sessionCreated] ->" + ioSession.getId());
    }

    @Override
    public void sessionOpened(IoSession ioSession) throws Exception {
        //System.out.println("[sessionOpened] ->" + ioSession.getId());
    }

    @Override
    public void sessionClosed(IoSession ioSession) throws Exception {
        System.out.println("[sessionClosed] ->" + ioSession.getId());
        ioSession.close(true);
    }

    @Override
    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {
        //System.out.println("[sessionIdle] ->"+ioSession.getId());
        ioSession.close(true);


    }

    @Override
    public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {
        System.out.println("[exceptionCaught] ->" + ioSession.getId() + ",异常信息：" + throwable.getMessage());

    }

    @Override
    public void messageReceived(IoSession ioSession, Object o) {
        try {
            System.out.println("[->]"+o);
            if(o !=null){
                String str = o.toString();
                if(str.indexOf(",")!=-1){
                    String[] array = str.split(",");
                    ioSession.write(array[array.length-1]);
                }
            }
        // 慧云数字课堂
        }catch ( Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void messageSent(IoSession ioSession, Object o) throws Exception {

    }

    @Override
    public void inputClosed(IoSession ioSession) throws Exception {
        //System.out.println("[inputClosed] ->" + ioSession.getId());
        ioSession.close(true);
    }
}
