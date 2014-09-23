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

import cn.edu.suda.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jsc.datastructures.PairedData;
import jsc.onesample.WilcoxonTest;
import jsc.regression.PearsonCorrelation;
import jsc.tests.H1;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.inference.TestUtils;

/**
 *
 * @author Wentao Wu
 */
public class StatsUtils {

    public static DataMatrix addTTest(DataMatrix dm, int m, int n){
        int col = dm.getDcol();
        dm.addCols(2);
        dm.setColname(col, "ABS_t_value");
        dm.setColname(col + 1, "P_value");
        for(int i = 0; i < dm.getDrow(); i++){
            double[] array = dm.getRow(i);
            SummaryStatistics stats1 = new SummaryStatistics();
            SummaryStatistics stats2 = new SummaryStatistics();
            for(int j = 0; j < m; j++){
                stats1.addValue(array[j]);
            }
            for(int j = m; j < m + n; j++){
                stats2.addValue(array[j]);
            }
            double var1 = stats1.getVariance();
            double var2 = stats2.getVariance();
            if(var1 == 0 && var2 == 0){
                dm.setValue(i, col, 0);
                dm.setValue(i, col + 1, 1);
            }else{
                double t = Math.abs(TestUtils.t(stats1, stats2));
                double p = TestUtils.tTest(stats1, stats2);
                t = Utils.formatNumber(t, 4);
                p = Utils.formatNumber(p, 4);
                dm.setValue(i, col, t);
                dm.setValue(i, col + 1, p);
            }
        }
        return dm;
    }
    
    public static DataMatrix LSOSSTest(DataMatrix dm, int m, int n, double r){
        int row = dm.getDrow();
        DataMatrix result = new DataMatrix(row, n);
        DataMatrix varall = new DataMatrix(row, n);
        DataMatrix statistics = new DataMatrix(row, 1);
        statistics.setRownames(dm.getRownames());
        DataMatrix sampley = new DataMatrix(row, n);
        double[] meanx = new double[row];
        double[] meany = new double[row];
        double[] varx = new double[row];
        for(int i = 0; i < row; i++){
            double[] samplexline = new double[m];
            for(int j = 0; j < m; j++){
                samplexline[j] = dm.getValue(i, j);
            }
            double[] sampleyline = new double[n];
            for(int j = 0; j < n; j++){
                sampleyline[j] = dm.getValue(i, m + j);
            }
            sampleyline = sort(sampleyline, false);
            sampley.setRow(i, sampleyline);
            sampley.setRowname(i, dm.getRowname(i));
            meanx[i] = StatUtils.mean(samplexline);
            meany[i] = StatUtils.mean(sampleyline);
            varx[i] = StatUtils.variance(samplexline, meanx[i]);        
        }
        
        for(int kt = 0; kt < n; kt++){
            double[] meanOutlier = new double[row];
            double[] meanOther = new double[row];
            double[] varOutlier = new double[row];
            double[] varOther = new double[row];
            for(int i = 0; i < row; i++){
                if(kt == 0){               
                    meanOutlier[i] = sampley.getValue(i, 0);
                    double[] other = new double[n - 1];
                    System.arraycopy(sampley.getRow(i), 1, other, 0, n - 1);
                    meanOther[i] = StatUtils.mean(other);
                    varOther[i] = StatUtils.variance(other, meanOther[i]);
                    varall.setValue(i, kt, varOther[i]);               
                }else if(kt > 0 && kt < n - 2){            
                    double[] outlier = new double[kt + 1];
                    System.arraycopy(sampley.getRow(i), 0, outlier, 0, kt + 1);
                    meanOutlier[i] = StatUtils.mean(outlier);
                    varOutlier[i] = StatUtils.variance(outlier, meanOutlier[i]);
                    double[] other = new double[n - kt - 1];
                    System.arraycopy(sampley.getRow(i), kt + 1, other, 0, n - kt - 1);
                    meanOther[i] = StatUtils.mean(other);
                    varOther[i] = StatUtils.variance(other, meanOther[i]);
                    varall.setValue(i, kt, varOutlier[i] + varOther[i]);             
                }else{            
                    double[] outlier = new double[kt + 1];
                    System.arraycopy(sampley.getRow(i), 0, outlier, 0, kt + 1);
                    meanOutlier[i] = StatUtils.mean(outlier);
                    varOutlier[i] = StatUtils.variance(outlier, meanOutlier[i]);
                    varall.setValue(i, kt, varOutlier[i]);            
                }
                result.setValue(i, kt, kt * (meanOutlier[i] - meanx[i]) / (Math.sqrt(varx[i] + varall.getValue(i, kt)) + 0.01));
            }
        }
        
        List<Integer> outlierRow = new ArrayList<>();
        for(int i = 0; i < row; i++){
            if(meany[i] >= meanx[i]){
                statistics.setValue(i, 0, 0);
            }else{
                int minIndex = indexOfMin(varall.getRow(i));
                statistics.setValue(i, 0, result.getValue(i, minIndex));
                if(statistics.getValue(i, 0) <= r){
                    outlierRow.add(i);
                }
            }
        }
        
        DataMatrix out = new DataMatrix(outlierRow.size(), dm.getDcol());
        out.setColnames(dm.getColnames());
        for(int i = 0; i < outlierRow.size(); i++){
            out.setRow(i, dm.getRow(outlierRow.get(i)));
            out.setRowname(i, dm.getRowname(outlierRow.get(i)));
        }
        return out;
    }
    
    public static StringMatrix pearsonCor(DataMatrix nm1, DataMatrix nm2, int m, int n, double r){
        List<String> result = new ArrayList<>();
        for(int i = 0; i < nm1.getDrow(); i++){
            for(int j = 0; j < nm2.getDrow(); j++){
                double[] a1 = Arrays.copyOfRange(nm1.getRow(i), 0, m);
                double[] a2 = Arrays.copyOfRange(nm2.getRow(j), 0, n);
                PairedData pd = new PairedData(a1, a2);
                double cor = new PearsonCorrelation(pd).getR();
                if(cor <= r){
                    result.add(nm1.getRowname(i) + "\t" + nm2.getRowname(j) + "\t" + cor);
                }
            }
        }
        
        StringMatrix out = new StringMatrix(result.size(), 3);
        for(int i = 0; i < result.size(); i++){
            String[] s = result.get(i).split("\t");
            for(int j = 0; j < s.length; j++){
                out.setValue(i, j, s[j]);
            }
        }
        return out;
    }
    
    public static double wilcoxTest(double[] data, double mu){
        try{
            WilcoxonTest wt = new WilcoxonTest(data, mu, H1.LESS_THAN);
            return wt.getSP();
        }catch(Exception e){
            return 0;
        }
    }
    
    public static double[] sort(double[] data, boolean desc){
        for(int i = 0; i < data.length - 1; i++){
            for(int j = 0; j < data.length - i - 1; j++){
                if(desc){
                    if(data[j] < data[j + 1]){
                        double tmp = data[j];
                        data[j] = data[j + 1];
                        data[j + 1] = tmp;
                    }
                }else{
                    if(data[j] > data[j + 1]){
                        double tmp = data[j];
                        data[j] = data[j + 1];
                        data[j + 1] = tmp;
                    }
                }
            }
        }
        return data;
    }
    
    public static int indexOfMin(double[] data){
        double min = Double.MAX_VALUE;
        int index = 0;
        for(int i = 0; i < data.length; i++){
            if(data[i] < min){
                min = data[i];
                index = i;
            }
        }
        return index;
    }
}
