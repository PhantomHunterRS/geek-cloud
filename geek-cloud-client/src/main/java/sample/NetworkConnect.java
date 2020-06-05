package sample;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.net.Socket;

public class NetworkConnect {
    public Channel channelClient;
    public Socket socket;
    public String IP_ADRESS = "127.0.0.1";
    public int PORT = 8989;

    public Channel getChannelClient() {
        return channelClient;
    }
    public void start(){
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap().group(clientGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress( new InetSocketAddress(IP_ADRESS,PORT))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new ClientAdapterHandler()
                            );
                            channelClient = socketChannel;
                            System.out.println("connect" + channelClient.toString());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            clientGroup.shutdownGracefully();
        }
    }
}
