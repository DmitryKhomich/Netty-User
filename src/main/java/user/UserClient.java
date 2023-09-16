package user;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class UserClient {
    private static Logger LOGGER = LogManager.getLogger(UserClient.class);
    public static final int PORT = 8999;
    public static final String HOST = "localhost";

    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new UserHandler());
                        }
                    });
            ChannelFuture ch = bootstrap.connect(HOST,PORT).sync();
            String input = "User";
            Channel channel = ch.sync().channel();
            channel.writeAndFlush(input);
            channel.flush();

        } catch (InterruptedException e) {
            LOGGER.warn("Smth went terribly wrong");
            throw new RuntimeException(e);
        } finally {
            LOGGER.warn("Unknown Error");
            group.shutdownGracefully();
        }
    }
}
