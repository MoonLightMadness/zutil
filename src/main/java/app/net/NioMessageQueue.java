package app.net;

import app.log.Log;
import app.net.entity.Message;
import app.system.Core;

import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @ClassName : app.net.NioMessageQueue
 * @Description :
 * @Date 2021-09-23 14:43:30
 * @Author ZhangHL
 */
public class NioMessageQueue {

    private Log log = Core.log;

    private volatile Queue<Message> queue;

    public NioMessageQueue(){
        queue = new LinkedList<>();
    }

    public void put(SocketChannel channel,byte[] data){
        Message message = new Message();
        message.setChannel(channel);
        message.setData(data);
        message.setTimeStamp(LocalDateTime.now().toString());
        synchronized (NioMessageQueue.class){
            queue.add(message);
            log.info("消息队列消息+1,目前队列中有{}条消息",queue.size());
        }
    }

    public Message get(){
        synchronized (NioMessageQueue.class){
            Message message = queue.poll();
            if(message != null){
                log.info("消息队列消息-1,目前队列中有{}条消息",queue.size()-1);
                return message;
            }
            return null;
        }
    }

    public Queue<Message> getQueue(){
        return this.queue;
    }

    public boolean hasElement(){
        return !queue.isEmpty();
    }

}
