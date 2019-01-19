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
public class WaitPair {
    int id; // Indicates ID of UE.
    int wait; // unit subFrame.
    WaitPair(int id,int wait){
        this.id = id;
        this.wait = wait;
    }
    public int getWait(){
        return wait;
    }
    public int getID(){
        return id;
    }
}
