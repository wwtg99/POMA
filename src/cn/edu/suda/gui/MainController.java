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

import cn.edu.suda.core.LoadTask;
import cn.edu.suda.core.Manager;
import cn.edu.suda.core.stats.DataMatrix;
import cn.edu.suda.core.stats.Pair;
import cn.edu.suda.core.stats.PairUtils;
import cn.edu.suda.core.stats.StringMatrix;
import cn.edu.suda.core.stats.Triplet;
import cn.edu.suda.utils.FileUtils;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Wentao Wu
 */
public class MainController implements Initializable {

    private Stage stage = null;
    @FXML Pane welcomePane, resultPane;
    @FXML TabPane tabPane;
    @FXML Label lb_info, lb_version, lb_author, lb_log;
    @FXML GridPane startPane;
    @FXML VBox aboutPane;
    @FXML BorderPane logPane, docPane;
    @FXML TextArea ta_log;
    @FXML WebView web;
    @FXML MenuItem mi_overview, mi_nod, mi_wilcox, mi_david;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lb_info.setText(Manager.INFO);
        lb_version.setText(Manager.VERSION);
        lb_author.setText(Manager.AUTHOR);
        ta_log.setText(Manager.COPYRIGHT);
        
        loadDoc();
        
        //Manager.LOG.addHandler(new LogHandler(ta_log, lb_log));
        //Manager.LOG.info("Initialize successfully!");
    }    
    
    public void addTab(int type){
        Tab tab = TabBuilder.build(type, this);
        if(tab == null){
            return;
        }
        tab.setOnClosed(new EventHandler<Event>(){

            @Override
            public void handle(Event t) {
                if(tabPane.getTabs().size() == 0){
                    GUIUtil.switchPane(resultPane, welcomePane);
                }
            }
            
        });
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().selectLast();
    }
    
    public void showOverview(){
        //if(Manager.getInstance().containResult(Manager.Result.Analyzed) && (int)(Manager.getInstance().getResult(Manager.Result.Analyzed)) == 1){
            if(!resultPane.isVisible()){
                GUIUtil.switchPane(welcomePane, resultPane);
            }
            addTab(1);
        //}
    }
    
    public void showNODChart(){
        if(Manager.getInstance().containResult(Manager.Result.NOD)){
            if(!resultPane.isVisible()){
                GUIUtil.switchPane(welcomePane, resultPane);
            }
            addTab(9);
        }
    }
    
    public void showWilcoxChart(){
        if(Manager.getInstance().containResult(Manager.Result.WilcoxTest)){
            if(!resultPane.isVisible()){
                GUIUtil.switchPane(welcomePane, resultPane);
            }
            addTab(10);
        }
    }
    
    public void showDAVID(){
        if(Manager.getInstance().containResult(Manager.Result.NOD)){
            if(!resultPane.isVisible()){
                GUIUtil.switchPane(welcomePane, resultPane);
            }
            addTab(11);
        }
    }
    
    public void export(int type){
        Manager m = Manager.getInstance();
        switch(type){
            case 0://test gene matrix
                if(m.getResult(Manager.Result.Gene_Matrix) != null){
                    FileChooser fc = new FileChooser();
                    File f = fc.showSaveDialog(null);
                    if(f != null){
                        try {
                            FileUtils.writeFile(f.getAbsolutePath(), ((DataMatrix)m.getResult(Manager.Result.Gene_Matrix)).toString());
                        } catch (IOException ex) {
                            Manager.LOG.severe(ex.getMessage());
                        }
                    }
                }
                break;
            case 1://test miRNA matrix
                if(m.getResult(Manager.Result.MiRNA_Matrix) != null){
                    FileChooser fc = new FileChooser();
                    File f = fc.showSaveDialog(null);
                    if(f != null){
                        try {
                            FileUtils.writeFile(f.getAbsolutePath(), ((DataMatrix)m.getResult(Manager.Result.MiRNA_Matrix)).toString());
                        } catch (IOException ex) {
                            Manager.LOG.severe(ex.getMessage());
                        }
                    }
                }
                break;
            case 2://cor matrix
                if(m.getResult(Manager.Result.Cor_Matrix) != null){
                    FileChooser fc = new FileChooser();
                    File f = fc.showSaveDialog(null);
                    if(f != null){
                        try {
                            FileUtils.writeFile(f.getAbsolutePath(), ((StringMatrix)m.getResult(Manager.Result.Cor_Matrix)).toString());
                        } catch (IOException ex) {
                            Manager.LOG.severe(ex.getMessage());
                        }
                    }
                }
                break;
            case 3://overlap matrix
                if(m.getResult(Manager.Result.Overlap) != null){
                    FileChooser fc = new FileChooser();
                    File f = fc.showSaveDialog(null);
                    if(f != null){
                        try {
                            List<Pair> overlap = (List<Pair>)m.getResult(Manager.Result.Overlap);
                            FileUtils.writeFile(f.getAbsolutePath(), PairUtils.toString(overlap));
                        } catch (IOException ex) {
                            Manager.LOG.severe(ex.getMessage());
                        }
                    }
                }
                break;
            case 4://nod matrix
                if(m.getResult(Manager.Result.NOD) != null){
                    FileChooser fc = new FileChooser();
                    File f = fc.showSaveDialog(null);
                    if(f != null){
                        try {
                            List<Triplet> nod = (List<Triplet>)m.getResult(Manager.Result.NOD);
                            FileUtils.writeFile(f.getAbsolutePath(), PairUtils.TripletsToString(nod));
                        } catch (IOException ex) {
                            Manager.LOG.severe(ex.getMessage());
                        }
                    }
                }
                break;
            case 5://wilcox
                if(m.getResult(Manager.Result.WilcoxTest) != null){
                    FileChooser fc = new FileChooser();
                    File f = fc.showSaveDialog(null);
                    if(f != null){
                        try {
                            double[] wilcox = (double[])m.getResult(Manager.Result.WilcoxTest);
                            StringBuilder sb = new StringBuilder();
                            for(int i = 0; i < wilcox.length; i++){
                                sb.append(i).append("\t").append(wilcox[i]).append("\n");
                            }
                            FileUtils.writeFile(f.getAbsolutePath(), sb.toString());
                        } catch (IOException ex) {
                            Manager.LOG.severe(ex.getMessage());
                        }
                    }
                }
                break;
        }
    }
      
    public void setStage(Stage stage){
        this.stage = stage;
    }
    
    public Stage getStage(){
        return stage;
    }
    
    public void exit(){
        stage.close();
    }
    
    public void showDocument(){
        try {
            Desktop.getDesktop().open(new File(Manager.DOCFILE));
        } catch (IOException ex) {
            Manager.LOG.severe(ex.getMessage());
        }
    }
    
    public void showAbout(){
        if(resultPane.isVisible()){         
            welcomePane.setVisible(true);
            resultPane.setVisible(false);
            welcomePane.setOpacity(1);
        }
        startPane.setVisible(true);
        aboutPane.setVisible(false);
        logPane.setVisible(false);
        docPane.setVisible(false);
        
        GUIUtil.scaleSwitch(startPane, aboutPane, 800);
    }
    
    public void processReturnValue(int re){
        String msg = "";
        switch(re){
            case 1: msg = "Interaction file missing!";break;
            case 2: msg = "Overlap information missing!";break;
            case 3: msg = "There is no need for correlation";break;
            case 4: msg = "Please do correlate first!";break;
        }
        if(!msg.isEmpty()){
            JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @FXML void menuAction(ActionEvent e){
        if(e.getSource() instanceof MenuItem){
            switch(((MenuItem)e.getSource()).getText()){
                case "Import Wizard...":showInputFrame();break;
                case "Exit":exit();break;
                case "Overview": showOverview();break;
                case "NOD Chart": showNODChart();break;
                case "Wilcox Chart": showWilcoxChart();break;
                case "DAVID analysis": showDAVID();break;
                case "Document": showDocument();break;
                case "About": showAbout();break;
            }
        }       
    }
    
    @FXML void aboutAction(ActionEvent e){
        GUIUtil.scaleSwitch(startPane, aboutPane, 800);
    }
    
    @FXML void aboutOkAction(ActionEvent e){
        GUIUtil.scaleSwitch(aboutPane, startPane, 800);
    }
    
    @FXML void consoleAction(ActionEvent e){
        GUIUtil.scaleSwitch(startPane, logPane, 800);
    }
    
    @FXML void consoleReturnAction(ActionEvent e){
        GUIUtil.scaleSwitch(logPane, startPane, 800);
    }
    
    @FXML void startAction(ActionEvent e){
        GUIUtil.scaleSwitch(startPane, docPane, 800);
    }
    
    @FXML void startReturnAction(ActionEvent e){
        GUIUtil.scaleSwitch(docPane, startPane, 800);
    }
    
    @FXML void docImportWizard(ActionEvent e){
        showInputFrame();
    }
    
    @FXML void docExampleData(ActionEvent e){
        loadExample();
    }
    
    private void showInputFrame(){
        try {        
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InputFrame.fxml"));
            Parent root = (Parent)loader.load(); 
            Stage s = new Stage();
            s.initModality(Modality.APPLICATION_MODAL);
            InputFrameController controller = (InputFrameController)loader.getController();
            controller.setStage(s);
            s.setTitle("POMA");
            s.setScene(new Scene(root));
            s.showAndWait();
            
            if(Manager.getInstance().getParam(Manager.Keyword.Comfirm_Input).equals("1")){
                //ProcessTask task = new ProcessTask();
                LoadTask task = new LoadTask();
                LoadingDialog ld = new LoadingDialog(stage, task);
                new Thread(task).start();
                ld.show();
                int re = task.get();
                processReturnValue(re);
                showResult();
                GUIUtil.switchPane(welcomePane, resultPane);
            }
        } catch (FileNotFoundException ex) {
            Manager.LOG.severe(ex.getMessage());
        } catch (IOException ex) {
            Manager.LOG.severe(ex.getMessage());
        } catch (Exception e){
            Manager.LOG.severe(e.getMessage());
        }    
    }
    
    private void loadDoc(){
        String helpdoc = "";
        try(BufferedReader bw = new BufferedReader(new FileReader(Manager.DOCFILE))){
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = bw.readLine()) != null){
                sb.append(line);
            }
            helpdoc = sb.toString();
        } catch (IOException ex) {
            helpdoc = "<h1>Document file do not exist!</h1>";
        }
        
        web.getEngine().loadContent(helpdoc);
    }
    
    private void showResult(){
        mi_overview.setDisable(false);
        mi_nod.setDisable(false);
        mi_wilcox.setDisable(false);
        mi_david.setDisable(false);
        addTab(1);
    }
    
    private void loadExample(){
        Manager.getInstance().loadExample();
        if(Manager.getInstance().getParam(Manager.Keyword.Comfirm_Input).equals("1")){
            LoadTask task = new LoadTask();
            LoadingDialog ld = new LoadingDialog(stage, task);
            new Thread(task).start();
            ld.show();
            int re = task.getValue();
            processReturnValue(re);
            showResult();
            GUIUtil.switchPane(welcomePane, resultPane);
         }
    }
    
    
    
}
