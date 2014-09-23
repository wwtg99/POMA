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

import cn.edu.suda.core.stats.DataMatrix;
import cn.edu.suda.core.stats.Pair;
import cn.edu.suda.core.stats.StringMatrix;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 *
 * @author Wentao Wu
 */
public class TableBuilder {

    public static TableView getTable(DataMatrix dm){
        TableView<ObservableList<String>> table = new TableView<>();
        TableColumn<ObservableList<String>, String> colrowname  = new TableColumn<>(dm.getName());
        colrowname.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>,String>, ObservableValue<String>>() {         

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> p) {
                return new SimpleStringProperty(p.getValue().get(0));
            }
        });
        table.getColumns().add(colrowname);
        
        for(int i = 0; i < dm.getDcol(); i++){
            final int n = i + 1;
            TableColumn<ObservableList<String>, String> col  = new TableColumn<>(dm.getColname(i));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>,String>, ObservableValue<String>>() {         

                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> p) {
                    return new SimpleStringProperty(p.getValue().get(n));
                }
            });
            table.getColumns().add(col);
        }
        
        ObservableList<ObservableList<String>> ss = FXCollections.observableArrayList();
        for(int i = 0; i < dm.getDrow(); i++){
            ObservableList<String> s = FXCollections.observableArrayList();
            if(dm.getRowname(i) != null){
                s.add(dm.getRowname(i));
            }else{
                s.add("row" + i);
            }
            for(int j = 0; j < dm.getDcol(); j++){
                s.add(String.valueOf(dm.getValue(i, j)));
            }
            ss.add(s);
        }
              
        table.setItems(ss);
        return table;
    }
    
    public static TableView getTable(StringMatrix sm){
        TableView<ObservableList<String>> table = new TableView<>();
        TableColumn<ObservableList<String>, String> colrowname  = new TableColumn<>(sm.getName());
        colrowname.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>,String>, ObservableValue<String>>() {         

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> p) {
                return new SimpleStringProperty(p.getValue().get(0));
            }
        });
        table.getColumns().add(colrowname);
        
        for(int i = 0; i < sm.getDcol(); i++){
            final int n = i + 1;
            TableColumn<ObservableList<String>, String> col  = new TableColumn<>(sm.getColname(i));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>,String>, ObservableValue<String>>() {         

                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> p) {
                    return new SimpleStringProperty(p.getValue().get(n));
                }
            });
            table.getColumns().add(col);
        }
        
        ObservableList<ObservableList<String>> ss = FXCollections.observableArrayList();
        for(int i = 0; i < sm.getDrow(); i++){
            ObservableList<String> s = FXCollections.observableArrayList();
            if(sm.getRowname(i) != null){
                s.add(sm.getRowname(i));
            }else{
                s.add("row" + i);
            }
            for(int j = 0; j < sm.getDcol(); j++){
                s.add(String.valueOf(sm.getValue(i, j)));
            }
            ss.add(s);
        }
              
        table.setItems(ss);
        return table;
    }
    
    public static TableView getTable(List<Pair> pairs){
        TableView<ObservableList<String>> table = new TableView<>();
        for(int i = 0; i < 2; i++){
            final int n = i;
            TableColumn<ObservableList<String>, String> colrowname  = new TableColumn<>();
            colrowname.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>,String>, ObservableValue<String>>() {         

                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> p) {
                    return new SimpleStringProperty(p.getValue().get(n));
                }
            });
            table.getColumns().add(colrowname);
        }
        
        ObservableList<ObservableList<String>> ss = FXCollections.observableArrayList();
        for(int i = 0; i < pairs.size(); i++){
            ObservableList<String> s = FXCollections.observableArrayList();           
            s.add(pairs.get(i).getP1());
            s.add(pairs.get(i).getP2());
            ss.add(s);
        }
              
        table.setItems(ss);
        return table;
    }
}
