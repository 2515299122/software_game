package team.happy.game.cli.socket;

import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioSession;
import team.happy.game.cli.utils.GlobalPathUtil;
import team.happy.game.commons.entity.ActionStatus;
import team.happy.game.commons.entity.Message;
import team.happy.game.commons.entity.User;

/**
 * @author 冰冰
 * @date 2020/11/21 上午9:11
 */
public class MessageProcessorCli implements MessageProcessor<Message> {
    public static Message message;

    public synchronized static Message getMessage() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return message;
    }
    public  static void setMessage(){
        message=new Message(ActionStatus.UN_KONW,null);
    }

    @Override
    public void process(AioSession aioSession, Message msg) {
        message=msg;
        //等待接受信息
        if (msg != null && msg.getStatus().equals(ActionStatus.B)) {
            System.out.println("人数已经到齐，开始游戏");
        }
    }

    @Override
    public void stateEvent(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {

    }

}
