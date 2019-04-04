package org.stfto.tomcat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.stfto.tomcat.component.NIOServerHanddler;

public class NIOSocketServerForTomcat {
    
    private String host = "127.0.0.1";
    
    private int port = 8080;

    private ExecutorService exec = new ThreadPoolExecutor(60, 60, 0L, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<Runnable>(2000));
    
    private ServerSocketChannel serverSocketChannel;
    
    private Selector selector;
    
    private NIOSocketServerForTomcat init() {
        this.host = "127.0.0.1";
        this.port = 8080;
        return this;
    }
    
    private void start() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(8080));
            
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            
            while (true) {
                //type type = (type) en.nextElement();
                selector.select(1000);
                
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                
                while (iterator.hasNext()) {
                    //type type = (type) en.nextElement();
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()){
                        SocketChannel channel = serverSocketChannel.accept();
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                        
                        
                    } else if (key.isReadable()){
                        SocketChannel channel = (SocketChannel) key.channel();
                        channel.configureBlocking(false);
                        exec.submit(new NIOServerHanddler(channel));
                        key.cancel();
                    }
                }
                
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
    }
    
    public static void main(String[] args) {
        new NIOSocketServerForTomcat().init().start();
    }

}
