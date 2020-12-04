package team.happy.game.commons.entity;

/**
 * @author 冰冰
 * @date 2020/11/30 下午3:18
 */
public class Position {
    private Integer x;
    private Integer y;
    private Type type;
    public Position(){}

    public Position(Integer x, Integer y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        A,//横板
        B//竖板
    }
}
