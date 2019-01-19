/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umts_rach;

/**
 *
 * @author thacker.d
 */
public class TransmitPair {
    int id;
    boolean isPhy;
    TransmitPair(int id,boolean isPhy){
        this.id = id;
        this.isPhy = isPhy;
    }
    public boolean isPhy(){
        return isPhy;
    }
    public int getId(){
        return id;
    }
}
