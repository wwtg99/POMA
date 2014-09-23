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
import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author Wentao Wu
 */
public abstract class VisualNode extends Group{
    double r;
    Node node;
    Shape shape;
    Text text;
    Color color = Color.BLACK;
    List<VisualEdge> lineFrom, lineTo;
    private static Point2D dragAnchor;
    private static double initX, initY;
    private int index = 0;
    private static final int ANIMATIONINV = 10;
    
    public VisualNode(Node node, double r){
        this.node = node;
        this.r = r;
        lineFrom = new ArrayList<>();
        lineTo = new ArrayList<>();
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        shape.setScaleX(1);
        shape.setScaleY(1);
        this.r = r;
    }

    public double getX() {
        return shape.getTranslateX();
    }

    public void setX(double x) {
        shape.setTranslateX(x);
        text.setTranslateX(x - r);
        for(VisualEdge l:lineFrom){
            l.setStartX(x);
        }
        for(VisualEdge l:lineTo){
            l.setEndX(x);
        }
    }

    public double getY() {
        return shape.getTranslateY();
    }

    public void setY(double y) {
        shape.setTranslateY(y);
        text.setTranslateY(y);
        for(VisualEdge l:lineFrom){
            l.setStartY(y);
        }
        for(VisualEdge l:lineTo){
            l.setEndY(y);
        }
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Shape getShape() {
        return shape;
    }

    public Text getText() {
        return text;
    }

    public void setColor(Paint paint) {
        if(paint instanceof Color){
            shape.setFill(new RadialGradient(0, 0, 0.2, 0.3, 1, true, CycleMethod.NO_CYCLE, new Stop[] {new Stop(0, Color.rgb(250,250,255)),new Stop(1, (Color)paint)}));
        }else{
            shape.setFill(paint);
        }
    }

    public void setFontSize(int size) {
        text.setFont(Font.font(text.getFont().getName(), size));
    }

    public void addStartEdge(VisualEdge edge) {
        lineFrom.add(edge);
    }

    public void addEndEdge(VisualEdge edge) {
        lineTo.add(edge);
    }
    
    public void zoom(float f){
        this.setScaleX(f);
        this.setScaleY(f);
    }

    public void changeColor(final Color c) {
        if(color.equals(c)) return;
        FillTransition ft = new FillTransition(Duration.millis(1000), shape, color, c);
        ft.setOnFinished(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                setColor(c);
            }           
        });
        ft.play();
    }

    public void changeSize(final double newr) {
        if(Math.abs(getR() - newr) < 0.1) return;
        float f = (float) (newr / getR()) - 1;
        ScaleTransition st = new ScaleTransition(Duration.millis(1000), shape);
        st.setByX(f);
        st.setByY(f);
        st.setOnFinished(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent t) {
                setR(newr);
                text.setWrappingWidth(r * 2);
            }            
        });
        st.play();
    }

    public void changePosition(double x, double y) {
        if(index != 0){
            return;
        }
        final double invx = (x - getX()) / ANIMATIONINV;
        final double invy = (y - getY()) / ANIMATIONINV;
        AnimationTimer timer = new AnimationTimer(){

            @Override
            public void handle(long l) {
                setX(getX() + invx);
                setY(getY() + invy);
                index++;
                if(index >= ANIMATIONINV){
                    index = 0;
                    stop();
                }
            }
            
        };
        timer.start();
    }
    
    protected void activeDrag(final Shape shape){
        shape.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                //the event will be passed only to the circle which is on front
                me.consume();
            }
        });
        text.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                //the event will be passed only to the circle which is on front
                me.consume();
            }
        });
        shape.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                double dragX = me.getSceneX() - dragAnchor.getX();
                double dragY = me.getSceneY() - dragAnchor.getY();
                //calculate new position of the circle
                double newXPosition = initX + dragX;
                double newYPosition = initY + dragY;
                setX(newXPosition);
                setY(newYPosition);
            }
        });
        text.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                double dragX = me.getSceneX() - dragAnchor.getX();
                double dragY = me.getSceneY() - dragAnchor.getY();
                //calculate new position of the circle
                double newXPosition = initX + dragX;
                double newYPosition = initY + dragY;
                setX(newXPosition);
                setY(newYPosition);
            }
        });
        shape.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                //change the z-coordinate of the circle
                shape.toFront();
                text.toFront();
            }
        });
        shape.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                 //when mouse is pressed, store initial position
                initX = shape.getTranslateX();
                initY = shape.getTranslateY();
                dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());               
            }
        });
        text.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                 //when mouse is pressed, store initial position
                initX = shape.getTranslateX();
                initY = shape.getTranslateY();
                dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());               
            }
        });
    }
}
