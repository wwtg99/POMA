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

import javafx.scene.Cursor;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author Wentao Wu
 */
public class CircleNode extends VisualNode {

    public CircleNode(Node node, double r, Color color){
        super(node, r);
        shape = new Circle(r, new RadialGradient(0, 0, 0.2, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop[] {
            new Stop(0, Color.rgb(250,250,255)),
            new Stop(1, color)
        }));
        shape.setEffect(new InnerShadow(7, color.darker().darker()));
        text = new Text(node.toString());
        text.setWrappingWidth(r * 2);
        text.setTextAlignment(TextAlignment.CENTER);
        shape.setCursor(Cursor.HAND);
        text.setCursor(Cursor.HAND);
        this.getChildren().add(shape);
        this.getChildren().add(text);
        activeDrag(shape);
    }

    @Override
    public void setR(double r) {
        super.setR(r);
        ((Circle)shape).setRadius(r);
    }
   
}
