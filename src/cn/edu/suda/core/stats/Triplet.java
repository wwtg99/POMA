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

import java.util.Objects;

/**
 *
 * @author Wentao Wu
 */
public class Triplet {

    private String t1, t2, t3;
    
    public Triplet(String t1, String t2, String t3){
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
    }
    
    public Pair toPair(int p1, int p2){
        String str1, str2;
        switch(p1){
            case 1: str1 = t1;break;
            case 2: str1 = t2;break;
            case 3: str1 = t3;break;
            default: str1 = "";
        }
        switch(p2){
            case 1: str2 = t1;break;
            case 2: str2 = t2;break;
            case 3: str2 = t3;break;
            default: str2 = "";
        }
        return new Pair(str1, str2);
    }

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    public String getT3() {
        return t3;
    }

    public void setT3(String t3) {
        this.t3 = t3;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.t1);
        hash = 59 * hash + Objects.hashCode(this.t2);
        hash = 59 * hash + Objects.hashCode(this.t3);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Triplet other = (Triplet) obj;
        if (!Objects.equals(this.t1, other.t1)) {
            return false;
        }
        if (!Objects.equals(this.t2, other.t2)) {
            return false;
        }
        if (!Objects.equals(this.t3, other.t3)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Triplet{" + "t1=" + t1 + ", t2=" + t2 + ", t3=" + t3 + '}';
    }
    
    
}
