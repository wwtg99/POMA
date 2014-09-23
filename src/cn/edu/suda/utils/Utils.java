/*
 * <Amino Acid Network Worker (AANW) is a useful AA network construction tool.>
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

package cn.edu.suda.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 *
 * @author Wentao Wu
 */
public class Utils {

    public static float formatNumber(double value, int scale){
        BigDecimal b = new BigDecimal(value);    
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }
    
    public static String formatNumberS(double d, int scale){
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(scale);
        return nf.format(d);
    }
    
    public static boolean containLetter(String str){
        for(int i = 0; i < str.length(); i++){
            char c = str.charAt(i);
            if(Character.isLetter(c)){
                return true;
            }
        }
        return false;
    }
    
    public static String arrayToString(int[] array, String sep){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < array.length; i++){
            sb.append(array[i]).append(sep);
        }
        return sb.toString();
    }
    
    public static String arrayToString(double[] array, String sep){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < array.length; i++){
            sb.append(array[i]).append(sep);
        }
        return sb.toString();
    }
    
    public static String arrayToString(Object[] array, String sep){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < array.length; i++){
            sb.append(array[i].toString()).append(sep);
        }
        return sb.toString();
    }
    
    public static String matrixToString(int[][] matrix, String sep){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                sb.append(matrix[i][j]).append(sep);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public static String matrixToString(double[][] matrix, String sep){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                sb.append(matrix[i][j]).append(sep);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public static String matrixToString(Object[][] matrix, String sep){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                sb.append(matrix[i][j].toString()).append(sep);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public static String trimMiRNAName(String miRNA){
        String miRNAl = miRNA.toLowerCase();
        if(miRNAl.startsWith("mir") || miRNAl.startsWith("let")){
            return miRNA;
        }else{
            if(miRNAl.contains("mir")){
                int n = miRNAl.indexOf("mir");
                return miRNAl.substring(n);
            }else if(miRNAl.contains("let")){
                int n = miRNAl.indexOf("let");
                return miRNAl.substring(n);
            }else{
                return miRNA;
            }
        }
    }
}
