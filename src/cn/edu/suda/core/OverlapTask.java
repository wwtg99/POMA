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
import cn.edu.suda.core.stats.InteractMap;
import cn.edu.suda.core.stats.MatrixBuilder;
import cn.edu.suda.core.stats.Pair;
import cn.edu.suda.core.stats.PairUtils;
import cn.edu.suda.core.stats.StatsUtils;
import cn.edu.suda.core.stats.StringMatrix;
import java.util.List;
import java.util.Map;
import javafx.concurrent.Task;

/**
 *
 * @author Wentao Wu
 */
public class OverlapTask extends Task<Integer>{

    @Override
    protected Integer call() throws Exception {
        updateMessage("Overlapping...");
        Manager ma = Manager.getInstance();
        int ptype = Integer.parseInt(ma.getParam(Manager.Keyword.Process_Type));
        List<Pair> overlap;
        if(ptype == 0){
            if(ma.containResult(Manager.Result.Cor_Matrix)){
                StringMatrix corM = (StringMatrix)ma.getResult(Manager.Result.Cor_Matrix);
                Map<String, InteractMap> interMap = (Map<String, InteractMap>)ma.getResult(Manager.Result.Interaction_Map);
                //StringMatrix interaction = MatrixBuilder.readString(ma.getParam(Manager.Keyword.Input_interaction), false, false, false);
                //ma.putResult(Manager.Result.Interaction_Matrix, interaction);
                //List<Pair> p2 = PairUtils.fromStringMatrix(interaction, 0, 1);
                List<Pair> p1 = PairUtils.fromStringMatrix(corM, 0, 1);
                overlap = PairUtils.overlap(interMap, p1);
                //overlap = PairUtils.overlap(p1, p2);
            }else{
                return 4;
            }
        }else if(ptype == 1){
            DataMatrix geneM = (DataMatrix)ma.getResult(Manager.Result.Gene_Matrix);
            DataMatrix miRNAM = (DataMatrix)ma.getResult(Manager.Result.MiRNA_Matrix);
            Map<String, InteractMap> interMap = (Map<String, InteractMap>)ma.getResult(Manager.Result.Interaction_Map);
            overlap = PairUtils.overlap(interMap, geneM.getRownames(), miRNAM.getRownames());
        }else if(ptype == 2){
            DataMatrix geneM = (DataMatrix)ma.getResult(Manager.Result.Gene_Matrix);
            Map<String, InteractMap> interMap = (Map<String, InteractMap>)ma.getResult(Manager.Result.Interaction_Map);
            overlap = PairUtils.overlap(interMap, geneM.getRownames());
        }else if(ptype == 3){
            DataMatrix miRNAM = (DataMatrix)ma.getResult(Manager.Result.MiRNA_Matrix);
            if(ma.containParam(Manager.Keyword.Input_interaction) && !ma.getParam(Manager.Keyword.Input_interaction).isEmpty()){
                StringMatrix interaction = MatrixBuilder.readString(ma.getParam(Manager.Keyword.Input_interaction), false, false, false);
                ma.putResult(Manager.Result.Interaction_Matrix, interaction);
                List<Pair> p2 = PairUtils.fromStringMatrix(interaction, 0, 1);
                overlap = PairUtils.overlap(p2, miRNAM.getRownames());
            }else{
                return 1;
            }
        }else{
            return 2;
        }
        ma.putResult(Manager.Result.Overlap, overlap);
        ma.putResult(Manager.Result.Overlap_Num, overlap.size());
        return 0;
    }

}
