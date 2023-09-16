package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import user.UserClient;

public class Server {
    public static final int PORT = 8999;
    private static Logger LOGGER = LogManager.getLogger(UserClient.class);

    public static void main(String[] args) {

        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * @param ch the {@link Channel} which was registered.
                         * @throws Exception
                         */
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipe = ch.pipeline();
                            pipe.addLast(new StringDecoder());
                            pipe.addLast(new StringEncoder());
                            pipe.addLast(new ServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(PORT).sync();

            System.out.println("Server is waiting for new users...");
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            LOGGER.warn("Smth went wrong");
            throw new RuntimeException(e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
