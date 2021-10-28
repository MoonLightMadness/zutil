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
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        int len = data.length;
        buffer.put(data);
        buffer.flip();
        try {
            int sended = channel.write(buffer);
            len -= sended;
            if(len > 0){
                byte[] rest = new byte[len];
                System.arraycopy(data,sended,rest,0,len);
                send(channel,rest);
            }
            channel.close();
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
