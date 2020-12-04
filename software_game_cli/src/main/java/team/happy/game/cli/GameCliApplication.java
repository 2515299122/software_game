package team.happy.game.cli;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author root
 */
public class GameCliApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/enter.fxml"));
        primaryStage.setTitle("步步为营");
        primaryStage.setScene(new Scene(root, 700, 700));
        primaryStage.show();
        primaryStage.setResizable(false);
        /*ChessPane chessPane = new ChessPane();

        Scene scene = new Scene(chessPane,700,700);

        primaryStage.setScene(scene);
        primaryStage.setTitle("五子棋");
        primaryStage.show();*/
    }


    public static void main(String[] args) {
        launch(args);
    }
}
