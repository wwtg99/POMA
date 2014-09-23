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

package cn.edu.suda.core;

import cn.edu.suda.core.stats.DataMatrix;
import cn.edu.suda.core.stats.StatsUtils;
import javafx.concurrent.Task;

/**
 *
 * @author Wentao Wu
 */
public class TestTask extends Task<Integer>{

    @Override
    protected Integer call() throws Exception {
        updateMessage("Processing...");
        Manager ma = Manager.getInstance();
        int ptype = Integer.parseInt(ma.getParam(Manager.Keyword.Process_Type));
        int testMethod = Integer.parseInt(ma.getParam(Manager.Keyword.Test_Method));
        int g1n = 0, g2n = 0, r1n = 0, r2n = 0;
        DataMatrix geneM = null, miRNAM = null;
        if(ptype == 0 || ptype == 1 || ptype == 2){
            g1n = Integer.parseInt(ma.getParam(Manager.Keyword.GeneGroup1Num));
            g2n = Integer.parseInt(ma.getParam(Manager.Keyword.GeneGroup2Num));
            geneM = (DataMatrix)ma.getResult(Manager.Result.Gene_Matrix);
        }
        if(ptype == 0 || ptype == 1 || ptype == 3){
            r1n = Integer.parseInt(ma.getParam(Manager.Keyword.RNAGroup1Num));
            r2n = Integer.parseInt(ma.getParam(Manager.Keyword.RNAGroup2Num));
            miRNAM = (DataMatrix)ma.getResult(Manager.Result.MiRNA_Matrix);
        }
        double LSOSSR = Double.parseDouble(ma.getParam(Manager.Keyword.LSOSS_R));
        
        if(testMethod == 0){
            updateMessage("T Test...");
            if(geneM != null){
                geneM = StatsUtils.addTTest(geneM, g1n, g2n);
                geneM = geneM.sort(g1n + g2n + 1, true).top(0.3f);
                ma.putResult(Manager.Result.Gene_Matrix, geneM);
                ma.putResult(Manager.Result.Gene_Num_Select, geneM.getDrow());
            }
            if(miRNAM != null){
                miRNAM = StatsUtils.addTTest(miRNAM, r1n, r2n);
                miRNAM = miRNAM.sort(r1n + r2n + 1, true).top(0.3f);
                ma.putResult(Manager.Result.MiRNA_Matrix, miRNAM);
                ma.putResult(Manager.Result.MiRNA_Num_Select, miRNAM.getDrow());
            }
        }else if(testMethod == 1){
            updateMessage("LSOSS Test");
            if(geneM != null){
                geneM = StatsUtils.LSOSSTest(geneM, g1n, g2n, LSOSSR);
                ma.putResult(Manager.Result.Gene_Matrix, geneM);
                ma.putResult(Manager.Result.Gene_Num_Select, geneM.getDrow());
            }
            if(miRNAM != null){
                miRNAM = StatsUtils.LSOSSTest(miRNAM, r1n, r2n, LSOSSR);
                ma.putResult(Manager.Result.MiRNA_Matrix, miRNAM);
                ma.putResult(Manager.Result.MiRNA_Num_Select, miRNAM.getDrow());
            }
        }
        return 0;
    }

}
