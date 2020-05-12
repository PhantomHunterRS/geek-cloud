
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class Server {
    private static final int PORT = 8989;

    public static void main(String[] args) throws Exception {
        new Server().start();
    }
        public void start(){
            EventLoopGroup incomingConnections = new NioEventLoopGroup();
            EventLoopGroup processingDataStream = new NioEventLoopGroup();
            ServerBootstrap configuringServer = new ServerBootstrap();
            try {
                configuringServer.group(incomingConnections, processingDataStream);
                configuringServer.channel(NioServerSocketChannel.class);
                configuringServer.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                                new ObjectDecoder(1024 * 1024 * 5, ClassResolvers.cacheDisabled(null)),
                                new ObjectEncoder(),
                                new ClientServerHandler()
                        );
                    }
                });
                ChannelFuture future = configuringServer.bind(PORT).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                incomingConnections.shutdownGracefully();
                processingDataStream.shutdownGracefully();

            }
        }
    }



