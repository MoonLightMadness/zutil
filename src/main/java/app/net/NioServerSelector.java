package app.net;

import app.log.Log;
import app.system.Core;
import app.utils.ThreadUitls;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @ClassName : app.net.NioSelector
 * @Description :
 * @Date 2021-09-23 08:38:42
 * @Author ZhangHL
 */
public class NioServerSelector {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private NioAccepter accepter;

    private NioReceiver receiver;

    private NioMessageQueue queue;

    private Log log = Core.log;

    public NioServerSelector() {
        init("127.0.0.1", "10010");
    }

    public NioServerSelector(String ip, String port) {
        init(ip, port);
    }

    public void accept() {
        ThreadUitls.submit(accepter);
        log.info("Accepter启动");
    }

    public void read(){
        ThreadUitls.submit(receiver);
        log.info("Receiver启动");
    }

    public void shutdownAccept() {
        accepter.shutdown();
        log.info("关闭Accepter");
    }

    public NioMessageQueue getQueue() {
        return queue;
    }

    private void init(String ip, String port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(ip, Integer.parseInt(port)));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            accepter = new NioAccepter(selector, serverSocketChannel);
            receiver = new NioReceiver(selector,accepter);
            queue = new NioMessageQueue();
            receiver.setQueue(queue);
            log.info("ServerSelector启动");
        } catch (IOException e) {
            log.error("发生错误，原因:{}", e);
            e.printStackTrace();
        }
    }
}
