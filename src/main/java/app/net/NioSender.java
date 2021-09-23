package app.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @ClassName : app.net.NioSender
 * @Description :
 * @Date 2021-09-23 14:38:25
 * @Author ZhangHL
 */
public class NioSender {

    public static void send(SocketChannel channel,byte[] data){
        ByteBuffer byteBuffer = ByteBuffer.allocate(data.length);
        byteBuffer.put(data);
        byteBuffer.flip();
        try {
            channel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(SocketChannel channel,byte[] data,boolean close){
        send(channel,data);
        if(close){
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
