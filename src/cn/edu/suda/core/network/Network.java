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

package cn.edu.suda.core.network;

import cn.edu.suda.core.network.Node.NodeType;
import cn.edu.suda.core.stats.Pair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wentao Wu
 */
public class Network implements Serializable {

    private List<Node> nodes = new ArrayList<>();
    private int[][] am;
    
    public Network(int[][] am, List<Node> nodes){
        this.am = am;
        this.nodes = nodes;
    }
    
    public int getNodeNum(){
        return nodes.size();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public int[][] getAm() {
        return am;
    }

    public void setAm(int[][] am) {
        this.am = am;
    }
    
    public Node getNodeAt(int n){
        if(n >= nodes.size() || n < 0){
            return null;
        }else{
            return nodes.get(n);
        }
    }
    
    public static Network build(List<Pair> pairs){
        Map<String, Integer> nodeMap = new HashMap<>();
        List<Node> nodes = new ArrayList<>();
        int index = 0;
        for(Pair p:pairs){
            if(!nodeMap.containsKey(p.getP1())){
                nodeMap.put(p.getP1(), index++);
                nodes.add(new Node(p.getP1(), NodeType.miRNA));
            }
            if(!nodeMap.containsKey(p.getP2())){
                nodeMap.put(p.getP2(), index++);
                nodes.add(new Node(p.getP2(), NodeType.Gene));
            }
        }
        
        int[][] am = new int[nodes.size()][nodes.size()];
        for(Pair p:pairs){
            int p1n = nodeMap.get(p.getP1());
            int p2n = nodeMap.get(p.getP2());
            if(p1n < p2n){
                am[p1n][p2n] = 1;
            }else{
                am[p2n][p1n] = 1;
            }
        }
        return new Network(am, nodes);
    }
}
