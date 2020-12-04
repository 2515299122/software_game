package team.happy.game.commons.entity;

import team.happy.game.commons.utils.Md5Util;

import java.io.Serializable;

/**
 * @author 冰冰
 * @date 2020/11/21 上午11:13
 */
public class Message<T> implements Serializable {
    public final static String SECRET_KEY = "Bing-StepChess";

    private Integer status;
    private T data;
    private String secretStr;
    private String num;//房间号

    public Message() {
        this.num = Code.getNum();
        this.secretStr = Md5Util.md5Password(SECRET_KEY);
    }

    public Message(Integer status, T data) {
        this.num = Code.getNum();
        this.secretStr = Md5Util.md5Password(SECRET_KEY);
        this.status = status;
        this.data = data;
    }

    public Message(Integer status) {
        this.num = Code.getNum();
        this.secretStr = Md5Util.md5Password(SECRET_KEY);
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getSecretStr() {
        return secretStr;
    }

    public void setSecretStr(String secretStr) {
        this.secretStr = secretStr;
    }

    public String getNum() {
        return num;
    }

    @Override
    public String toString() {
        return "Message{" +
                "status=" + status +
                ", data=" + data +
                ", secretStr='" + secretStr + '\'' +
                '}';
    }
}
