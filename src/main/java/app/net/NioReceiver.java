package app.net;

import app.log.Log;
import app.system.Core;
import app.utils.SimpleUtils;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @ClassName : app.net.NioReceiver
 * @Description :
 * @Date 2021-09-23 13:33:12
 * @Author ZhangHL
 */
public class NioReceiver implements Runnable{

    private Selector selector;

    private Log log = Core.log;

    private NioMessageQueue queue;

    public NioReceiver(Selector selector){
        this.selector = selector;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            int num = selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                byte[] data = SimpleUtils.receiveDataInNIO((SocketChannel) key.channel());
                queue.put((SocketChannel) key.channel(),data);
                iterator.remove();
            }
        } catch (IOException e) {
            log.error("发生错误，原因:{}",e);
            e.printStackTrace();
        }
    }

    public void setQueue(NioMessageQueue queue){
        this.queue = queue;
    }

    public NioMessageQueue getQueue(){
        return this.queue;
    }
}
