package smartTest;

import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioQuickClient;
import org.smartboot.socket.transport.AioSession;
import team.happy.game.commons.entity.ActionStatus;
import team.happy.game.commons.entity.Direction;
import team.happy.game.commons.entity.Message;
import team.happy.game.commons.utils.ByteUtil;
import team.happy.game.commons.utils.JsonUtil;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author 冰冰
 * @date 2020/11/20 下午2:42
 */
public class Client {
    public static void main(String[] args) throws IOException {
        AioQuickClient<String> client = new AioQuickClient<>("127.0.0.1", 8080, new StringProtocol(), new MessageProcessor<String>() {
            public void process(AioSession session, String msg) {
                System.out.println(msg);
                byte[] response = "客户端以接收消息!".getBytes();
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
        client.setWriteBuffer(100000,32);
        client.setReadBufferSize(100000);
        AioSession session = client.start();
        char[][] ints = new char[20][20];
        Message<Direction> message = new Message<>(ActionStatus.UN_KONW, Direction.DOWN);
//        message.setArr(ints);
        byte[] msgBody = JsonUtil.toJson(message).getBytes();
        byte[] msgHead = {(byte) msgBody.length};
        try {
            session.writeBuffer().write(msgHead);
            session.writeBuffer().write(msgBody);
            session.writeBuffer().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
