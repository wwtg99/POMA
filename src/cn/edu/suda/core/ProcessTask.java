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
import cn.edu.suda.core.stats.MatrixBuilder;
import cn.edu.suda.core.stats.Pair;
import cn.edu.suda.core.stats.PairUtils;
import cn.edu.suda.core.stats.StatsUtils;
import cn.edu.suda.core.stats.StringMatrix;
import cn.edu.suda.core.stats.Triplet;
import cn.edu.suda.utils.Utils;
import java.util.List;
import javafx.concurrent.Task;
import org.apache.commons.math3.stat.StatUtils;

/**
 *
 * @author Wentao Wu
 */
public class ProcessTask extends Task<Integer>{

    @Override
    protected Integer call() throws Exception {
        Manager ma = Manager.getInstance();
        long t1 = System.currentTimeMillis();
        updateMessage("Processing...");
        ma.putResult(Manager.Result.Analyzed, 1);
        //step 1
        updateMessage("step1");
        int testMethod = Integer.parseInt(ma.getParam(Manager.Keyword.Test_Method));
        int g1n = Integer.parseInt(ma.getParam(Manager.Keyword.GeneGroup1Num));
        int g2n = Integer.parseInt(ma.getParam(Manager.Keyword.GeneGroup2Num));
        int r1n = Integer.parseInt(ma.getParam(Manager.Keyword.RNAGroup1Num));
        int r2n = Integer.parseInt(ma.getParam(Manager.Keyword.RNAGroup2Num));
        double LSOSSR = Double.parseDouble(ma.getParam(Manager.Keyword.LSOSS_R));
        double corR = Double.parseDouble(ma.getParam(Manager.Keyword.cor_R));
        DataMatrix geneM = MatrixBuilder.readDouble(Manager.getInstance().getParam(Manager.Keyword.Input_Gene), 
                Boolean.parseBoolean(Manager.getInstance().getParam(Manager.Keyword.Gene_hasRowname)), 
                Boolean.parseBoolean(Manager.getInstance().getParam(Manager.Keyword.Gene_hasColname)),
                Boolean.parseBoolean(Manager.getInstance().getParam(Manager.Keyword.Gene_hasName)));
        DataMatrix miRNAM = MatrixBuilder.readDouble(Manager.getInstance().getParam(Manager.Keyword.Input_miRNA), 
                Boolean.parseBoolean(Manager.getInstance().getParam(Manager.Keyword.miRNA_hasRowname)),
                Boolean.parseBoolean(Manager.getInstance().getParam(Manager.Keyword.miRNA_hasColname)),
                Boolean.parseBoolean(Manager.getInstance().getParam(Manager.Keyword.miRNA_hasName)));
        for(int i = 0; i < miRNAM.getDrow(); i++){
            miRNAM.setRowname(i, Utils.trimMiRNAName(miRNAM.getRowname(i)));
        }
        ma.putResult(Manager.Result.Gene_Num_All, geneM.getDrow());
        ma.putResult(Manager.Result.MiRNA_Num_All, miRNAM.getDrow());
        if(testMethod == 0){
            updateMessage("T Test...");
            geneM = StatsUtils.addTTest(geneM, g1n, g2n);
            miRNAM = StatsUtils.addTTest(miRNAM, r1n, r2n);
            geneM = geneM.sort(g1n + g2n + 1, true).top(0.3f);
            miRNAM = miRNAM.sort(r1n + r2n + 1, true).top(0.3f);
            ma.putResult(Manager.Result.Gene_Matrix, geneM);
            ma.putResult(Manager.Result.MiRNA_Matrix, miRNAM);
            ma.putResult(Manager.Result.Gene_Num_Select, geneM.getDrow());
            ma.putResult(Manager.Result.MiRNA_Num_Select, miRNAM.getDrow());
        }else if(testMethod == 1){
            updateMessage("LSOSS Test");
            geneM = StatsUtils.LSOSSTest(geneM, g1n, g2n, LSOSSR);
            miRNAM = StatsUtils.LSOSSTest(miRNAM, r1n, r2n, LSOSSR);
            ma.putResult(Manager.Result.Gene_Matrix, geneM);
            ma.putResult(Manager.Result.MiRNA_Matrix, miRNAM);
            ma.putResult(Manager.Result.Gene_Num_Select, geneM.getDrow());
            ma.putResult(Manager.Result.MiRNA_Num_Select, miRNAM.getDrow());
        }
              
        //step 2
        updateMessage("Correlation Test...");
        StringMatrix corM = StatsUtils.pearsonCor(miRNAM, geneM, r1n + r2n, g1n + g2n, corR);
        ma.putResult(Manager.Result.Cor_Matrix, corM);
        ma.putResult(Manager.Result.Cor_Num, corM.getDrow());
        
        //step 3
        updateMessage("Overlapping...");
        List<Pair> overlap;
        if(ma.containParam(Manager.Keyword.Input_interaction) && !ma.getParam(Manager.Keyword.Input_interaction).isEmpty()){
            List<Pair> p1 = PairUtils.fromStringMatrix(corM, 0, 1);
            StringMatrix interaction = MatrixBuilder.readString(ma.getParam(Manager.Keyword.Input_interaction), false, false, false);
            ma.putResult(Manager.Result.Interaction_Matrix, interaction);
            List<Pair> p2 = PairUtils.fromStringMatrix(interaction, 0, 1);
            overlap = PairUtils.overlap(p1, p2);
        }else{
            overlap = PairUtils.fromStringMatrix(corM, 0, 1);
        }
        ma.putResult(Manager.Result.Overlap, overlap);
        ma.putResult(Manager.Result.Overlap_Num, overlap.size());
        
        //step 4
        updateMessage("Calculating NOD...");
        List<Triplet> nods = PairUtils.countNOD(overlap, PairUtils.count(overlap, false), false);
        ma.putResult(Manager.Result.NOD, nods);
        
        //step 5
        updateMessage("Wilcox Test...");
        double[] data1 = new double[nods.size()];
        int i = 0;
        for(Triplet p:nods){
            data1[i++] = Double.parseDouble(p.getT2());
        }
        double max = StatUtils.max(data1);
        double[] wilcox = new double[(int)Math.ceil(max) + 1];
        for(int j = 0; j < max + 1; j++){
            wilcox[j] = StatsUtils.wilcoxTest(data1, j);
        }
        ma.putResult(Manager.Result.WilcoxTest, wilcox);
        
        long t2 = System.currentTimeMillis();
        float time = Utils.formatNumber((t2 - t1) / 1000.0, 3);
        Manager.LOG.info("Finished! Time: " + time + " s");
        return 0;
    }
    
    

}
