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

import cn.edu.suda.core.AnalysisTask;
import cn.edu.suda.core.CorrelateTask;
import cn.edu.suda.core.LoadTask;
import cn.edu.suda.core.Manager;
import cn.edu.suda.core.OverlapTask;
import cn.edu.suda.core.TestTask;
import cn.edu.suda.core.network.Network;
import cn.edu.suda.core.stats.Pair;
import cn.edu.suda.utils.Utils;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Wentao Wu
 */
public class OverviewPane extends AbstractTabView{
    
    @FXML private VBox inputGeneBox, inputmiRNABox, interBox, dsGeneBox, dsmiRNABox, corBox, overlapBox, resBox;
    @FXML private Label testLabel, miRNATestLabel, geneTestLabel, corLabel, overlapLabel;
    
    public OverviewPane(String file){
        super(file);
    }
    
    @Override
    public void initController() {
        Manager m = Manager.getInstance();
        String geneN = "";
        if(m.getResult(Manager.Result.Gene_Num_All) != null){
            geneN = m.getResult(Manager.Result.Gene_Num_All).toString();
        }
        inputGeneBox.getChildren().add(new Label("Rows: " + geneN));
        String miRNAN = "";
        if(m.getResult(Manager.Result.MiRNA_Num_All) != null){
            miRNAN = m.getResult(Manager.Result.MiRNA_Num_All).toString();
        }
        inputmiRNABox.getChildren().add(new Label("Rows: "+ miRNAN));
        
        inputGeneBox.getChildren().add(new Label("Group1 Number: "+ m.getParam(Manager.Keyword.GeneGroup1Num)));
        inputGeneBox.getChildren().add(new Label("Group2 Number: "+ m.getParam(Manager.Keyword.GeneGroup2Num)));
        inputmiRNABox.getChildren().add(new Label("Group1 Number: "+ m.getParam(Manager.Keyword.RNAGroup1Num)));
        inputmiRNABox.getChildren().add(new Label("Group2 Number: "+ m.getParam(Manager.Keyword.RNAGroup2Num)));
        
        Button geneBt = new Button("Show Data");
        geneBt.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                controller.addTab(2);
            }
            
        });
        inputGeneBox.getChildren().add(geneBt);
        Button rnaBt = new Button("Show Data");
        rnaBt.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                controller.addTab(3);
            }
            
        });
        inputmiRNABox.getChildren().add(rnaBt);
        
        if(m.getParam(Manager.Keyword.Input_interaction).isEmpty()){
            interBox.getChildren().add(new Label("No interaction file"));
        }else{
            Button interBt = new Button("Show Data");
            interBt.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent t) {
                    controller.addTab(4);
                }

            });
            interBox.getChildren().add(interBt);
        }
        
        //differential
        String tgeneN = "";
        if(m.getResult(Manager.Result.Gene_Num_Select) != null){
            tgeneN = m.getResult(Manager.Result.Gene_Num_Select).toString();
        }
        geneTestLabel = new Label("Rows: " + tgeneN);
        dsGeneBox.getChildren().add(geneTestLabel);
        String tmiRNAN = "";
        if(m.getResult(Manager.Result.MiRNA_Num_Select) != null){
            tmiRNAN = m.getResult(Manager.Result.MiRNA_Num_Select).toString();
        }
        miRNATestLabel = new Label("Rows: "+ tmiRNAN);
        dsmiRNABox.getChildren().add(miRNATestLabel);
        
        Button tgeneBt = new Button("Show Data");
        tgeneBt.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                controller.addTab(5);
            }
            
        });
        dsGeneBox.getChildren().add(tgeneBt);
        Button exGeneBt = new Button("Export");
        exGeneBt.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                controller.export(0);
            }
            
        });
        dsGeneBox.getChildren().add(exGeneBt);
        
        Button trnaBt = new Button("Show Data");
        trnaBt.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                controller.addTab(6);
            }
            
        });
        dsmiRNABox.getChildren().add(trnaBt);
        Button exRNABt = new Button("Export");
        exRNABt.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                controller.export(1);
            }
            
        });
        dsmiRNABox.getChildren().add(exRNABt);
        
        //cor
        String corN = "";
        if(m.getResult(Manager.Result.Cor_Num) != null){
            corN = m.getResult(Manager.Result.Cor_Num).toString();
        }
        corLabel = new Label("Rows: " + corN);
        corBox.getChildren().add(corLabel);
        Button corBt = new Button("Show Data");
        corBt.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                controller.addTab(7);
            }
            
        });
        corBox.getChildren().add(corBt);
        Button exCorBt = new Button("Export");
        exCorBt.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                controller.export(2);
            }
            
        });
        corBox.getChildren().add(exCorBt);
        
        //overlap
        String olN = "";
        if(m.getResult(Manager.Result.Overlap_Num) != null){
            olN = m.getResult(Manager.Result.Overlap_Num).toString();
        }
        overlapLabel = new Label("Rows: " + olN);
        overlapBox.getChildren().add(overlapLabel);
        Button olBt = new Button("Show Data");
        olBt.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                controller.addTab(8);
            }
            
        });
        overlapBox.getChildren().add(olBt);
        Button exolBt = new Button("Export");
        exolBt.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                controller.export(3);
            }
            
        });
        overlapBox.getChildren().add(exolBt);
        
        //result
        Button anaBt = new Button("Do Analysis");
        anaBt.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                analyse();
            }
            
        });
        resBox.getChildren().add(anaBt);
        
        resBox.getChildren().add(new Label("Network"));
        Button netBt = new Button("Show Network");
        netBt.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                try {
                    //controller.addTab(13);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("NetworkViewer.fxml"));
                    Parent root = (Parent)loader.load(); 
                    Stage s = new Stage();
                    s.setTitle("POMA Network Viewer");
                    s.setScene(new Scene(root));
                    s.show();
                } catch (IOException ex) {
                    Logger.getLogger(OverviewPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        resBox.getChildren().add(netBt);
        
        resBox.getChildren().add(new Label("NOD"));
        Button nodBt = new Button("NOD Chart");
        nodBt.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                controller.addTab(9);
            }
            
        });
        resBox.getChildren().add(nodBt);
        
        resBox.getChildren().add(new Label("Wilcoxon Signed-rank Test"));
        Button wilBt = new Button("Wilcox Chart");
        wilBt.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                controller.addTab(10);
            }
            
        });
        resBox.getChildren().add(wilBt);
        
        resBox.getChildren().add(new Label("DAVID analysis"));
        Button davidBt = new Button("Show");
        davidBt.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent t) {
                controller.addTab(11);
            }
            
        });
        resBox.getChildren().add(davidBt);
    }

    @FXML void doTest(ActionEvent e){
        if(e.getSource() instanceof Hyperlink){
            String name = ((Hyperlink)e.getSource()).getText();
            switch(name){
                case "Do T Test": doTTest();break;
                case "Do LSOSS Test": doLSOSSTest();break;
            }
            refreshTest(name);
        }
    }
    
    @FXML void doCorTest(ActionEvent e){
        try {
            CorrelateTask task = new CorrelateTask();
            LoadingDialog ld = new LoadingDialog(this.controller.getStage(), task);
            new Thread(task).start();
            ld.show();
            int re = task.get();
            this.controller.processReturnValue(re);
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(OverviewPane.class.getName()).log(Level.SEVERE, null, ex);
        }
        Manager m = Manager.getInstance();
        String corN = "";
        if(m.getResult(Manager.Result.Cor_Num) != null){
            corN = m.getResult(Manager.Result.Cor_Num).toString();
        }
        corLabel.setText("Rows: " + corN);
    }
    
    @FXML void doOverlap(ActionEvent e){
        try {
            OverlapTask task = new OverlapTask();
            LoadingDialog ld = new LoadingDialog(this.controller.getStage(), task);
            new Thread(task).start();
            ld.show();
            int re = task.get();
            this.controller.processReturnValue(re);
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(OverviewPane.class.getName()).log(Level.SEVERE, null, ex);
        }
        Manager m = Manager.getInstance();
        String olN = "";
        if(m.getResult(Manager.Result.Overlap_Num) != null){
            olN = m.getResult(Manager.Result.Overlap_Num).toString();
        }
        overlapLabel.setText("Rows: " + olN);
    }
    
    private void refreshTest(String name){
        Manager m = Manager.getInstance();
        testLabel.setText("Processed by " + name.substring(3));
        String tgeneN = "";
        if(m.getResult(Manager.Result.Gene_Num_Select) != null){
            tgeneN = m.getResult(Manager.Result.Gene_Num_Select).toString();
        }
        geneTestLabel.setText("Rows: " + tgeneN);
        
        String tmiRNAN = "";
        if(m.getResult(Manager.Result.MiRNA_Num_Select) != null){
            tmiRNAN = m.getResult(Manager.Result.MiRNA_Num_Select).toString();
        }
        miRNATestLabel.setText("Rows: "+ tmiRNAN);
    }
    
    private void doTTest(){
        try {
            Manager.getInstance().putParam(Manager.Keyword.Test_Method, "0");
            TestTask task = new TestTask();
            LoadingDialog ld = new LoadingDialog(this.controller.getStage(), task);
            new Thread(task).start();
            ld.show();
            int re = task.get();
            this.controller.processReturnValue(re);
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(OverviewPane.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void doLSOSSTest(){
        try {
            Manager.getInstance().putParam(Manager.Keyword.Test_Method, "1");
            TestTask task = new TestTask();
            LoadingDialog ld = new LoadingDialog(this.controller.getStage(), task);
            new Thread(task).start();
            ld.show();
            int re = task.get();
            this.controller.processReturnValue(re);
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(OverviewPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void analyse(){
        try {
            AnalysisTask task = new AnalysisTask();
            LoadingDialog ld = new LoadingDialog(this.controller.getStage(), task);
            new Thread(task).start();
            ld.show();
            int re = task.get();
            this.controller.processReturnValue(re);
            JOptionPane.showMessageDialog(null, "Analysis finished!", "POMA", JOptionPane.INFORMATION_MESSAGE);
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(OverviewPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
