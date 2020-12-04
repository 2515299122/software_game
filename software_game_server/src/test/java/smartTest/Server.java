package smartTest;

import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioQuickServer;
import org.smartboot.socket.transport.AioSession;
import team.happy.game.commons.utils.ByteUtil;

import java.io.IOException;

/**
 * @author 冰冰
 * @date 2020/11/20 下午2:41
 */
public class Server {
    public static void main(String[] args) throws IOException {
        // 1
        AioQuickServer<String> server = new AioQuickServer<String>(8080, new StringProtocol(), new MessageProcessor<String>() {
            public void process(AioSession session, String msg) {
                System.out.println("接受到客户端消息:" + msg);

                byte[] response = "Hi Client!".getBytes();
                byte[] head = {(byte) response.length};
                try {
                    session.writeBuffer().write(head);
                    session.writeBuffer().write(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void stateEvent(AioSession session, StateMachineEnum stateMachineEnum, Throwable throwable) {
            }
        });
        server.setWriteBuffer(100000,32);
        server.setReadBufferSize(100000);
        //2
        server.start();
    }
}

