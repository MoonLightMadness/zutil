package app.net.entity;

import lombok.Data;

import java.nio.channels.SocketChannel;

/**
 * @ClassName : app.net.entity.Message
 * @Description :
 * @Date 2021-09-23 14:46:55
 * @Author ZhangHL
 */
@Data
public class Message {

    private SocketChannel channel;

    private byte[] data;

}
