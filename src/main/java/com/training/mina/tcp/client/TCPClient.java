package com.training.mina.tcp.client;

import sun.security.util.AuthResources_pt_BR;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {

    final static ThreadLocal<Socket> tl = new ThreadLocal<>();

    final static String _IP = "127.0.0.1";
    final static int _PORT = 60000;


    public static class Builder {

        /**
         * 发送请求并接收服务端消息
         * @param msg
         * @return
         */
        public static String send(String msg) {
            String result = null;
            DataOutputStream outputStream = null;
            BufferedReader bufferedReader = null;
            try {
                Socket client = tl.get();
                if (client == null) {
                    client = new Socket(_IP, _PORT);
                    tl.set(client);
                }
                outputStream = new DataOutputStream(client.getOutputStream());
                bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));//连接套接字的输入流
                msg = msg + "\n";
                outputStream.write(msg.getBytes("utf-8"));
                result = bufferedReader.readLine();
                //System.out.println("服务端消息:" + result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
            return result;
        }

        /**
         * 关闭socket client
         */
        public static void close(){
            Socket client = tl.get();
            if(client !=null){
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            tl.set(null);
        }

    }

    public static void main(String[] args) {
        String msg = TCPClient.Builder.send("我是John Goo.");
        System.out.println(msg);
        msg = TCPClient.Builder.send("我是John Goo.");
        System.out.println(msg);
        TCPClient.Builder.close();
    }


}
