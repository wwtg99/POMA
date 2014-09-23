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

import cn.edu.suda.core.network.Network;
import cn.edu.suda.core.stats.Pair;
import cn.edu.suda.core.stats.PairUtils;
import cn.edu.suda.core.stats.StatsUtils;
import cn.edu.suda.core.stats.Triplet;
import java.util.List;
import javafx.concurrent.Task;
import org.apache.commons.math3.stat.StatUtils;

/**
 *
 * @author Wentao Wu
 */
public class AnalysisTask extends Task<Integer>{

    @Override
    protected Integer call() throws Exception {
        Manager ma = Manager.getInstance();
        updateMessage("Building Network...");
        Network net = Network.build((List<Pair>)ma.getResult(Manager.Result.Overlap));
        ma.putResult(Manager.Result.Network, net);
        
        updateMessage("Calculating NOD...");
        
        if(!ma.containResult(Manager.Result.Overlap)){
            return 2;
        }
        List<Pair> overlap = (List<Pair>)ma.getResult(Manager.Result.Overlap);
        List<Triplet> nods = PairUtils.countNOD(overlap, PairUtils.count(overlap, false), false);
        ma.putResult(Manager.Result.NOD, nods);
        
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
        
        return 0;
    }

}
