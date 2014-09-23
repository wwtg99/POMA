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

import java.util.List;

/**
 *
 * @author Wentao Wu
 */
public class MatrixArranger implements Arranger {

    private static MatrixArranger instance = null;
    private static boolean animationOn = true;
    
    private MatrixArranger(){
        
    }
    
    public static MatrixArranger getInstance(){
        if(instance == null){
            instance = new MatrixArranger();
        }
        return instance;
    }

    public static boolean isAnimationOn() {
        return animationOn;
    }

    public static void setAnimationOn(boolean animationOn) {
        MatrixArranger.animationOn = animationOn;
    }
    
    @Override
    public void arrange(List<VisualNode> nodes, double MAXR, double MINR, double width, double height) {
        int DEFAULTX = (int)MAXR * 2, DEFAULTY = (int)MAXR * 2;
        
        int x = DEFAULTX, y = DEFAULTY;
        int n = nodes.size();
        int maxcol = (int)(width / (MAXR * 3));
        int nodeEachRow = (int)Math.sqrt(n);       
        if(nodeEachRow < 6){
            nodeEachRow = 6;
        }else if(nodeEachRow > maxcol){
            nodeEachRow = maxcol;
        }
        int col = 0;
        for(VisualNode node:nodes){
            if(animationOn){
                node.changePosition(x, y);
            }else{
                node.setX(x);
                node.setY(y);
            }
            x += MAXR * 3;
            col++;
            if(col >= nodeEachRow){
                x = DEFAULTX;
                y += MAXR * 3;
                col = 0;
            }
        }
    }

}
