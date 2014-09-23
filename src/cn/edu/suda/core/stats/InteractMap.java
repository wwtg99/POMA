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

package cn.edu.suda.core.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Wentao Wu
 */
public class InteractMap {

    private String name;//gene name
    private Map<String, Short> map = new HashMap<>();//miRNA names
    
    public InteractMap(String name){
        this.name = name;
    }
    
    public void add(String name){
        map.put(name, Short.MIN_VALUE);
    }
    
    public boolean contain(String name){
        return map.containsKey(name);
    }
    
    public String[] getMaps(){        
        Set<String> s = map.keySet();
        String[] names = new String[s.size()];
        names = s.toArray(names);
        return names;
    }
    
    public void clear(){
        map.clear();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
