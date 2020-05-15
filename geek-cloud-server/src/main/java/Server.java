
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
            EventLoopGroup bossGroup = new NioEventLoopGroup(5);
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap configuringServer = new ServerBootstrap();
            try {
                configuringServer.group(bossGroup, workerGroup);
                configuringServer.channel(NioServerSocketChannel.class);
                configuringServer.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                                new ClientServerHandler(),
                                new ObjectDecoder(1024 * 1024 * 5, ClassResolvers.cacheDisabled(null)),
                                new ObjectEncoder()

                        );
                        System.out.println("Client connect");
                    }
                });
                ChannelFuture future = configuringServer.bind(PORT).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();

            }
        }
    }



