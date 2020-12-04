package team.happy.game.commons.utils;

import org.smartboot.socket.transport.AioSession;
import team.happy.game.commons.entity.Message;

import java.io.IOException;

/**
 * @author 冰冰
 * @date 2020/11/21 上午11:30
 */
public class SocketUtil {
    public static void sendMsg(AioSession session, Message message){
        try {
            String msg = JsonUtil.toJson(message);
            System.out.println(msg);
            byte[] msgBody = msg.getBytes();
            byte[] msgHead = {(byte) msgBody.length};
            session.writeBuffer().write(msgHead);
            session.writeBuffer().write(msgBody);
            session.writeBuffer().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
