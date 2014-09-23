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
import cn.edu.suda.core.stats.StringMatrix;
import javafx.concurrent.Task;

/**
 *
 * @author Wentao Wu
 */
public class CorrelateTask extends Task<Integer>{

    @Override
    protected Integer call() throws Exception {
        updateMessage("Correlation Test...");
        Manager ma = Manager.getInstance();
        int ptype = Integer.parseInt(ma.getParam(Manager.Keyword.Process_Type));
        if(ptype == 0){
            int g1n = Integer.parseInt(ma.getParam(Manager.Keyword.GeneGroup1Num));
            int g2n = Integer.parseInt(ma.getParam(Manager.Keyword.GeneGroup2Num));
            int r1n = Integer.parseInt(ma.getParam(Manager.Keyword.RNAGroup1Num));
            int r2n = Integer.parseInt(ma.getParam(Manager.Keyword.RNAGroup2Num));
            double corR = Double.parseDouble(ma.getParam(Manager.Keyword.cor_R));
            DataMatrix geneM = (DataMatrix)ma.getResult(Manager.Result.Gene_Matrix);
            DataMatrix miRNAM = (DataMatrix)ma.getResult(Manager.Result.MiRNA_Matrix);
            StringMatrix corM = StatsUtils.pearsonCor(miRNAM, geneM, r1n + r2n, g1n + g2n, corR);
            ma.putResult(Manager.Result.Cor_Matrix, corM);
            ma.putResult(Manager.Result.Cor_Num, corM.getDrow());
        }else{
            return 3;
        }
        return 0;
    }

}
