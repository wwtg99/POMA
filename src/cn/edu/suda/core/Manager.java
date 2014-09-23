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

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Wentao Wu
 */
public class Manager {

    public enum Keyword{
        Comfirm_Input, Input_Gene, Gene_hasRowname, Gene_hasColname, Gene_hasName, 
        Input_miRNA, miRNA_hasRowname, miRNA_hasColname, miRNA_hasName, 
        Input_interaction, Inter_hasRowname, Inter_hasColname, Inter_hasName, 
        Test_Method, GeneGroup1Num, GeneGroup2Num, RNAGroup1Num, RNAGroup2Num, LSOSS_R, cor_R, Process_Type
    }
    public enum Result{
        Analyzed, Gene_Matrix, MiRNA_Matrix, Interaction_Matrix, Interaction_Map, Cor_Matrix, Gene_Num_All, MiRNA_Num_All, 
        Gene_Num_Select, MiRNA_Num_Select, Cor_Num, Overlap, Overlap_Num, NOD, WilcoxTest, DAVID_URL, Network
    }
    public static final String INFO = "Prediction of miRNA Activity";
    public static final String VERSION = "v0.3";
    public static final String AUTHOR = "Bairong Shen, Wenyu Zhang, Wentao Wu";
    public static final String COPYRIGHT = "<Prediction of miRNA Activity>\n" +
        "Copyright (C) <2013>\n" +
        "\n" +
        "This program is free software: you can redistribute it and/or modify\n" +
        "it under the terms of the GNU General Public License as published by\n" +
        "the Free Software Foundation, either version 3 of the License, or\n" +
        "(at your option) any later version.\n" +
        "\n" +
        "This program is distributed in the hope that it will be useful,\n" +
        "but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
        "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
        "GNU General Public License for more details.\n" +
        "\n" +
        "You should have received a copy of the GNU General Public License\n" +
        "along with this program.  If not, see <http://www.gnu.org/licenses/>.";
    public static final Logger LOG = Logger.getLogger(Manager.class.getName());
    public static final String DOCFILE = "readme.html";
    public static final String InterFile = "data\\miRNA_mRNA_interaction_data.txt";
    public static final String exGeneFile = "data\\Gene_expression.txt";
    public static final String exRNAFile = "data\\miRNA_expression.txt";
    
    private static Manager instance = null;
    private Map<Keyword, String> params = Collections.synchronizedMap(new EnumMap<Keyword, String>(Keyword.class));
    private Map<Result, Object> results = Collections.synchronizedMap(new EnumMap<>(Result.class));
    
    private Manager(){
        
    }
    
    public static Manager getInstance(){
        if(instance == null){
            instance = new Manager();
        }
        return instance;
    }
    
    public String getParam(Keyword key){
        if(!params.containsKey(key)){
            return "";
        }
        return params.get(key);
    }
    
    public void putParam(Keyword key, String value){
        params.put(key, value);
    }
    
    public boolean containParam(Keyword key){
        return params.containsKey(key);
    }
    
    public void clearParams(){
        params.clear();
    }
    
    public Object getResult(Result res){
        return results.get(res);
    }
    
    public void putResult(Result res, Object value){
        results.put(res, value);
    }
    
    public boolean containResult(Result res){
        return results.containsKey(res);
    }
    
    public void clearResults(){
        results.clear();
    }
    
    public void loadExample(){
        Manager.getInstance().putParam(Manager.Keyword.Process_Type, "0");
        Manager.getInstance().putParam(Manager.Keyword.Input_Gene, exGeneFile);
        Manager.getInstance().putParam(Manager.Keyword.Input_miRNA, exRNAFile);
        Manager.getInstance().putParam(Manager.Keyword.GeneGroup1Num, "4");
        Manager.getInstance().putParam(Manager.Keyword.GeneGroup2Num, "4");
        Manager.getInstance().putParam(Manager.Keyword.RNAGroup1Num, "4");
        Manager.getInstance().putParam(Manager.Keyword.RNAGroup2Num, "4");
        Manager.getInstance().putParam(Manager.Keyword.Test_Method, "1");
        Manager.getInstance().putParam(Manager.Keyword.Input_interaction, InterFile);
        Manager.getInstance().putParam(Manager.Keyword.Gene_hasRowname, "true");
        Manager.getInstance().putParam(Manager.Keyword.Gene_hasColname, "true");
        Manager.getInstance().putParam(Manager.Keyword.Gene_hasName, "true");
        Manager.getInstance().putParam(Manager.Keyword.miRNA_hasRowname, "true");
        Manager.getInstance().putParam(Manager.Keyword.miRNA_hasColname, "true");
        Manager.getInstance().putParam(Manager.Keyword.miRNA_hasName, "true");
        Manager.getInstance().putParam(Manager.Keyword.Inter_hasRowname, "false");
        Manager.getInstance().putParam(Manager.Keyword.Inter_hasColname, "false");
        Manager.getInstance().putParam(Manager.Keyword.Inter_hasName, "false");
        Manager.getInstance().putParam(Manager.Keyword.LSOSS_R, "0");
        Manager.getInstance().putParam(Manager.Keyword.cor_R, "-0.6");
        Manager.getInstance().putParam(Manager.Keyword.Comfirm_Input, "1");
    }
}
