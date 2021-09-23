package app.net;

import app.net.entity.Message;
import java.nio.channels.SocketChannel;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @ClassName : app.net.NioMessageQueue
 * @Description :
 * @Date 2021-09-23 14:43:30
 * @Author ZhangHL
 */
public class NioMessageQueue {

    private Queue<Message> queue;

    public NioMessageQueue(){
        queue = new PriorityQueue<>();
    }

    public void put(SocketChannel channel,byte[] data){
        Message message = new Message();
        message.setChannel(channel);
        message.setData(data);
        synchronized (NioMessageQueue.class){
            queue.add(message);
        }
    }

    public Message get(){
        synchronized (NioMessageQueue.class){
            return queue.poll();
        }
    }

    public boolean hasElement(){
        return !queue.isEmpty();
    }

}
