package smartTest;

import org.smartboot.socket.Protocol;
import org.smartboot.socket.transport.AioSession;
import team.happy.game.commons.utils.ByteUtil;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author 冰冰
 * @date 2020/11/20 下午2:39
 */
public class StringProtocol implements Protocol<String> {
    @Override
    public String decode(ByteBuffer buffer, AioSession session) {
        buffer.mark(); // 1
        byte length = buffer.get(); // 2
        if (buffer.remaining() < length) { // 3
            buffer.reset(); // 4
            return null;
        }
        byte[] body = new byte[length];
        buffer.get(body); // 5
        buffer.mark(); // 6
        return new String(body); // 7
    }
}