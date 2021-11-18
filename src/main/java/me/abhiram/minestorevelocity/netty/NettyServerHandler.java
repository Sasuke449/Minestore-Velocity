package me.abhiram.minestorevelocity.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServerHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel serverSocket) throws Exception {
        serverSocket.pipeline().addLast(new StringEncoder());
        serverSocket.pipeline().addLast(new StringDecoder());
        serverSocket.pipeline().addLast(new NettyMinestoreCommandHandler());
    }
}
