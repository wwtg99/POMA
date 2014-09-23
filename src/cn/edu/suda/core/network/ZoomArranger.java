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
public class ZoomArranger implements Arranger {

    private static ZoomArranger instance = null;
    private static float oldzoom = 1, newzoom = 1;
    
    private ZoomArranger(){
        
    }
    
    public static ZoomArranger getInstance(float z){
        if(instance == null){
            instance = new ZoomArranger();
        }
        oldzoom = newzoom;
        newzoom = z;
        return instance;
    }
    
    @Override
    public void arrange(List<VisualNode> nodes, double MAXR, double MINR, double width, double height) {
        for(VisualNode node:nodes){
            node.setX(node.getX() * (newzoom - oldzoom + 1));
            node.setY(node.getY() * (newzoom - oldzoom + 1));
        }
    }

}
