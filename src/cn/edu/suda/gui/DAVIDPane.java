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

import cn.edu.suda.core.Manager;
import cn.edu.suda.core.stats.Pair;
import cn.edu.suda.core.stats.Triplet;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

/**
 *
 * @author Wentao Wu
 */
public class DAVIDPane extends AbstractTabView{

    @FXML ComboBox cb_mirna;
    @FXML ListView<String> lv_genes;
    @FXML TextField tf_addgene;
    
    public static final String TYPE = "GENE_SYMBOL", TOOL = "annotationReport", ANNOT = "KEGG_PATHWAY";
    
    public DAVIDPane(String file){
        super(file);
    }
    
    @Override
    public void initController() {
        Manager m = Manager.getInstance();
        if(m.getResult(Manager.Result.NOD) == null){
            return;
        }
        final List<Triplet> nods = (List<Triplet>)m.getResult(Manager.Result.NOD);
        String[] mirnas = new String[nods.size()];
        int i = 0;
        for(Triplet t:nods){
            mirnas[i++] = t.getT1();
        }
        cb_mirna.setItems(FXCollections.observableArrayList(mirnas));
        cb_mirna.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                int index = cb_mirna.getSelectionModel().getSelectedIndex();
                String[] genes = nods.get(index).getT3().split("\t");
                lv_genes.getItems().setAll(genes);
            }
            
        });
        
    }
    
    @FXML void addGene(ActionEvent e){
        String genename = tf_addgene.getText().trim();
        if(!genename.isEmpty()){
            lv_genes.getItems().add(genename);
            tf_addgene.setText("");
        }
    }
    
    @FXML void removeGene(ActionEvent e){
        int index = lv_genes.getSelectionModel().getSelectedIndex();
        if(index != -1){
            lv_genes.getItems().remove(index);
        }
    }
    
    @FXML void clearGene(ActionEvent e){
        lv_genes.getItems().clear();
    }
    
    @FXML void changeGenes(ActionEvent e){
        if(e.getSource() instanceof RadioButton){
            String name = ((RadioButton)e.getSource()).getText();
            switch(name){
                case "All unique genes": String[] genes = getUniqueGenes(cb_mirna.getSelectionModel().getSelectedItem().toString()); lv_genes.getItems().setAll(genes); break;
                case "All genes":String[] genes2 = getAllGenes(cb_mirna.getSelectionModel().getSelectedItem().toString()); lv_genes.getItems().setAll(genes2); break;
            }
        }
    }
    
    @FXML void goDAVID(ActionEvent e){
        StringBuilder geneList = new StringBuilder();
        for(Object o:lv_genes.getItems()){
            geneList.append(o.toString()).append(",");
        }
        String url = "http://david.abcc.ncifcrf.gov/api.jsp?type=" + TYPE + "&tool=" + TOOL + "&annot=" + ANNOT + "&ids=" + geneList.toString();
        Manager.getInstance().putResult(Manager.Result.DAVID_URL, url);
        this.controller.addTab(12);
        
//        try {
//            Desktop.getDesktop().browse(new URI(url));
//        } catch (IOException | URISyntaxException ex) {
//            Logger.getLogger(DAVIDPane.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private String[] getUniqueGenes(String mirna){
        Manager m = Manager.getInstance();
        if(m.getResult(Manager.Result.NOD) == null){
            return new String[0];
        }
        final List<Triplet> nods = (List<Triplet>)m.getResult(Manager.Result.NOD);
        for(Triplet t:nods){
            if(mirna.equals(t.getT1())){
                return t.getT3().split("\t");
            }
        }
        return new String[0];
    }
    
    private String[] getAllGenes(String mirna){
        Manager m = Manager.getInstance();
        if(m.getResult(Manager.Result.Overlap) == null){
            return new String[0];
        }
        List<Pair> overlap = (List<Pair>)m.getResult(Manager.Result.Overlap);
        List<String> genes = new ArrayList<>();
        for(Pair p:overlap){
            if(p.getP1().equals(mirna)){
                genes.add(p.getP2());
            }
        }
        String[] ss = new String[genes.size()];
        ss = genes.toArray(ss);
        return ss;
    }
}
