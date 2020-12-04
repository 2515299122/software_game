package team.happy.game.server.socket;

import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioSession;
import team.happy.game.commons.entity.*;
import team.happy.game.commons.utils.JsonUtil;
import team.happy.game.commons.utils.SocketUtil;
import team.happy.game.server.entity.AioSessionPro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 冰冰
 * @date 2020/11/21 上午9:12
 */
public class MessageProcessorServer implements MessageProcessor<Message> {

    Map<String,List<AioSessionPro>> map=new HashMap<>();

    @Override
    public void process(AioSession session, Message message) {
        if(message==null){
            return;
        }
        System.out.println("服务端接受数据：" + message);
        //为请求加入房间的信息
        if (message.getStatus().equals(ActionStatus.A)) {
            String num = message.getNum();
            //存在房间号
            if(map.containsKey(num)){
                List<AioSessionPro> sessions = map.get(num);
                if(sessions.size()==1){
                    sessions.add(new AioSessionPro(session));
                    //房间人数已满,为客户端分配游戏用户
                    map.put(num,sessions);
                    SocketUtil.sendMsg(sessions.get(0).getAioSession(), new Message<>(ActionStatus.B, User.A));
                    SocketUtil.sendMsg(sessions.get(1).getAioSession(), new Message<>(ActionStatus.B, User.B));
                }else{
                    return;
                    //TODO 返回失败消息
                }
            }else{
                ArrayList<AioSessionPro> list = new ArrayList<>();
                list.add(new AioSessionPro(session));
                map.put(num,list);
            }
        }
        //玩家移动位置
        else if(message.getStatus().equals(ActionStatus.C)){
            String num = message.getNum();
            List<AioSessionPro> sessions = map.get(num);
            update(message.getNum(),session);
            //只给其他玩家发送消息,消息转发
            sessions.forEach(u->{
                if(!u.getAioSession().equals(session)){
                    message.setStatus(ActionStatus.D);
                    System.out.println("服务器发送消息，玩家移动");
                    SocketUtil.sendMsg(u.getAioSession(), message);
                }
            });
        }
        //玩家放置板子
        else if(message.getStatus().equals(ActionStatus.E)){
            String num = message.getNum();
            List<AioSessionPro> sessions = map.get(num);
            update(message.getNum(),session);
            //只给其他玩家发送消息,消息转发
            sessions.forEach(u->{
                if(!u.getAioSession().equals(session)){
                    message.setStatus(ActionStatus.F);
                    System.out.println("服务器发送消息，玩家放置板子");
                    SocketUtil.sendMsg(u.getAioSession(), message);
                }
            });
        }
    }
    @Override
    public void stateEvent(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {

    }
    //更新事件搓
    private void update(String message,AioSession session){
        List<AioSessionPro> sessions = map.get(message);
        sessions.forEach(s->{
            if(s.equals(session)){
                s.setTime(System.currentTimeMillis());
            }
        });
    }

    public Map<String, List<AioSessionPro>> getMap() {
        return map;
    }
}
