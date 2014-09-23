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
import cn.edu.suda.core.network.Network;
import cn.edu.suda.core.network.NetworkView;
import cn.edu.suda.core.stats.DataMatrix;
import cn.edu.suda.core.stats.MatrixBuilder;
import cn.edu.suda.core.stats.Pair;
import cn.edu.suda.core.stats.StringMatrix;
import cn.edu.suda.core.stats.Triplet;
import cn.edu.suda.utils.Utils;
import java.util.List;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;

/**
 *
 * @author Wentao Wu
 */
public class TabBuilder {

    public static final String[] fxmlFile = {"OverviewPane.fxml", "DAVIDPane.fxml"};
    
    public static Tab build(int type, MainController controller){
        Manager m = Manager.getInstance();
        Tab tab = new Tab();
        switch(type){            
            case 1://overview
                OverviewPane op = new OverviewPane(fxmlFile[0]);
                op.addController(controller);
                op.initController();
                tab.setText("Overview");
                tab.setContent(op);
                break;
            case 2: //gene data table
                try {
                    DataMatrix geneM = MatrixBuilder.readDouble(m.getParam(Manager.Keyword.Input_Gene), 
                            Boolean.parseBoolean(m.getParam(Manager.Keyword.Gene_hasRowname)), 
                            Boolean.parseBoolean(m.getParam(Manager.Keyword.Gene_hasColname)), 
                            Boolean.parseBoolean(m.getParam(Manager.Keyword.Gene_hasName)));
                    TableView table = TableBuilder.getTable(geneM);
                    tab.setText("Gene Data");
                    tab.setContent(table);
                } catch (Exception ex) {
                    ex.getMessage();
                }
                break;
            case 3://miRNA data table
                try {
                    DataMatrix miRNAM = MatrixBuilder.readDouble(m.getParam(Manager.Keyword.Input_miRNA), 
                            Boolean.parseBoolean(m.getParam(Manager.Keyword.miRNA_hasRowname)), 
                            Boolean.parseBoolean(m.getParam(Manager.Keyword.miRNA_hasColname)), 
                            Boolean.parseBoolean(m.getParam(Manager.Keyword.miRNA_hasName)));
                    TableView table = TableBuilder.getTable(miRNAM);
                    tab.setText("miRNA Data");
                    tab.setContent(table);
                } catch (Exception ex) {
                    ex.getMessage();
                }
                break;
            case 4://interaction data table
                try {
                    if(m.getParam(Manager.Keyword.Input_interaction).isEmpty()){
                        return null;
                    }
                    StringMatrix interM = MatrixBuilder.readString(m.getParam(Manager.Keyword.Input_interaction), 
                            Boolean.parseBoolean(m.getParam(Manager.Keyword.Inter_hasRowname)), 
                            Boolean.parseBoolean(m.getParam(Manager.Keyword.Inter_hasColname)), 
                            Boolean.parseBoolean(m.getParam(Manager.Keyword.Inter_hasName)));
                    TableView table = TableBuilder.getTable(interM);
                    tab.setText("Interaction Data");
                    tab.setContent(table);
                } catch (Exception ex) {
                    ex.getMessage();
                }
                break;
            case 5://gene test table      
                if(m.getResult(Manager.Result.Gene_Matrix) == null){
                    return null;
                }
                DataMatrix geneM = (DataMatrix)m.getResult(Manager.Result.Gene_Matrix);
                TableView table = TableBuilder.getTable(geneM);
                tab.setText("Gene Test Data");
                tab.setContent(table);
                break;
            case 6://miRNA test table
                if(m.getResult(Manager.Result.MiRNA_Matrix) == null){
                    return null;
                }
                DataMatrix miRNAM = (DataMatrix)m.getResult(Manager.Result.MiRNA_Matrix);
                TableView table2 = TableBuilder.getTable(miRNAM);
                tab.setText("miRNA Test Data");
                tab.setContent(table2);            
                break;
            case 7://cor table
                if(m.getResult(Manager.Result.Cor_Matrix) == null){
                    return null;
                }
                StringMatrix corM = (StringMatrix)m.getResult(Manager.Result.Cor_Matrix);
                TableView table3 = TableBuilder.getTable(corM);
                tab.setText("Correlation Data");
                tab.setContent(table3);            
                break;
            case 8://overlap table
                if(m.getResult(Manager.Result.Overlap) == null){
                    return null;
                }
                List<Pair> overlap = (List<Pair>)m.getResult(Manager.Result.Overlap);
                TableView table4 = TableBuilder.getTable(overlap);
                tab.setText("Overlap Data");
                tab.setContent(table4);            
                break;
            case 9://nod chart
                if(m.getResult(Manager.Result.NOD) == null){
                    return null;
                }
                List<Triplet> nods = (List<Triplet>)m.getResult(Manager.Result.NOD);
                tab.setText("NOD");
                tab.setContent(new NODChartPane(nods, controller));
                break;
            case 10://wilcox chart
                if(m.getResult(Manager.Result.WilcoxTest) == null){
                    return null;
                }
                double[] wilcox = (double[])m.getResult(Manager.Result.WilcoxTest);
                tab.setText("Wilcoxon Signed-rank Test");
                tab.setContent(new WilcoxChartPane(wilcox, controller));
                break;
            case 11://DAVID pane
                DAVIDPane dp = new DAVIDPane(fxmlFile[1]);
                dp.addController(controller);
                dp.initController();
                tab.setText("DAVID Analysis");
                tab.setContent(dp);
                break;
            case 12://DAVID web
                if(m.getResult(Manager.Result.DAVID_URL) == null){
                    return null;
                }
                String url = m.getResult(Manager.Result.DAVID_URL).toString();
                tab.setText("DAVID View");
                tab.setContent(new DAVIDWebPane(url, controller));
                break;
            case 13://network viewer
                NetworkView netView = new NetworkView();
                Network net = Network.build((List<Pair>)Manager.getInstance().getResult(Manager.Result.Overlap));
                netView.loadNetwork(net);
                tab.setText("Network Viewer");
                tab.setContent(netView);
                break;
            default: tab.setText("test");
        }
        return tab;
    }
}
