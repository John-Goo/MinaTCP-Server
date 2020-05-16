package com.training.mina.tcp.server;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Map;

public class ServerSharedLockHandler implements IoHandler {
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
            if (o == null) {
                return;
            }
            String msg = o.toString();
           // System.out.println("[messageReceived] ->" + ioSession.getId() + ",接收信息：" + msg);
            // 启动计算标志
            if (msg.indexOf("start#") != -1) {
                String[] metaArry = msg.split("#");
                /*初始化数据*/
                ioSession.setAttribute("threadId", metaArry[2]);
                ioSession.setAttribute("execSeq", 0);
                ioSession.setAttribute("calcVal", 0);
                System.out.println("*** 开始计算目标批次标识：" + metaArry[1] + ",目标线程ID:" + metaArry[2]);
                ioSession.write("OK");
            }

            /*
             * 计算过程
             * */
            if (msg.indexOf(",") != -1) {
                String[] funData = msg.split(",");
                System.out.println("收到线程ID:" + funData[2] + ",序号：" + funData[0] + " -->数据：" + funData[1]);
                int dVal = Integer.valueOf(funData[1]);
                Integer calcVal = (Integer) ioSession.getAttribute("calcVal");
                System.out.println("====>本次计算累计结果：" + (dVal + calcVal));
                ioSession.setAttribute("calcVal", dVal + calcVal);
                ioSession.write("yes");
            }


            // 标识结束标记
            if (msg.indexOf("end#") != -1) {
                String[] metaArry = msg.split("#");
                System.out.println("*** 开始计算目标批次标识：" + metaArry[1] + ",目标线程ID:" + metaArry[2]);
                // 返回结果值
                ioSession.write(ioSession.getAttribute("calcVal"));
                /*清除缓存数据*/
                ioSession.removeAttribute("threadId");
                ioSession.removeAttribute("execSeq");
                ioSession.removeAttribute("calcVal");
            }
        }catch ( Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void messageSent(IoSession ioSession, Object o) throws Exception {

    }

    @Override
    public void inputClosed(IoSession ioSession) throws Exception {
        System.out.println("[inputClosed] ->" + ioSession.getId());
        ioSession.close(true);
    }
}
