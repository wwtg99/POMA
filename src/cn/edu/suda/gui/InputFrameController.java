/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.suda.gui;

import cn.edu.suda.core.Manager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Administrator
 */
public class InputFrameController implements Initializable {

    private Pane[] paneSteps;
    private int currentStep = 0;
    private Stage stage;
    private int testMethod = 0;
    private int ptype = 0;
    private String interfile = "";
    
    @FXML private TextField tf_mirna, tf_gene, tf_inter, tf_g1n, tf_g2n, tf_r1n, tf_r2n, tf_lsossR, tf_corR;
    @FXML private Button bt_pre, bt_next;
    @FXML private Pane pane_s1, pane_s2, pane_s3, pane_s4, pane_s5, pane_s6;
    @FXML private TableView rnaTable, geneTable;
    @FXML private CheckBox cb_rn1, cb_cn1, cb_rn2, cb_cn2;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        paneSteps = new Pane[]{pane_s1, pane_s2, pane_s3, pane_s4, pane_s5, pane_s6};
        Manager.getInstance().clearParams();
        Manager.getInstance().clearResults();
        Manager.getInstance().putParam(Manager.Keyword.Comfirm_Input, "0");
    }    
    
    public void setStage(Stage stage){
        this.stage = stage;
    }
    
    @FXML void changeProcessType(ActionEvent e){
        if(e.getSource() instanceof RadioButton){
            switch(((RadioButton)e.getSource()).getText()){
                case "Both Gene & miRNA": ptype = 0;break;
                case "Both Gene & miRNA without correlation": ptype = 1;break;
                case "Only Gene expression data": ptype = 2;break;
                case "Only miRNA expression data": ptype = 3;break;
            }
        }
    }
    
    @FXML void browse(ActionEvent e){
        if(e.getSource() instanceof Button){
            switch(((Button)e.getSource()).getText()){
                case "mirna": tf_mirna.setText(showOpenDialog());break;
                case "gene": tf_gene.setText(showOpenDialog());break;
                case "interaction": tf_inter.setText(showOpenDialog());break;
            }
        }
    }
    
    @FXML void ButtonAction(ActionEvent e){
        if(e.getSource() instanceof Button){
            switch(((Button)e.getSource()).getText()){
                case "Previous": previousStep();break;
                case "Next": nextStep();break;
                case "Finish": finish();break;
                case "Cancel": cancel();break;
            }
        }
    }
    
    @FXML void changeTestMethod(ActionEvent e){
        if(e.getSource() instanceof RadioButton){
            switch(((RadioButton)e.getSource()).getText()){
                case "T Test": testMethod = 0;break;
                case "LSOSS": testMethod = 1;break;
            }
        }
    }
    
    @FXML void changeRNAPreview(ActionEvent e){
        if(e.getSource() instanceof CheckBox){           
            refreshRNATable(cb_rn1.isSelected(), cb_cn1.isSelected());
        }
    }
    
    @FXML void changeGenePreview(ActionEvent e){
        if(e.getSource() instanceof CheckBox){
            refreshGeneTable(cb_rn2.isSelected(), cb_cn2.isSelected());
        }
    }
    
    @FXML void changeInter(ActionEvent e){
        if(e.getSource() instanceof RadioButton){
            switch(((RadioButton)e.getSource()).getText()){
                case "Default": tf_inter.setDisable(true);interfile = Manager.InterFile;break;
                case "Custom": tf_inter.setDisable(false);interfile = "";break;
            }
        }
    }
    
    private void nextStep(){
        if(currentStep < 0 && currentStep >= paneSteps.length){
            return;
        }
        if(check(currentStep)){
            GUIUtil.switchPane(paneSteps[currentStep], paneSteps[++currentStep]);
            if(currentStep > 0){
                bt_pre.setVisible(true);
            }
            if(currentStep == paneSteps.length - 1){
                bt_next.setText("Finish");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Invalid Input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void previousStep(){
        if(currentStep < 0 && currentStep >= paneSteps.length){
            return;
        }
        GUIUtil.switchPane(paneSteps[currentStep], paneSteps[--currentStep]);
        if(currentStep == 0){
            bt_pre.setVisible(false);
        }   
        bt_next.setText("Next");       
    }
    
    private void finish(){        
        Manager.getInstance().putParam(Manager.Keyword.Process_Type, ptype + "");
        Manager.getInstance().putParam(Manager.Keyword.Input_Gene, tf_gene.getText().trim());
        Manager.getInstance().putParam(Manager.Keyword.Input_miRNA, tf_mirna.getText().trim());
        Manager.getInstance().putParam(Manager.Keyword.GeneGroup1Num, tf_g1n.getText().trim());
        Manager.getInstance().putParam(Manager.Keyword.GeneGroup2Num, tf_g2n.getText().trim());
        Manager.getInstance().putParam(Manager.Keyword.RNAGroup1Num, tf_r1n.getText().trim());
        Manager.getInstance().putParam(Manager.Keyword.RNAGroup2Num, tf_r2n.getText().trim());
        Manager.getInstance().putParam(Manager.Keyword.Test_Method, testMethod + "");
        if(interfile.isEmpty() && !tf_inter.getText().trim().isEmpty()){
            interfile = tf_inter.getText().trim();
        }else{
            interfile = Manager.InterFile;
        }
        Manager.getInstance().putParam(Manager.Keyword.Input_interaction, interfile);
        //
        Manager.getInstance().putParam(Manager.Keyword.Gene_hasRowname, String.valueOf(cb_rn2.isSelected()));
        Manager.getInstance().putParam(Manager.Keyword.Gene_hasColname, String.valueOf(cb_cn2.isSelected()));
        Manager.getInstance().putParam(Manager.Keyword.Gene_hasName, "true");
        Manager.getInstance().putParam(Manager.Keyword.miRNA_hasRowname, String.valueOf(cb_rn1.isSelected()));
        Manager.getInstance().putParam(Manager.Keyword.miRNA_hasColname, String.valueOf(cb_cn1.isSelected()));
        Manager.getInstance().putParam(Manager.Keyword.miRNA_hasName, "true");
        Manager.getInstance().putParam(Manager.Keyword.Inter_hasRowname, "false");
        Manager.getInstance().putParam(Manager.Keyword.Inter_hasColname, "false");
        Manager.getInstance().putParam(Manager.Keyword.Inter_hasName, "false");
        
        if((tf_lsossR.getText().trim().isEmpty())){
            Manager.getInstance().putParam(Manager.Keyword.LSOSS_R, "0");
        }else{
            Manager.getInstance().putParam(Manager.Keyword.LSOSS_R, tf_lsossR.getText().trim());
        }
        if(tf_corR.getText().trim().isEmpty()){
            Manager.getInstance().putParam(Manager.Keyword.cor_R, "-0.6");
        }else{
            Manager.getInstance().putParam(Manager.Keyword.cor_R, tf_corR.getText().trim());
        }
        Manager.getInstance().putParam(Manager.Keyword.Comfirm_Input, "1");
        cancel();     
    }
    
    private boolean check(int step){
        switch(step){
            case 1:             
                if(tf_mirna.getText().isEmpty() && tf_gene.getText().isEmpty()){
                    return false;
                }else{
                    refreshRNATable(cb_rn1.isSelected(), cb_cn1.isSelected());
                    refreshGeneTable(cb_rn2.isSelected(), cb_cn2.isSelected());
                    return true;
                }
            case 3: 
                if(tf_g1n.getText().isEmpty() || tf_g2n.getText().isEmpty() || tf_r1n.getText().isEmpty() || tf_r2n.getText().isEmpty()){
                    return false;
                }else{
                    return true;
                }
            default: return true;
        }
    }
    
    private void cancel(){
        tf_mirna.setText("");
        tf_gene.setText("");
        tf_inter.setText("");
        bt_next.setText("Next");
        bt_pre.setVisible(false);
        stage.close();
    }
    
    private String showOpenDialog(){
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(null);
        if(f != null){
            return f.getAbsolutePath();
        }else{
            return "";
        }
    }
    
    private void refreshRNATable(boolean rowname, boolean colname){
        String file = tf_mirna.getText().trim();
        if(file.isEmpty()){
            return;
        }
        preview(rnaTable, rowname, colname, getFirstThreeLines(file));
    }
    
    private void refreshGeneTable(boolean rowname, boolean colname){
        String file = tf_gene.getText().trim();
        if(file.isEmpty()){
            return;
        }
        preview(geneTable, rowname, colname, getFirstThreeLines(file));
    }
    
    private List<String> getFirstThreeLines(String file){
        List<String> lines = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            int i = 0;
            String line;           
            while((line = br.readLine()) != null){
                line = line.trim();
                if(line.isEmpty()) continue;
                lines.add(line);
                i++;
                if(i > 2){
                    break;
                }
            }        
        } catch (FileNotFoundException ex) {
            
        } catch (IOException ex) {
            
        }
        return lines;
    }
    
    private void preview(TableView table, boolean rowname, boolean colname, List<String> lines){
        table.getColumns().clear();
        String[] line = lines.get(0).split("\t");
        int size = line.length;
        if(colname){           
            for(int j = 0; j < size; j++){
                final int n = j;
                TableColumn<ObservableList<String>, String> col  = new TableColumn<>(line[j]);
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>,String>, ObservableValue<String>>() {         

                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> p) {
                        return new SimpleStringProperty(p.getValue().get(n));
                    }
                });
                table.getColumns().add(col);
            }
        }else{
            for(int j = 0; j < size; j++){
                final int n = j;
                TableColumn<ObservableList<String>, String> col  = new TableColumn<>();
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>,String>, ObservableValue<String>>() {         

                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> p) {
                        return new SimpleStringProperty(p.getValue().get(n));
                    }
                });
                table.getColumns().add(col);
            }
        }
       
        ObservableList<ObservableList<String>> ss = FXCollections.observableArrayList();
        for(int i = 0; i < lines.size(); i++){           
            if(colname && i == 0) continue;
            String[] l = lines.get(i).split("\t");
            ObservableList<String> s = FXCollections.observableArrayList(l);
            ss.add(s);
        }
              
        table.setItems(ss);
    }
}
