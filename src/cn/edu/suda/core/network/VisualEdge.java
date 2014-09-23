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

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 *
 * @author Wentao Wu
 */
public abstract class VisualEdge extends Group{
    Line line;
    Color color = Color.BLACK;
    int fromID;
    int toID;
    
    public VisualEdge(int fromID, int toID){
        this.fromID = fromID;
        this.toID = toID;
    }

    public double getStartX() {
        return line.getStartX();
    }

    public void setStartX(double x) {
        this.line.setStartX(x);
    }

    public double getStartY() {
        return line.getStartY();
    }

    public void setStartY(double y) {
        this.line.setStartY(y);
    }

    public double getEndX() {
        return line.getEndX();
    }

    public void setEndX(double x) {
        this.line.setEndX(x);
    }

    public double getEndY() {
        return line.getEndY();
    }

    public void setEndY(double y) {
        this.line.setEndY(y);
    }

    public Shape getShape() {
        return line;
    }

    public void setColor(Paint paint) {
        line.setFill(paint);
    }

    public Color getColor() {
        return color;
    }

    public int getFromID() {
        return fromID;
    }

    public void setFromID(int fromID) {
        this.fromID = fromID;
    }

    public int getToID() {
        return toID;
    }

    public void setToID(int toID) {
        this.toID = toID;
    }

}
