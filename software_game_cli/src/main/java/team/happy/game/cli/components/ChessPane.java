package team.happy.game.cli.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * @author 冰冰
 * @date 2020/11/27 上午10:17
 */
public class ChessPane extends Pane {
    private Canvas canvas;
    private GraphicsContext gc;
    private int m;//行
    private int n;//竖
    private double margin;//棋盘扩充外延
    private double lineWidth;//线框，
    private double chessPadding;//棋子内边距
    private double width;
    private double height;
    private double cellLen;
    private double align;//棋盘距窗口边框左、上的距离
    private char[][] chessArr;


    public ChessPane() {
        this.align = 68;
        this.margin = 15;
        this.lineWidth = 10;
        this.chessPadding = 6;
        this.m = 10;
        this.n = 8;
        this.width = 448;
        this.height = 560;
        this.cellLen = 56;
        initChessArr();
        draw();
        getChildren().add(canvas);
    }

    void initChessArr() {
        int m=15;
        int n=19;
        this.chessArr=new char[n][m];
        for (int i = 0; i < n; i++) {
            for (int i1 = 0; i1 < m; i1++) {
                this.chessArr[i][i1]='0';
            }
        }
    }

    /**
     * 绘制棋盘
     */
    public void draw() {
        canvas = new Canvas(700, 700);
        this.gc = canvas.getGraphicsContext2D();

        double cell = cellLen;

        //填充棋盘颜色
        gc.setFill(Color.BURLYWOOD);
        gc.fillRect(align - margin, align - margin, width + margin * 2, height + margin * 2);
        gc.setLineWidth(lineWidth);
        for (int y = 1; y < m; y++) {
            //画横线
            gc.strokeLine(align, y * cell + align, width + align, y * cell + align);
        }

        for (int x = 1; x < n; x++) {
            //画竖线
            gc.strokeLine(x * cell + align, align, x * cell + align, height + align);
        }
        //边框加粗
        gc.strokeRect(align, align, width, height);
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public double getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }

    public double getChessPadding() {
        return chessPadding;
    }

    public void setChessPadding(double chessPadding) {
        this.chessPadding = chessPadding;
    }


    @Override
    public void setWidth(double width) {
        this.width = width;
    }


    @Override
    public void setHeight(double height) {
        this.height = height;
    }

    public double getCellLen() {
        return cellLen;
    }

    public void setCellLen(double cellLen) {
        this.cellLen = cellLen;
    }

    public double getAlign() {
        return align;
    }

    public void setAlign(double align) {
        this.align = align;
    }

    public char[][] getChessArr() {
        return chessArr;
    }

    public void setChessArr(char[][] chessArr) {
        System.out.println("=================================");
        for (int i = 0; i < chessArr.length; i++) {
            System.out.println(chessArr[i]);
        }
        System.out.println("=================================");
        this.chessArr = chessArr;
    }
}
