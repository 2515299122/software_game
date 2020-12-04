package team.happy.game.cli.utils;

import com.sun.javafx.robot.impl.FXRobotHelper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import team.happy.game.cli.GameCliApplication;

import java.net.URL;

/**
 * @author 冰冰
 * @date 2020/11/19 下午7:16
 */
public class GlobalPathUtil {
    public static URL getResource(String path){
        URL resource = GameCliApplication.class.getResource("/" + path);
        return resource;
    }

    public static void changePath(String fileName){
        try{
            ObservableList<Stage> stage = FXRobotHelper.getStages();
            Scene scene = new Scene(FXMLLoader.load(getResource("fxml/"+fileName)));
            stage.get(0).setScene(scene);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("页面加载失败");
        }
    }

}
