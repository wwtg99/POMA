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

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author Wentao Wu
 */
public class WilcoxChartPane extends BorderPane{

    private double[] wilcox;
    private MainController controller;
    
    public WilcoxChartPane(double[] wilcox, MainController controller){
        this.wilcox = wilcox;
        this.controller = controller;
        init();
    }
    
    private void init(){
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        Button exp = new Button("Export");
        exp.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                controller.export(5);
            }
            
        });
        hbox.getChildren().add(exp);
        this.setBottom(hbox);
        
        ScrollPane sp = new ScrollPane();
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("NOD");
        yAxis.setLabel("P Value");
        
        final LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
                
        lineChart.setTitle("Wilcoxon Signed-rank Test");
                                
        XYChart.Series series = new XYChart.Series();
        series.setName("Wilcoxon Signed-rank Test");
        for(int i = 0; i < wilcox.length; i++){
            series.getData().add(new XYChart.Data<Number, Number>(i, wilcox[i]));
        }
        lineChart.getData().add(series);
        sp.setContent(lineChart);
        this.setCenter(sp);
    }
}
