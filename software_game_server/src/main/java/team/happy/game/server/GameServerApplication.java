package team.happy.game.server;

import org.smartboot.socket.transport.AioQuickServer;
import team.happy.game.commons.entity.Message;
import team.happy.game.commons.socket.StringProtocol;
import team.happy.game.server.entity.AioSessionPro;
import team.happy.game.server.socket.MessageProcessorServer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 冰冰
 * @date 2020/11/21 上午9:14
 */
public class GameServerApplication {
    private final static long MAX_WAIT_TIME = 1000 * 60 * 60;

    public static void main(String[] args) throws IOException {
        // 1
        MessageProcessorServer messageProcessorServer = new MessageProcessorServer();
        AioQuickServer<Message> server = new AioQuickServer<>(8370, new StringProtocol(), messageProcessorServer);
        server.setReadBufferSize(1024);
        server.setWriteBuffer(1024, 32);
        //2
        server.start();
        //定时关闭不活跃的房间
        new Thread(() -> {
            try {
                Thread.sleep(MAX_WAIT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                Map<String, List<AioSessionPro>> map = messageProcessorServer.getMap();
                Set<String> keySet = map.keySet();
                for (String key : keySet) {
                    List<AioSessionPro> aioSessionPros = map.get(key);
                    for (int i = 0; i < aioSessionPros.size(); i++) {
                        if (System.currentTimeMillis() - aioSessionPros.get(i).getTime() > MAX_WAIT_TIME) {
                            map.remove(key);
                            System.out.println("移除房间号" + key);
                            System.out.println(keySet);
                            break;
                        }
                    }
                }
            }
        }).start();
    }
}
