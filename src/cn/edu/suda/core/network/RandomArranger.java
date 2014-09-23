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
public class RandomArranger implements Arranger {

    private static RandomArranger instance = null;
    private static int w, h;
    private static boolean animationOn = true;
    
    private RandomArranger(){
        
    }
    
    public static RandomArranger getInstance(int width, int height){
        if(instance == null){
            instance = new RandomArranger();
        }
        w = width;
        h = height;
        return instance;
    }

    public static boolean isAnimationOn() {
        return animationOn;
    }

    public static void setAnimationOn(boolean animationOn) {
        RandomArranger.animationOn = animationOn;
    }
    
    @Override
    public void arrange(List<VisualNode> nodes, double MAXR, double MINR, double width, double height) {
        for(VisualNode node:nodes){
            double x = Math.random() * w + MAXR;
            double y = Math.random() * h + MAXR;
            if(animationOn){
                node.changePosition(x, y);
            }else{
                node.setX(x);
                node.setY(y);
            }
        }
    }

}
