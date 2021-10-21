package app.net;

import app.config.Config;
import app.log.Log;
import app.reflect.ReflectUtils;
import app.reflect.container.Indicators;
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

    private MessageQueueMonitor messageQueueMonitor;

    private Indicators indicators;

    private Log log = Core.log;

    private Config config = Core.configer;

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
            initIndicators();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(ip, Integer.parseInt(port)));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            accepter = new NioAccepter(selector, serverSocketChannel);
            receiver = new NioReceiver(selector,accepter);
            queue = new NioMessageQueue();
            messageQueueMonitor = new MessageQueueMonitor(queue.getQueue());
            receiver.setQueue(queue);
            workerOnline();
            log.info("ServerSelector启动");
        } catch (IOException e) {
            log.error("发生错误，原因:{}", e);
            e.printStackTrace();
        }
    }

    private void workerOnline(){
        WorkTrigger workTrigger1 = new WorkTrigger(queue, indicators);
        WorkTrigger workTrigger2 = new WorkTrigger(queue, indicators);
        WorkTrigger workTrigger3 = new WorkTrigger(queue, indicators);
        ThreadUitls.submit(workTrigger1);
        ThreadUitls.submit(workTrigger2);
        ThreadUitls.submit(workTrigger3);
        ThreadUitls.submit(messageQueueMonitor);
    }

    private void initIndicators(){
        indicators = new Indicators();
        indicators.initialize();
        System.out.println("反射初始化");
        ReflectUtils.constructReflectIndicator(config.read("package.find"), indicators);
        System.out.println("反射初始化完成");
    }
}
