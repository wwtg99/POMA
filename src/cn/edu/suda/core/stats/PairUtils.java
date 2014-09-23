/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.suda.core.stats;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class PairUtils {
    public static List<Pair> fromStringMatrix(StringMatrix sm, int col1, int col2){
        List<Pair> pairs = new ArrayList<>();
        for(int i = 0; i < sm.getDrow(); i++){
            pairs.add(new Pair(sm.getValue(i, col1), sm.getValue(i, col2)));
        }
        return pairs;
    }
    
    public static List<Pair> fromTabFile(String file) throws FileNotFoundException, IOException{
        List<Pair> pairs = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while((line = br.readLine()) != null){
                String[] ss = line.split("\t");
                if(ss.length > 1){
                    pairs.add(new Pair(ss[0], ss[1]));
                }
            }
        }
        return pairs;
    }
    
    public static String toString(List<Pair> pairs){
        StringBuilder sb = new StringBuilder();
        for(Pair p:pairs){
            sb.append(p.getP1()).append("\t").append(p.getP2()).append("\n");
        }
        return sb.toString();
    }
    
    public static String TripletsToString(List<Triplet> triplets){
        StringBuilder sb = new StringBuilder();
        for(Triplet t:triplets){
            sb.append(t.getT1()).append("\t").append(t.getT2()).append("\t").append(t.getT3()).append("\n");
        }
        return sb.toString();
    }
    
    public static List<Pair> overlap(List<Pair> pairs1, List<Pair> pairs2){
        List<Pair> result = new ArrayList<>();
        for(Pair p1:pairs1){
            for(Pair p2:pairs2){
                if(p1.equals(p2)){
                    result.add(p1);
                }
            }
        }
        return result;
    }
    
    public static List<Pair> overlap(List<Pair> pairs, String[] lists){
        List<Pair> result = new ArrayList<>();
        for(Pair p1:pairs){
            for(int i = 0; i < lists.length; i++){
                if(p1.getP1().toLowerCase().equals(lists[i].toLowerCase()) || p1.getP2().toLowerCase().equals(lists[i].toLowerCase())){
                    result.add(p1);
                }
            }
        }
        return result;
    }
    
    public static List<Pair> overlap(Map<String, InteractMap> map, String[] lists){
        List<Pair> result = new ArrayList<>();
        for(int i = 0; i < lists.length; i++){
            if(map.containsKey(lists[i].trim().toUpperCase())){
                InteractMap mirnas = map.get(lists[i].trim().toUpperCase());
                String[] s = mirnas.getMaps();
                for(int j = 0; j < s.length; j++){
                    result.add(new Pair(s[j], lists[i]));
                }
            }
        }
        return result;
    }
    
    public static List<Pair> overlap(Map<String, InteractMap> map, List<Pair> pair){
        List<Pair> result = new ArrayList<>();
        for(Pair p:pair){
            if(map.containsKey(p.getP2().toUpperCase())){
                InteractMap mirnas = map.get(p.getP2().toUpperCase());
                if(mirnas.contain(p.getP1().toUpperCase())){
                    result.add(p);
                }
            }
        }
        return result;
    }
    
    public static List<Pair> overlap(Map<String, InteractMap> map, String[] l1, String[] l2){
        List<Pair> result = new ArrayList<>();
        for(int i = 0; i < l1.length; i++){
            if(map.containsKey(l1[i].toUpperCase())){
                InteractMap mirnas = map.get(l1[i].toUpperCase());
                for(int j = 0; j < l2.length; j++){
                    if(mirnas.contain(l2[j].toUpperCase())){
                        result.add(new Pair(l2[j], l1[i]));
                    }
                }
            }
        }
        return result;
    }
    
    public static Map<String, Integer> count(List<Pair> pairs, boolean isFirst){
        Map<String, Integer> num = new HashMap<>();
        for(int i = 0; i < pairs.size(); i++){
            if(isFirst){
                String str = pairs.get(i).getP1();
                if(num.containsKey(str)){
                    int n = num.get(str);
                    num.put(str, n + 1);
                }else{
                    num.put(str, 1);
                }
            }else{
               String str = pairs.get(i).getP2();
                if(num.containsKey(str)){
                    int n = num.get(str);
                    num.put(str, n + 1);
                }else{
                    num.put(str, 1);
                } 
            }
        }
        return num;
    }
    
    public static List<Triplet> countNOD(List<Pair> pairs, Map<String, Integer> num, boolean isFirst){
        Map<String, Integer> nod = new HashMap<>();
        Map<String, String> nodOnlyName = new HashMap<>();
        List<String> onlyone = new ArrayList<>();
        for(String key:num.keySet()){
            int value = num.get(key);
            if(value == 1){
                onlyone.add(key);
            }
        }
        for(Pair p:pairs){
            if(isFirst){
                String str = p.getP2();
                if(isInOnlyOne(p.getP1(), onlyone)){
                    String onlyName = "";
                    if(nodOnlyName.containsKey(str)){
                        onlyName = nodOnlyName.get(str) + "\t";
                    }
                    onlyName += p.getP1();
                    nodOnlyName.put(str, onlyName);
                    if(nod.containsKey(str)){
                        int value = nod.get(str);
                        nod.put(str, value + 1);
                    }else{
                        nod.put(str, 1);
                    }
                }
            }else{
                String str = p.getP1();
                if(isInOnlyOne(p.getP2(), onlyone)){
                    String onlyName = "";
                    if(nodOnlyName.containsKey(str)){
                        onlyName = nodOnlyName.get(str) + "\t";
                    }
                    onlyName += p.getP2();
                    nodOnlyName.put(str, onlyName);
                    if(nod.containsKey(str)){
                        int value = nod.get(str);
                        nod.put(str, value + 1);
                    }else{
                        nod.put(str, 1);
                    }
                }
            }
        }
        List<Triplet> nodList = new ArrayList<>();
        for(String key:nod.keySet()){
            nodList.add(new Triplet(key, String.valueOf(nod.get(key)), nodOnlyName.get(key)));
        }
        return nodList;
    }
    
    private static boolean isInOnlyOne(String str, List<String> onlyone){
        for(String s:onlyone){
            if(s.equals(str)){
                return true;
            }
        }
        return false;
    }
}
