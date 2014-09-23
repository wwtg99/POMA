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

package cn.edu.suda.core.stats;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Wentao Wu
 */
public class MatrixBuilder {

    public static DataMatrix readDouble(String file, boolean hasRowname, boolean hasColname, boolean hasName) throws FileNotFoundException, IOException, NumberFormatException{
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line = br.readLine();
            String[] colnames = null;
            String name = "";
            if(hasColname){
                if(hasName){
                    String[] s = line.split("\t");
                    name = s[0];
                    colnames = Arrays.copyOfRange(s, 1, s.length);
                }else{
                    colnames = line.split("\t");
                }
                line = br.readLine();
            }
            List<String> rownamesList = new ArrayList<>();
            List<List<Double>> data = new ArrayList<>();
            if(hasRowname){
                while(line != null){
                    String[] cols = line.split("\t");
                    rownamesList.add(cols[0]);
                    data.add(getLineData(cols, hasRowname));
                    line = br.readLine();
                }
            }else{
                while(line != null){
                    String[] cols = line.split("\t");
                    data.add(getLineData(cols, hasRowname));
                    line = br.readLine();
                }
            }
            String[] rownames = new String[rownamesList.size()];
            rownames = rownamesList.toArray(rownames);
            DataMatrix nm = new DataMatrix(getData(data));
            if(hasRowname){
                nm.setRownames(rownames);
            }
            if(hasColname){
                nm.setColnames(colnames);
            }
            if(hasName){
                nm.setName(name);
            }
            return nm;
        }
    }
    
    public static DataMatrix readDouble(String file) throws FileNotFoundException, IOException, NumberFormatException{
        return readDouble(file, true, true, true);
    }
    
    public static StringMatrix readString(String file) throws FileNotFoundException, IOException{
        return readString(file, true, true, true);
    }
    
    public static StringMatrix readString(String file, boolean hasRowname, boolean hasColname, boolean hasName) throws FileNotFoundException, IOException{
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line = br.readLine();
            String[] colnames = null;
            String name = "";
            if(hasColname){
                if(hasName){
                    String[] s = line.split("\t");
                    name = s[0];
                    colnames = Arrays.copyOfRange(s, 1, s.length);
                }else{
                    colnames = line.split("\t");
                }
                line = br.readLine();
            }
            List<String> rownamesList = new ArrayList<>();
            List<List<String>> data = new ArrayList<>();
            if(hasRowname){
                while(line != null){
                    String[] cols = line.split("\t");
                    rownamesList.add(cols[0]);
                    data.add(getLineDataString(cols, hasRowname));
                    line = br.readLine();
                }
            }else{
                while(line != null){
                    String[] cols = line.split("\t");
                    data.add(getLineDataString(cols, hasRowname));
                    line = br.readLine();
                }
            }
            String[] rownames = new String[rownamesList.size()];
            rownames = rownamesList.toArray(rownames);
            StringMatrix nm = new StringMatrix(getDataString(data));
            if(hasRowname){
                nm.setRownames(rownames);
            }
            if(hasColname){
                nm.setColnames(colnames);
            }
            if(hasName){
                nm.setName(name);
            }
            return nm;
        }
    }
    
    private static List<Double> getLineData(String[] cols, boolean rowname) throws NumberFormatException{
        int start = 0;
        if(rowname){
            start = 1;
        }
        List<Double> fs = new ArrayList<>(cols.length - start);
        for(int i = start; i < cols.length; i++){
            double f = Double.parseDouble(cols[i]);
            fs.add(f);
        }
        return fs;
    }
    
    private static double[][] getData(List<List<Double>> list){
        double[][] out = new double[list.size()][list.get(0).size()];
        for(int i = 0; i < list.size(); i++){
            for(int j = 0; j < list.get(i).size(); j++){
                out[i][j] = list.get(i).get(j);
            }
        }
        return out;
    }
    
    private static List<String> getLineDataString(String[] cols, boolean rowname) {
        int start = 0;
        if(rowname){
            start = 1;
        }
        List<String> fs = new ArrayList<>(cols.length - start);
        for(int i = start; i < cols.length; i++){
            fs.add(cols[i]);
        }
        return fs;
    }
    
    private static String[][] getDataString(List<List<String>> list){
        String[][] out = new String[list.size()][list.get(0).size()];
        for(int i = 0; i < list.size(); i++){
            for(int j = 0; j < list.get(i).size(); j++){
                out[i][j] = list.get(i).get(j);
            }
        }
        return out;
    }
}
