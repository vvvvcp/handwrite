package org.stfto.tomcat.component;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class NIOServerHanddler implements Runnable{
    
    private SocketChannel channel;

    public NIOServerHanddler(SocketChannel channel) {
        // TODO Auto-generated constructor stub
        this.channel = channel;
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
        

        try {
            ByteBuffer bb = ByteBuffer.allocate(1024);
            channel.read(bb);
            bb.flip();
            byte[] array = bb.array();
            bb.clear();
            System.out.println(new String(array));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
    }

}
