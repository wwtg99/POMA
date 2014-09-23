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
import cn.edu.suda.core.stats.StringMatrix;
import cn.edu.suda.utils.Utils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.concurrent.Task;

/**
 *
 * @author Wentao Wu
 */
public class LoadTask extends Task<Integer>{

    @Override
    protected Integer call() throws Exception {
        updateMessage("Processing...");
        Manager ma = Manager.getInstance();
        int ptype = Integer.parseInt(ma.getParam(Manager.Keyword.Process_Type));
        if(ptype == 0 || ptype == 1 || ptype == 2){
            DataMatrix geneM = MatrixBuilder.readDouble(Manager.getInstance().getParam(Manager.Keyword.Input_Gene), 
                    Boolean.parseBoolean(Manager.getInstance().getParam(Manager.Keyword.Gene_hasRowname)), 
                    Boolean.parseBoolean(Manager.getInstance().getParam(Manager.Keyword.Gene_hasColname)),
                    Boolean.parseBoolean(Manager.getInstance().getParam(Manager.Keyword.Gene_hasName)));
            ma.putResult(Manager.Result.Gene_Num_All, geneM.getDrow());
            ma.putResult(Manager.Result.Gene_Matrix, geneM);
            ma.putResult(Manager.Result.Gene_Num_Select, geneM.getDrow());
        }
        if(ptype == 0 || ptype == 1 || ptype == 3){
            DataMatrix miRNAM = MatrixBuilder.readDouble(Manager.getInstance().getParam(Manager.Keyword.Input_miRNA), 
                    Boolean.parseBoolean(Manager.getInstance().getParam(Manager.Keyword.miRNA_hasRowname)),
                    Boolean.parseBoolean(Manager.getInstance().getParam(Manager.Keyword.miRNA_hasColname)),
                    Boolean.parseBoolean(Manager.getInstance().getParam(Manager.Keyword.miRNA_hasName)));
            for(int i = 0; i < miRNAM.getDrow(); i++){
                miRNAM.setRowname(i, Utils.trimMiRNAName(miRNAM.getRowname(i)));
            }
            ma.putResult(Manager.Result.MiRNA_Num_All, miRNAM.getDrow());
            ma.putResult(Manager.Result.MiRNA_Matrix, miRNAM);
            ma.putResult(Manager.Result.MiRNA_Num_Select, miRNAM.getDrow());
        }
        
        StringMatrix interaction = MatrixBuilder.readString(ma.getParam(Manager.Keyword.Input_interaction), false, false, false);
        ma.putResult(Manager.Result.Interaction_Matrix, interaction);
        List<Pair> list = PairUtils.fromStringMatrix(interaction, 0, 1);
        Map<String, InteractMap> interMap = new HashMap<>();
        for(Pair p:list){
            if(interMap.containsKey(p.getP2().toUpperCase())){
                interMap.get(p.getP2().toUpperCase()).add(p.getP1().toUpperCase());
            }else{
                InteractMap imap = new InteractMap(p.getP2().toUpperCase());
                imap.add(p.getP1().toUpperCase());
                interMap.put(p.getP2().toUpperCase(), imap);
            }
        }
        ma.putResult(Manager.Result.Interaction_Map, interMap);
        return 0;
    }

}
