/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.suda.gui;

import cn.edu.suda.core.Manager;
import cn.edu.suda.core.network.MatrixArranger;
import cn.edu.suda.core.network.Network;
import cn.edu.suda.core.network.NetworkView;
import cn.edu.suda.core.network.RandomArranger;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class NetworkViewerController implements Initializable {

    @FXML ChoiceBox arrangeChoice;
    @FXML VBox settingBox;
    @FXML ScrollPane sp;
    @FXML ColorPicker cp1, cp2;
    @FXML Slider zoomS;
    private NetworkView net = null;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        arrangeChoice.getItems().clear();
        arrangeChoice.getItems().addAll("Random", "Matrix");       
        if(Manager.getInstance().containResult(Manager.Result.Network)){
            loadNetwork((Network) Manager.getInstance().getResult(Manager.Result.Network));
            cp1.setValue(net.getGeneColor());
            cp2.setValue(net.getMiRNAColor());
        }   
    }    
    
    @FXML void showVBox(MouseEvent e){
        FadeTransition ft = new FadeTransition(Duration.millis(500), settingBox);
        ft.setFromValue(0.3f);
        ft.setToValue(1);
        ft.play();
    }
    
    @FXML void hideVBox(MouseEvent e){
        FadeTransition ft = new FadeTransition(Duration.millis(500), settingBox);
        ft.setFromValue(1);
        ft.setToValue(0.3);
        ft.play();
    }
    
    @FXML void arrange(ActionEvent e){
        int n = arrangeChoice.getSelectionModel().getSelectedIndex();
        switch(n){
            case 0: RandomArranger.setAnimationOn(net.isAnimationOn());net.setArranger(RandomArranger.getInstance(800, 600));break;
            case 1: MatrixArranger.setAnimationOn(net.isAnimationOn());net.setArranger(MatrixArranger.getInstance());break;
        }
    }
    
    @FXML void zoom(MouseEvent e){
        float v = (float)zoomS.getValue();
        net.setZoom(v);
    }
    
    @FXML void changeAnimation(ActionEvent e){
        if(e.getSource() instanceof ToggleButton){
            ToggleButton tb = (ToggleButton)e.getSource();
            if(net != null){
                net.setAnimationOn(tb.isSelected());
            }
        }
    }
    
    @FXML void changeColor(ActionEvent e){
        if(e.getSource() == cp1){
            net.setGeneColor(cp1.getValue());
        }else if(e.getSource() == cp2){
            net.setMiRNAColor(cp2.getValue());
        }
        net.changeColor();
    }
    
    private void loadNetwork(Network net){
        this.net = new NetworkView();
        this.net.loadNetwork(net);
        sp.setContent(this.net);
    }
    
}
