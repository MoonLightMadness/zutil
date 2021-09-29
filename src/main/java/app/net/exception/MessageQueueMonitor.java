package app.net.exception;

import app.net.NioMessageQueue;
import app.net.entity.Message;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;

public class MessageQueueMonitor implements Runnable{

    private volatile Queue<Message> queue;

    public MessageQueueMonitor(Queue<Message> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true){
                synchronized (NioMessageQueue.class){
                    Iterator<Message> iterator = queue.iterator();
                    while (iterator.hasNext()){
                        Message message = iterator.next();
                        if(!message.getChannel().isConnected()){
                            SocketChannel socketChannel = message.getChannel();
                            socketChannel.close();
                            message.setChannel(null);
                            iterator.remove();
                        }
                    }
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
