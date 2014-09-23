/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.suda.core.stats;

import java.util.Objects;

/**
 *
 * @author Administrator
 */
public class Pair {
    private String p1, p2;
    
    public Pair(String p1, String p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.p1);
        hash = 11 * hash + Objects.hashCode(this.p2);
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
        final Pair other = (Pair) obj;
        if (!Objects.equals(this.p1.toUpperCase(), other.p1.toUpperCase())) {
            return false;
        }
        if (!Objects.equals(this.p2.toUpperCase(), other.p2.toUpperCase())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pair{" + "p1=" + p1 + ", p2=" + p2 + '}';
    }
    
    
}
