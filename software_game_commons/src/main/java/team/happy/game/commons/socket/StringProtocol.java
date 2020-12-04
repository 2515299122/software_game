package team.happy.game.commons.socket;

import org.smartboot.socket.Protocol;
import org.smartboot.socket.transport.AioSession;
import team.happy.game.commons.entity.Message;
import team.happy.game.commons.utils.JsonUtil;
import team.happy.game.commons.utils.Md5Util;

import java.nio.ByteBuffer;

/**
 * @author 冰冰
 * @date 2020/11/21 上午9:08
 */
public class StringProtocol implements Protocol<Message> {
    @Override
    public Message decode(ByteBuffer buffer, AioSession session) {
        buffer.mark();
        byte length = buffer.get();
        if (buffer.remaining() < length) {
            buffer.reset();
            return null;
        }
        byte[] body = new byte[length];
        buffer.get(body);
        buffer.mark();
        String json = new String(body);
        Message message = JsonUtil.toObject(json, Message.class);
        if(message==null|| !message.getSecretStr().equals(Md5Util.md5Password(Message.SECRET_KEY))){
            return null;
        }
        return message;
    }
}