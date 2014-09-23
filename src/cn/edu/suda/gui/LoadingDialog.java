/*
 * <Amino Acid Network Worker (AANW) is a useful AA network construction tool.>
 * Copyright (C) <2013>  <Wentao Wu>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cn.edu.suda.gui;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Wentao Wu
 */
public class LoadingDialog {
    
    private final Stage stage;
    private Task task;

    public LoadingDialog(Stage owner, Task task){
        this.task = task;
        stage = new Stage(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        Group root = new Group();
        root.setOpacity(0.5);
        Scene scene = new Scene(root, 300, 50);
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefWidth(300);
        Label l = new Label();
        l.textProperty().bind(task.messageProperty());
        ProgressBar bar = new ProgressBar();
        bar.setPrefWidth(200);
        bar.progressProperty().bind(task.progressProperty());
        vbox.getChildren().add(l);
        vbox.getChildren().add(bar);
        root.getChildren().add(vbox);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
    }
    
    public void show(){      
        Task t = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                while(true){
                    if(task.isDone()){

                        break;
                    }
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                close();
            }
            

        };
        new Thread(t).start();
        stage.showAndWait();
    }

    public void close(){
        stage.close();
    }
}
