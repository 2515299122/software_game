package team.happy.game.server.entity;

import org.smartboot.socket.transport.AioSession;

/**
 * @author 冰冰
 * @date 2020/12/2 下午4:02
 */
public class AioSessionPro {
    private AioSession aioSession;
    private long time;

    public AioSessionPro(AioSession aioSession) {
        this.time=System.currentTimeMillis();
        this.aioSession = aioSession;
    }

    public AioSessionPro(AioSession aioSession, long time) {
        this.aioSession = aioSession;
        this.time = time;
    }

    public AioSession getAioSession() {
        return aioSession;
    }

    public void setAioSession(AioSession aioSession) {
        this.aioSession = aioSession;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
