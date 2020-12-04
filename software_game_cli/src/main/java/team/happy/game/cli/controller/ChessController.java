package team.happy.game.cli.controller;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.smartboot.socket.transport.AioSession;
import team.happy.game.cli.components.ChessPane;
import team.happy.game.cli.socket.MessageProcessorCli;
import team.happy.game.cli.utils.AioSessionUtil;
import team.happy.game.cli.utils.GlobalPathUtil;
import team.happy.game.commons.entity.*;
import team.happy.game.commons.utils.JsonUtil;
import team.happy.game.commons.utils.SocketUtil;

/**
 * @author 冰冰
 * @date 2020/11/27 上午11:10
 */
public class ChessController {
    @FXML
    private BorderPane mainPane;
    private ChessPane chessPane;
    private ImageView user1;
    private ImageView user2;
    private Color user1Color;
    private Color user2Color;
    private boolean flag;//用于判断是否轮到自己下棋
    private Position chess2Position;//当前棋子所在位置
    private Position chess1Position;//对方棋子所在位置
    private char u2;
    private char u1;

    public ChessController() {
    }

    @FXML
    public void initialize() {
        chessPane = new ChessPane();
        chessPane.setOnMouseClicked(event -> {
            //限制监听范围，防止鼠标数组越界
            if(event.getX()<68||event.getX()>520){
                return;
            }
            if(event.getY()<60||event.getY()>630){
                return;
            }

            //确认轮到自己下棋才执行
            if (this.flag) {
                MouseButton button = event.getButton();
                //右键单击事件，画竖线
                if (button == MouseButton.SECONDARY) {
                    double x = event.getSceneX() - chessPane.getAlign() + chessPane.getLineWidth() / 2;
                    double y = event.getSceneY() - chessPane.getAlign() + chessPane.getLineWidth() / 2;
                    double cellLen = chessPane.getCellLen();
                    double lineWidth = chessPane.getLineWidth();
                    if (x % cellLen < lineWidth && x % cellLen > 0) {
                        if (y % cellLen < lineWidth && y % cellLen > 0) {
                            int positionX = (int) (x / cellLen);
                            int positionY = (int) (y / cellLen);
                            //棋盘边缘不允许放置
                            if (positionX == 0 || positionY == 0) {
                                return;
                            }
                            System.out.println("(" + positionX + "," + positionY + ")");
                            //更新二维数组
                            char[][] chessArr = this.chessPane.getChessArr();
                            if (chessArr[2 * positionY][2 * positionX - 1] == '1' ||
                                    chessArr[2 * (positionY - 1)][2 * positionX - 1] == '1' ||
                                    chessArr[2 * positionY - 1][2 * positionX - 1] == '1') {
                                return;
                            }
                            chessArr[2 * positionY][2 * positionX - 1] = '1';
                            chessArr[2 * positionY - 1][2 * positionX - 1] = '1';
                            chessArr[2 * positionY - 2][2 * positionX - 1] = '1';
                            chessPane.setChessArr(chessArr);
                            vertical(chessPane, positionX, positionY, user2Color);
                            this.flag = false;
                            AioSession session = AioSessionUtil.getSession();
                            assert session != null;
                            Message<Position> message = new Message<>(ActionStatus.E, new Position(positionX, positionY, Position.Type.B));
                            SocketUtil.sendMsg(session, message);
                        }
                    }
                }
                //中键点击事件，画横线
                else if (button == MouseButton.MIDDLE) {
                    double x = event.getSceneX() - chessPane.getAlign() + chessPane.getLineWidth() / 2;
                    double y = event.getSceneY() - chessPane.getAlign() + chessPane.getLineWidth() / 2;
                    double cellLen = chessPane.getCellLen();
                    double lineWidth = chessPane.getLineWidth();
                    if (x % cellLen < lineWidth && x % cellLen > 0) {
                        if (y % cellLen < lineWidth && y % cellLen > 0) {
                            int positionX = (int) (x / cellLen);
                            int positionY = (int) (y / cellLen);
                            //棋盘边缘不允许放置
                            if (positionX == 0 || positionY == 0) {
                                return;
                            }
                            System.out.println("(" + positionX + "," + positionY + ")");
                            horizontal(chessPane, positionX, positionY, user2Color);
                            this.flag = false;
                            //更新二维数组
                            char[][] chessArr = this.chessPane.getChessArr();
                            if (chessArr[2 * positionY - 1][2 * (positionX - 1)] == '1' ||
                                    chessArr[2 * positionY - 1][2 * positionX] == '1' ||
                                    chessArr[2 * positionY - 1][2 * positionX - 1] == '1') {
                                return;
                            }
                            chessArr[2 * positionY - 1][2 * positionX - 1] = '1';
                            chessArr[2 * positionY - 1][2 * positionX - 2] = '1';
                            chessArr[2 * positionY - 1][2 * positionX] = '1';
                            chessPane.setChessArr(chessArr);

                            AioSession session = AioSessionUtil.getSession();
                            assert session != null;
                            Message<Position> message = new Message<>(ActionStatus.E, new Position(positionX, positionY, Position.Type.A));
                            SocketUtil.sendMsg(session, message);
                        }
                    }
                }
                //左键单击事件
                else if (button == MouseButton.PRIMARY) {
                    Direction direction = userStep(event, user2);
                    if(direction!=null){
                        AioSession session = AioSessionUtil.getSession();
                        assert session != null;
                        Message<Direction> message = new Message<>(ActionStatus.C, direction);
                        SocketUtil.sendMsg(session, message);
                    }

                }
            }
        });
        mainPane.setLeft(chessPane);
        setUser();
        new Thread(() -> {
            TranslateTransition translateTransition = new TranslateTransition();
            translateTransition.setDuration(Duration.seconds(0.3));
            translateTransition.setNode(user1);
            //循环监听其他玩家的动作
            while (true) {
                Message message = null;
                try {
                    message = MessageProcessorCli.getMessage();
                    //接受到其他玩家的下棋动作
                    if (message.getStatus().equals(ActionStatus.D)) {
                        Direction direction = JsonUtil.toObject(message.getData(), Direction.class);
//                        System.out.println(direction);
                        if (direction != null) {
                            this.flag = true;
                            Integer x1 = this.chess1Position.getX();
                            Integer y1 = this.chess1Position.getY();
                            char[][] chessArr = this.chessPane.getChessArr();
                            switch (direction) {
                                case UP:
                                    chessArr[x1][y1] = '0';
                                    chessArr[x1 - 2][y1] = this.u1;
                                    this.chess1Position.setX(chess1Position.getX() - 2);
                                    this.chessPane.setChessArr(chessArr);
                                    toDown(translateTransition, user1.getTranslateY(), chessPane.getCellLen());
                                    break;
                                case DOWN:
                                    chessArr[x1][y1] = '0';
                                    chessArr[x1 + 2][y1] = this.u1;
                                    this.chess1Position.setX(chess1Position.getX() + 2);
                                    this.chessPane.setChessArr(chessArr);
                                    toUp(translateTransition, user1.getTranslateY(), chessPane.getCellLen());
                                    break;
                                case LEFT:
                                    chessArr[x1][y1] = '0';
                                    chessArr[x1][y1 + 2] = this.u1;
                                    this.chess1Position.setY(chess1Position.getY() + 2);
                                    this.chessPane.setChessArr(chessArr);
                                    toRight(translateTransition, user1.getTranslateX(), chessPane.getCellLen());
                                    break;
                                case RIGHT:
                                    chessArr[x1][y1] = '0';
                                    chessArr[x1][y1 - 2] = this.u1;
                                    this.chess1Position.setY(chess1Position.getY() - 2);
                                    this.chessPane.setChessArr(chessArr);
                                    toLeft(translateTransition, user1.getTranslateX(), chessPane.getCellLen());
                                    break;
                                default:
                                    MessageProcessorCli.setMessage();
                            }
                            MessageProcessorCli.setMessage();
                        }
                    } else if (message.getStatus().equals(ActionStatus.F)) {
                        Message finalMessage = message;
                        Platform.runLater(() -> {
                            Position position = JsonUtil.toObject(finalMessage.getData(), Position.class);
                            Integer positionY = 10 - position.getY();
                            Integer positionX = 8 - position.getX();
                            char[][] chessArr = chessPane.getChessArr();
                            if (position != null) {
                                this.flag = true;
                                switch (position.getType()) {
                                    case A:
                                        chessArr[2 * positionY - 1][2 * positionX - 2] = '1';
                                        chessArr[2 * positionY - 1][2 * positionX - 1] = '1';
                                        chessArr[2 * positionY - 1][2 * positionX] = '1';
                                        chessPane.setChessArr(chessArr);
                                        horizontal(chessPane, chessPane.getN() - position.getX(), chessPane.getM() - position.getY(), user1Color);
                                        break;
                                    case B:
                                        chessArr[2 * positionY][2 * positionX - 1] = '1';
                                        chessArr[2 * positionY - 2][2 * positionX - 1] = '1';
                                        chessArr[2 * positionY - 1][2 * positionX - 1] = '1';
                                        chessPane.setChessArr(chessArr);
                                        vertical(chessPane, chessPane.getN() - position.getX(), chessPane.getM() - position.getY(), user1Color);
                                        break;
                                    default:
                                }
                                MessageProcessorCli.setMessage();
                            }
                        });

                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    System.out.println(message);
                }

            }
        }).start();
    }
    //绘制垂直板子
    private void vertical(ChessPane chessPane, int positionX, int positionY, Color color) {
        double cellLen = chessPane.getCellLen();
        double lineWidth = chessPane.getLineWidth();
        Rectangle rectangle = new Rectangle(lineWidth, cellLen * 2 - lineWidth, color);
        rectangle.setLayoutX(chessPane.getAlign() - lineWidth / 2 + positionX * cellLen);
        rectangle.setLayoutY(chessPane.getAlign() + positionY * cellLen - rectangle.getHeight() / 2);
        chessPane.getChildren().add(rectangle);
    }

    //绘制水平板子
    private void horizontal(ChessPane chessPane, int positionX, int positionY, Color color) {
        double cellLen = chessPane.getCellLen();
        double lineWidth = chessPane.getLineWidth();
        Rectangle rectangle = new Rectangle(cellLen * 2 - lineWidth, lineWidth, color);
        rectangle.setLayoutY(chessPane.getAlign() - lineWidth / 2 + positionY * cellLen);
        rectangle.setLayoutX(chessPane.getAlign() + positionX * cellLen - rectangle.getWidth() / 2);
        chessPane.getChildren().add(rectangle);
    }

    private Direction userStep(MouseEvent mouseEvent, ImageView user) {
        double translateX = user.getTranslateX();
        double translateY = user.getTranslateY();

        double x = user.getLayoutX();
        double y = user.getLayoutY();
        double mousePositionX = mouseEvent.getSceneX() - x - chessPane.getLayoutX() - user.getFitWidth() / 2;
        double mousePositionY = mouseEvent.getSceneY() - y - chessPane.getLayoutY() - user.getFitHeight() / 2;
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setDuration(Duration.seconds(0.3));
        translateTransition.setNode(user);
        System.out.println("x=============::::" + mousePositionX + ":" + translateX);

        double minLength = chessPane.getCellLen() / 2 + chessPane.getLineWidth() / 2;
        double maxLength = chessPane.getCellLen() * 1.5 - chessPane.getLineWidth() / 2;

        if (Math.abs(translateX - mousePositionX) < maxLength && Math.abs(translateX - mousePositionX) > minLength) {
            //向左边
            if (mousePositionX < translateX) {
                this.flag = false;
                Integer x1 = this.chess2Position.getX();
                Integer y1 = this.chess2Position.getY();
                char[][] chessArr = this.chessPane.getChessArr();
                chessArr[x1][y1] = '0';
                chessArr[x1][y1 - 2] = this.u2;
                this.chess2Position.setY(chess2Position.getY() - 2);
                this.chessPane.setChessArr(chessArr);
                return toLeft(translateTransition, translateX, chessPane.getCellLen());
            }
            //向右边
            if (mousePositionX > translateX) {
                this.flag = false;
                Integer x1 = this.chess2Position.getX();
                Integer y1 = this.chess2Position.getY();
                char[][] chessArr = this.chessPane.getChessArr();
                chessArr[x1][y1] = '0';
                chessArr[x1][y1 + 2] = this.u2;
                this.chess2Position.setY(chess2Position.getY() + 2);
                this.chessPane.setChessArr(chessArr);
                return toRight(translateTransition, translateX, chessPane.getCellLen());

            }
        } else if (Math.abs(translateY - mousePositionY) < maxLength && Math.abs(translateY - mousePositionY) > minLength) {
            System.out.println("y=============::::" + mousePositionY + ":" + translateY);
            //向上边
            if (mousePositionY > translateY) {
                this.flag = false;
                Integer x1 = this.chess2Position.getX();
                Integer y1 = this.chess2Position.getY();
                char[][] chessArr = this.chessPane.getChessArr();
                chessArr[x1][y1] = '0';
                chessArr[x1 + 2][y1] = this.u2;
                this.chess2Position.setX(chess2Position.getX() + 2);
                this.chessPane.setChessArr(chessArr);
                return toUp(translateTransition, translateY, chessPane.getCellLen());

            }
            //向下边
            if (mousePositionY < translateY) {
                this.flag = false;
                Integer x1 = this.chess2Position.getX();
                Integer y1 = this.chess2Position.getY();
                char[][] chessArr = this.chessPane.getChessArr();
                chessArr[x1][y1] = '0';
                chessArr[x1 - 2][y1] = this.u2;
                this.chess2Position.setX(chess2Position.getX() - 2);
                this.chessPane.setChessArr(chessArr);
                return toDown(translateTransition, translateY, chessPane.getCellLen());

            }
        }

        return null;
    }

    private Direction toDown(TranslateTransition tt, double translateY, double cell) {
        tt.setToY(translateY - cell);
        tt.play();
        return Direction.DOWN;
    }

    private Direction toUp(TranslateTransition tt, double translateY, double cell) {
        tt.setToY(translateY + cell);
        tt.play();
        return Direction.UP;
    }

    private Direction toLeft(TranslateTransition tt, double translateX, double cell) {
        tt.setToX(translateX - cell);
        tt.play();
        return Direction.LEFT;
    }

    private Direction toRight(TranslateTransition tt, double translateX, double cell) {
        tt.setToX(translateX + cell);
        tt.play();
        return Direction.RIGHT;
    }

    /**
     * 获取棋子位置
     */
    private Position getChessPosition(char c) {
        char[][] chessArr = chessPane.getChessArr();
        for (int i = 0; i < chessArr.length; i++) {
            for (int i1 = 0; i1 < chessArr[i].length; i1++) {
                if (chessArr[i][i1] == c) {
                    System.out.println("(" + i + "," + i1 + ")");
                    Position position = new Position(i, i1, null);
                    System.out.println(position);
                    return position;
                }
            }
        }
        return null;
    }

    private User setUser() {
        User user;
        //分配用户
        while (true) {
            Message<User> message = MessageProcessorCli.getMessage();
            if (message.getStatus().equals(ActionStatus.B)) {
                user = JsonUtil.toObject(message.getData(), User.class);
                if (user.equals(User.A)) {
                    this.flag = true;
                    setUser1("images/chess1.png");
                    setUser2("images/chess2.png");
                    this.user1Color = Color.BLUE;
                    this.user2Color = Color.RED;
                    //初始化棋盘数据
                    char[][] chessArr = this.chessPane.getChessArr();
                    chessArr[0][8] = 'A';
                    chessArr[18][6] = 'B';
                    this.u1 = 'A';
                    this.u2 = 'B';
                } else if (user.equals(User.B)) {
                    setUser1("images/chess2.png");
                    setUser2("images/chess1.png");
                    this.user2Color = Color.BLUE;
                    this.user1Color = Color.RED;
                    this.flag = false;
                    //初始化棋盘数据
                    char[][] chessArr = this.chessPane.getChessArr();
                    chessArr[0][8] = 'B';
                    chessArr[18][6] = 'A';
                    this.u2 = 'A';
                    this.u1 = 'B';
                }
                this.chess1Position = getChessPosition(this.u1);
                this.chess2Position = getChessPosition(this.u2);
                break;
            }
        }
        return user;
    }


    private void setUser2(String path) {
        double cellLen = chessPane.getCellLen();
        double chessPadding = chessPane.getChessPadding();
        double lineWidth = chessPane.getLineWidth();
        Image image = new Image(GlobalPathUtil.getResource(path).toExternalForm());
        double align = chessPane.getAlign();
        this.user2 = new ImageView(image);
        user2.setFitWidth(cellLen - 2 * chessPadding);
        user2.setFitHeight(cellLen - 2 * chessPadding);
        user2.setLayoutX(3 * cellLen + chessPadding + lineWidth / 4 + align);
        user2.setLayoutY(9 * cellLen + chessPadding + lineWidth / 4 + align);
        user2.setCache(true);
        user2.setCacheHint(CacheHint.SPEED);
        chessPane.getChildren().add(user2);

    }


    private void setUser1(String path) {
        double cellLen = chessPane.getCellLen();
        double chessPadding = chessPane.getChessPadding();
        double lineWidth = chessPane.getLineWidth();
        double align = chessPane.getAlign();
        Image image = new Image(GlobalPathUtil.getResource(path).toExternalForm());
        user1 = new ImageView(image);
        user1.setFitWidth(cellLen - 2 * chessPadding);
        user1.setFitHeight(cellLen - 2 * chessPadding);
        user1.setLayoutX(4 * cellLen + chessPadding + lineWidth / 4 + align);
        user1.setLayoutY(chessPadding + lineWidth / 4 + align);
        user1.setCache(true);
        user1.setCacheHint(CacheHint.SPEED);
        chessPane.getChildren().add(user1);
    }

}
