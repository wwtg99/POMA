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

package cn.edu.suda.gui;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 *
 * @author Wentao Wu
 */
public class LogHandler extends Handler{

    private static final int MAXIMUM_DOCUMENT_SIZE = 524288; // 0.5 MB
    private final TextArea text;
    private final Label label;
    
    public LogHandler(TextArea text, Label label){
        this.text = text;
        this.label = label;
    }
    
    @Override
    public void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }

        StringBuilder message = new StringBuilder();

        if (record.getLevel() == Level.SEVERE) {
            message.append("ERROR: ").append(record.getMessage());
        } else if(record.getLevel() == Level.WARNING) {
            message.append("WARNING: ").append(record.getMessage());
        } else{
            message.append("INFO: ").append(record.getMessage());
        }
        
        //label.setText(message.toString());
        
        synchronized(text) {
            
            if (text.getText().length() >= MAXIMUM_DOCUMENT_SIZE) {
        	text.setText("");
            }
            message.append("\n").insert(0, text.getText());
            text.setText(message.toString());
        }   
        
    }

    @Override
    public void flush() {
        
    }

    @Override
    public void close() throws SecurityException {
        
    }

}
