package server;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private static Logger LOGGER = LogManager.getLogger(ServerHandler.class);
    private static List<Channel> channelList = new ArrayList<>();

    public void channelActive(final ChannelHandlerContext context){
        System.out.println("New user found " + context);
        channelList.add(context.channel());
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        for(Channel c : channelList){
            c.writeAndFlush("Hi " + msg + '\n');
        }
    }

    public void exceptionCaught(ChannelHandlerContext context, Throwable trouble){
        System.out.println("Closing connect for " + context);
        LOGGER.warn("Connection is lost");
        context.close();
    }
}
