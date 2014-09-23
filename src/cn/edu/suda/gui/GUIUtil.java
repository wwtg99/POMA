/*
 * <>
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

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 *
 * @author Wentao Wu
 */
public class GUIUtil {

    public static void switchPane(final Pane from, final Pane to){
        if(from == to) return;
        //hide prevous
        if(from != null){
            FadeTransition ft = new FadeTransition(Duration.millis(500), from);
            ft.setFromValue(1.0);
            ft.setToValue(0);
            ft.setCycleCount(1);
            ft.play();
            ft.setOnFinished(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {                    
                    from.setVisible(false);                   
                }          
            });
        }
        //show next
        if(to != null){
            to.setVisible(true);
            FadeTransition ft = new FadeTransition(Duration.millis(500), to);
            ft.setFromValue(0);
            ft.setToValue(1.0);
            ft.setCycleCount(1);
            ft.play();
        }       
    }
    
    public static void scaleSwitch(final Pane p1, final Pane p2, double duration){
        ScaleTransition st1 = new ScaleTransition(Duration.millis(duration));
        st1.setFromX(1);
        st1.setFromY(1);
        st1.setToX(1.5f);
        st1.setToY(1.5f);
        st1.setCycleCount(1);
        
        FadeTransition ft1 = new FadeTransition(Duration.millis(duration));
        ft1.setFromValue(1.0);
        ft1.setToValue(0);
        ft1.setCycleCount(1);
        ft1.setOnFinished(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                p1.setVisible(false);
            }
            
        });
        ParallelTransition plt1 = new ParallelTransition(p1, st1, ft1);
               
        p2.setVisible(true);
        ScaleTransition st2 = new ScaleTransition(Duration.millis(duration));
        st2.setFromX(1.5f);
        st2.setFromY(1.5f);
        st2.setToX(1);
        st2.setToY(1);
        st2.setCycleCount(1);
        
        FadeTransition ft2 = new FadeTransition(Duration.millis(duration));
        ft2.setFromValue(0);
        ft2.setToValue(1);
        ft2.setCycleCount(1);
       
        ParallelTransition plt2 = new ParallelTransition(p2, st2, ft2);
        
        SequentialTransition seqT = new SequentialTransition (plt1, plt2);
        seqT.play();
    }
    
    public static void scaleAndHide(final Pane pane, double duration){
        ScaleTransition st = new ScaleTransition(Duration.millis(duration));
        st.setFromX(1);
        st.setFromY(1);
        st.setToX(1.5f);
        st.setToY(1.5f);
        st.setCycleCount(1);
        
        FadeTransition ft = new FadeTransition(Duration.millis(duration));
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setCycleCount(1);
        ft.setOnFinished(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                pane.setVisible(false);
            }
            
        });
        
        ParallelTransition plt = new ParallelTransition(pane, st, ft);
        plt.play();
    }
    
    public static void scaleAndShow(final Pane pane, double duration){
        ScaleTransition st = new ScaleTransition(Duration.millis(duration));
        st.setFromX(1.5f);
        st.setFromY(1.5f);
        st.setToX(1);
        st.setToY(1);
        st.setCycleCount(1);
        
        FadeTransition ft = new FadeTransition(Duration.millis(duration));
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setOnFinished(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                pane.setVisible(true);
            }
            
        });
        
        ParallelTransition plt = new ParallelTransition(pane, st, ft);
        plt.play();
    }
}
