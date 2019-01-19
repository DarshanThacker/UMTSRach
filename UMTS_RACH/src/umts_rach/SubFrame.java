/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package umts_rach;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
/**
 *
 * @author thacker.d
 */
public class SubFrame{
    /*
        ArrayList signature[i] contains UE.id of Ue's which transmitted signatures with i.
        Array FPACH[i] indicates relative SFN and Signature Reference Number on ith FPACH.
    */
    int sfn;
    ArrayList<Integer>[] Tsignature; // Transmitted Signature.
    FPACHResponse[] FPACH;
    
    //Static Variables.
    static final int NFPACH;
    static Properties prop;
    
    static{
        prop = new Properties();
        InputStream input;
        try{
            input = new FileInputStream("config.properties");
            prop.load(input);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        NFPACH = Integer.parseInt(prop.getProperty("NFPACH"));
    }
    
    SubFrame(int sfn){
        this.sfn = sfn;
        Tsignature = new ArrayList[Integer.parseInt(prop.getProperty("NSignature"))];
    //    FPACH = new FPACHResponse[NFPACH];
        for(int i = 0; i < Tsignature.length; i++){
            Tsignature[i] = new ArrayList<>(1);
        }
    }
    
    //Method to be used by UE.
    public void transmit(int sign,int id){
        Tsignature[sign].add(id);
    }
    
    //Method to be used by NodeB.
    public ArrayList<Integer>[] getSignatures(){
        return Tsignature;
    }
    
    //Method to be used by NodeB.
    public void setResponses(FPACHResponse temp[]){
        this.FPACH = temp;
    }
    public int getSFN(){
        return sfn;
    }
}