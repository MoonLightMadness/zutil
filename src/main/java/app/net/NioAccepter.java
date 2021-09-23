package app.net;

import app.log.Log;
import app.system.Core;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : app.net.NioAccepter
 * @Description :
 * @Date 2021-09-23 09:02:29
 * @Author ZhangHL
 */
public class NioAccepter implements Runnable {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private boolean enabled = true;

    private Log log = Core.log;

    private List<SocketChannel> channels;

    public NioAccepter(Selector selector,ServerSocketChannel serverSocketChannel){
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;
        channels = new ArrayList<>();
    }

    public void shutdown(){
        enabled = false;
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
            SocketChannel channel = serverSocketChannel.accept();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
            channels.add(channel);
            log.info("{}连接",channel.socket().getRemoteSocketAddress());
        } catch (IOException e) {
            log.error("发生错误，原因:{}",e);
            e.printStackTrace();
        }
    }
}
