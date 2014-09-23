/*
 * <Amino Acid Network Worker (AANW) is a useful AA network construction tool.>
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

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;

/**
 *
 * @author Wentao Wu
 */
public class NetworkView extends Group{
    private static final int R = 20;
    private Color geneColor = Color.GREEN, miRNAColor = Color.BLUE;
    private float zoom = 1;
    private int fontSize = 10;
    private double width, height;
    private Network network;
    private Rectangle rect;
    private List<VisualNode> nodes = new ArrayList<>();
    private List<VisualEdge> edges = new ArrayList<>();
    private Arranger arranger = MatrixArranger.getInstance();
    private boolean animationOn = true;
    
    public NetworkView(){
        
    }
    
    public void loadNetwork(Network network){
        this.network = network;
        this.getChildren().clear();
        int n = network.getNodes().size();
        createBg(n);
        for(Node node:network.getNodes()){
            VisualNode vn = new CircleNode(node, R, getColor(node));
            vn.setFontSize(fontSize);
            nodes.add(vn);
        }
        
        for(int i = 0; i < n - 1; i++){
            for(int j = i + 1; j < n; j++){
                if(network.getAm()[i][j] != 0){
                    VisualEdge ve = new SolidEdge(i, j);
                    edges.add(ve);
                    nodes.get(i).addStartEdge(ve);
                    nodes.get(j).addEndEdge(ve);
                }
            }
        }

        this.getChildren().add(rect);
        for(VisualEdge ve:edges){
            this.getChildren().add(ve);
        }
        for(VisualNode vn:nodes){
            this.getChildren().add(vn);
        }       
        
        arranger.arrange(nodes, R * zoom, R * zoom, width, height);
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        if(zoom < 0.5 || zoom > 2) return;
        this.zoom = zoom;
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setTranslateX(0);
        rect.setTranslateY(0);
        for(VisualNode node:nodes){
            node.zoom(zoom);
        }
        arranger = ZoomArranger.getInstance(zoom);
        arranger.arrange(nodes, R * zoom, R * zoom, width, height);
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        for(VisualNode node:nodes){
            node.setFontSize(fontSize);
        }
    }

    public double getWidth() {
        return width * zoom;
    }

    public double getHeight() {
        return height * zoom;
    }

    public Color getGeneColor() {
        return geneColor;
    }

    public void setGeneColor(Color geneColor) {
        this.geneColor = geneColor;
    }

    public Color getMiRNAColor() {
        return miRNAColor;
    }

    public void setMiRNAColor(Color miRNAColor) {
        this.miRNAColor = miRNAColor;
    }

    public Network getNetwork() {
        return network;
    }

    public Arranger getArranger() {
        return arranger;
    }

    public void setArranger(Arranger arranger) {
        this.arranger = arranger;
        arranger.arrange(nodes, R * zoom, R * zoom, width, height);
    }

    public List<VisualNode> getNodes() {
        return nodes;
    }

    public List<VisualEdge> getEdges() {
        return edges;
    }

    public boolean isAnimationOn() {
        return animationOn;
    }

    public void setAnimationOn(boolean animationOn) {
        this.animationOn = animationOn;
    }
    
    public void changeColor(){
        if(animationOn){
            for(VisualNode node:nodes){               
                node.changeColor(getColor(node.getNode()));                   
            }
        }else{
            for(VisualNode node:nodes){
                node.setColor(getColor(node.getNode()));
            }
        }
    }
    
    private void createBg(int n){
        if(n < 100){
            width = 3000;
            height = 3000;
        }else{
            width = (int)(n / 100) * 3500;
            height = (int)(n / 100) * 3500;
        }
        rect = RectangleBuilder.create()
            .width(width).height(height)
            .fill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop[] {
                new Stop(1, Color.rgb(156,216,255)),
                new Stop(0, Color.rgb(156,216,255, 0.5))
            })).stroke(Color.BLACK).build();
    }
    
    private Color getColor(Node node){
        switch(node.getType()){
            case Gene:return geneColor;
            case miRNA:return miRNAColor;
            default:return Color.BLACK;
        }
    }
}
