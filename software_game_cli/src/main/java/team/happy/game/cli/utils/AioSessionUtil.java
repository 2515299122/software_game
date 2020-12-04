package team.happy.game.cli.utils;

import org.smartboot.socket.transport.AioQuickClient;
import org.smartboot.socket.transport.AioSession;
import team.happy.game.cli.socket.MessageProcessorCli;
import team.happy.game.commons.entity.Message;
import team.happy.game.commons.socket.StringProtocol;

/**
 * @author 冰冰
 * @date 2020/11/21 下午1:57
 */
public class AioSessionUtil {
    private static AioSession session;
//    private static AioQuickClient<Message> client = new AioQuickClient<>("81.71.6.187", 8370, new StringProtocol(),new MessageProcessorCli());
    private static AioQuickClient<Message> client = new AioQuickClient<>("127.0.0.1", 8370, new StringProtocol(),new MessageProcessorCli());
    public static AioSession getSession(){ ;
        try{
            if(session==null){
                client.setReadBufferSize(1024);
                client.setWriteBuffer(1024,32);
                session = client.start();
            }
            return session;
        }catch (Exception e){
            System.out.println("客户端启动失败");
            e.printStackTrace();
        }
        return null;
    }
}
