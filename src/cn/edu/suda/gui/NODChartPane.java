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

import cn.edu.suda.core.stats.Triplet;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Wentao Wu
 */
public class NODChartPane extends BorderPane {

    private List<Triplet> nods;
    private MainController controller;
    private ListView<String> nodList, geneList;
    
    public NODChartPane(List<Triplet> nods, MainController controller){
        this.nods = nods;
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
                controller.export(4);
            }
            
        });
        hbox.getChildren().add(exp);
        this.setBottom(hbox);
        
        ScrollPane sp = new ScrollPane();
        final CategoryAxis yAxis = new CategoryAxis();
        final NumberAxis xAxis = new NumberAxis();
        final BarChart<Number, String> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("NOD");
        bc.setCategoryGap(10);
        xAxis.setLabel("NOD Value");
        xAxis.setTickLabelRotation(90);
        yAxis.setLabel("miRNA");
        XYChart.Series<Number, String> series1 = new XYChart.Series();
        series1.setName("miRNA NOD");
        for(Triplet p:nods){
            series1.getData().add(new XYChart.Data<Number, String>(Integer.parseInt(p.getT2()), p.getT1()));
        }
        bc.getData().add(series1);
        bc.setPrefHeight(30 * nods.size());
        sp.setContent(bc);
        
        VBox vbox = new VBox();
        vbox.setPrefWidth(150);
        vbox.setSpacing(10);
        vbox.getChildren().add(new Label("miRNA"));
        ObservableList<String> miRNA = FXCollections.observableArrayList();
        for(Triplet t:nods){
            miRNA.add(t.getT1());
        }
        nodList = new ListView<>(miRNA);
        vbox.getChildren().add(nodList);
        vbox.getChildren().add(new Label("Gene List:"));
        geneList = new ListView<>();
        vbox.getChildren().add(geneList);
        nodList.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
                    int index = nodList.getSelectionModel().getSelectedIndex();
                    String[] genes = nods.get(index).getT3().split("\t");
                    geneList.getItems().setAll(genes);
                }
            });
        
        BorderPane bp = new BorderPane();
        bp.setCenter(sp);
        bp.setLeft(vbox);
        this.setCenter(bp);
    }
}
