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

/**
 *
 * @author Wentao Wu
 */
public class DataMatrix {
    private double data[][];
    private String rownames[], colnames[];
    private int drow, dcol;
    private String name = "";
    
    public DataMatrix(int row, int col){
        this.drow = row;
        this.dcol = col;
        data = new double[row][col];
        rownames = new String[row];
        colnames = new String[col];
    }
    
    public DataMatrix(double[][] data){
        this.data = data;
        this.drow = data.length;
        this.dcol = data[0].length;
        rownames = new String[drow];
        colnames = new String[dcol];
    }
    
    public void addCols(int cols){
        dcol += cols;
        String[] newColnames = new String[dcol];
        System.arraycopy(colnames, 0, newColnames, 0, colnames.length);
        colnames = newColnames;
        double[][] newData = new double[drow][dcol];
        for(int i = 0; i < drow; i++){
            System.arraycopy(data[i], 0, newData[i], 0, data[i].length);
        }
        data = newData;
    }
    
    public void addRows(int rows){
        drow += rows;
        String[] newRownames = new String[drow];
        System.arraycopy(rownames, 0, newRownames, 0, rownames.length);
        rownames = newRownames;
        double[][] newData = new double[drow][dcol];
        for(int i = 0; i < data.length; i++){
            System.arraycopy(data[i], 0, newData[i], 0, data[i].length);
        }
        data = newData;
    }
    
    public double getValue(int row, int col){
        return data[row][col];
    }
    
    public void setValue(int row, int col, double value){
        data[row][col] = value;
    }
    
    public double[] getRow(int row){
        double[] out = new double[dcol];
        System.arraycopy(data[row], 0, out, 0, dcol);
        return out;
    }
    
    public void setRow(int row, double[] rowData){
        if(row >= 0 && row < drow){
            System.arraycopy(rowData, 0, data[row], 0, dcol);
        }
    }
    
    public String getRowname(int row){
        return rownames[row];
    }
    
    public void setRowname(int row, String name){
        rownames[row] = name;
    }
    
    public String getColname(int col){
        return colnames[col];
    }
    
    public void setColname(int col, String name){
        colnames[col] = name;
    }

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] data) {
        this.data = data;
        this.drow = data.length;
        this.dcol = data[0].length;
    }

    public String[] getRownames() {
        return rownames;
    }

    public void setRownames(String[] rownames) {
        this.rownames = rownames;
    }

    public String[] getColnames() {
        return colnames;
    }

    public void setColnames(String[] colnames) {
        this.colnames = colnames;
    }

    public int getDrow() {
        return drow;
    }

    public int getDcol() {
        return dcol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(); 
        sb.append(name).append("\t");
        for(String s:colnames){
            if(s == null || s.isEmpty()){
                sb.append("col").append("\t");
            }else{
                sb.append(s).append("\t");
            }
        }
        sb.append("\n");
        for(int i = 0; i < drow; i++){
            if(rownames[i] == null || rownames[i].isEmpty()){
                sb.append("row").append(i + 1).append("\t");
            }else{
                sb.append(rownames[i]).append("\t");
            }          
            for(int j = 0; j < dcol; j++){
                sb.append(data[i][j]).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public void swap(int row1, int row2){
        double[] tmp = getRow(row1);
        String tmpname = getRowname(row1);
        for(int i = 0; i < dcol; i++){
            setValue(row1, i, getValue(row2, i));
        }
        setRowname(row1, getRowname(row2));
        for(int i = 0; i < dcol; i++){
            setValue(row2, i, tmp[i]);
        }
        setRowname(row2, tmpname);
    }

    public DataMatrix sort(int col, boolean asc){
        for(int i = 0; i < data.length - 1; i++){
            for(int j = 0; j < data.length - i - 1; j++){
                if(asc){
                    if(data[j][col] > data[j + 1][col]){
                        swap(j, j + 1);
                    }
                }else{
                    if(data[j][col] < data[j + 1][col]){
                        swap(j, j + 1);
                    }
                }
            }
        }
        return this;
    }
    
    public DataMatrix top(float percent){
        int nrow = Math.round(percent * drow);
        double[][] d = new double[nrow][dcol];
        String[] rn = new String[nrow];
        for(int i = 0; i < nrow; i++){
            d[i] = data[i];
            rn[i] = rownames[i];
        }
        DataMatrix re = new DataMatrix(d);
        re.setColnames(colnames);
        re.setRownames(rn);
        return re;
    }
}
