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

    private NioAccepter accepter;

    public NioReceiver(Selector selector,NioAccepter accepter){
        this.selector = selector;
        this.accepter = accepter;
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
        while (true){
            try {
                int num = selector.select();
                if(num > 0){
                    log.info("收到{}个请求",num);
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if(key.isAcceptable()){
                            accepter.run();
                        }
                        if(key.isReadable()){
                            if(((SocketChannel) key.channel()).isConnected()){
                                byte[] data = SimpleUtils.receiveDataInNIO((SocketChannel) key.channel());
                                if(data.length > 0){
                                    queue.put((SocketChannel) key.channel(),data);
                                }else {
                                    log.info("该链接无效");
                                    key.channel().close();
                                }
                            }else {
                                log.info("该链接无效");
                            }
                        }
                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                log.error("发生错误，原因:{}",e);
                e.printStackTrace();
            }
        }
    }

    public void setQueue(NioMessageQueue queue){
        this.queue = queue;
    }

    public NioMessageQueue getQueue(){
        return this.queue;
    }
}
