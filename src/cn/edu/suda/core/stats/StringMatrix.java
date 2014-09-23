/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.suda.core.stats;

/**
 *
 * @author Administrator
 */
public class StringMatrix {
    private String[][] data;
    private String rownames[], colnames[];
    private int drow, dcol;
    private String name = "";
    
    public StringMatrix(int row, int col){
        this.drow = row;
        this.dcol = col;
        data = new String[row][col];
        rownames = new String[row];
        colnames = new String[col];
    }
    
    public StringMatrix(String[][] data){
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
        String[][] newData = new String[drow][dcol];
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
        String[][] newData = new String[drow][dcol];
        for(int i = 0; i < data.length; i++){
            System.arraycopy(data[i], 0, newData[i], 0, data[i].length);
        }
        data = newData;
    }
    
    public String getValue(int row, int col){
        return data[row][col];
    }
    
    public void setValue(int row, int col, String value){
        data[row][col] = value;
    }
    
    public String[] getRow(int row){
        String[] out = new String[dcol];
        System.arraycopy(data[row], 0, out, 0, dcol);
        return out;
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

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
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
        String[] tmp = getRow(row1);
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

}
