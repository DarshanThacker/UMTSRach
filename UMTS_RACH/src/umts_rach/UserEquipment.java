/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umts_rach;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.HashMap;
/**
 *
 * @author thacker.d
 */

public class UserEquipment{
    int id;
    int ASC;
    int startFrame;
    int M;
    double Pi;
    int pltn;
    int signatureTransmitted;
    int transmittedSubFrame;
    public int collision;
    public int reqTrans;
    boolean granted;
    
    static final int PLTN; //Physical Layer Transmission Number.
    static final int TTI ; //Time Transmission Interval.
    static final int Mmax; // MAC Layer Transmission Number.
    static final int NUpPCH; // Number of UpLink SubChannels.
    static final int NFPACH;
    static Properties prop; // For Getting Property files.
    
    static final HashMap<Integer,Integer> ASC_UPPCH = new HashMap<Integer,Integer>(); 
    static {
        ASC_UPPCH.put(0,0);
        ASC_UPPCH.put(1,1);
        //Getting Properties from config.properties.
        prop = new Properties();
        InputStream input;
        try{
            input = new FileInputStream("config.properties");
            prop.load(input);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        PLTN = Integer.parseInt(prop.getProperty("Phy_Transmission"));
        TTI = Integer.parseInt(prop.getProperty("TTI"));
        Mmax = Integer.parseInt(prop.getProperty("Mmax"));
        NUpPCH = Integer.parseInt(prop.getProperty("NUpPCH_Sub"));
        NFPACH = Integer.parseInt(prop.getProperty("NFPACH"));
    }
    
    UserEquipment(int id,int ASC){
        this.id = id;
        this.ASC = ASC;
        this.granted = false;
        M = Mmax;
        if(this.ASC == 1){
            Pi = 1; // For Delay Sensitive Devices.
        }
        else{
            Pi = Double.parseDouble(prop.getProperty("Pi"));
        }
        
        pltn = 0;
        signatureTransmitted = -1;
        transmittedSubFrame = -1;
    }
    
    //Test with random value and Pi.
    private boolean randomTest(){
        return (Math.random() <= Pi);
    }
    
    void performNextIteration(ArrayList<Integer> transmitQueue[],ArrayList<WaitPair> listenQueue[],SubFrame subFrame){
        int T2 = TTI / 5;
        if(pltn != 0){
            transmitSignature(transmitQueue,listenQueue,subFrame);
        }
        else if(M != 0){
            if(randomTest()){
                pltn = PLTN;
                transmitSignature(transmitQueue,listenQueue,subFrame);
                M--;
            }
            else{
                pltn = 0;
                WaitForSubFrame(T2, transmitQueue, listenQueue, subFrame);
            }
        }
    }
    private void transmitSignature(ArrayList<Integer>[] transmitQueue, ArrayList<WaitPair>[] listenQueue, SubFrame subFrame) {
        pltn--;
        int nSignature = Integer.parseInt(prop.getProperty("NSignature"));
        int selectedSignature = (int)Math.floor(Math.random()*(nSignature));
        subFrame.transmit(selectedSignature,this.id);
        signatureTransmitted = selectedSignature;
        transmittedSubFrame  = subFrame.getSFN();
        listenQueue[subFrame.getSFN()+1].add(new WaitPair(this.id,Integer.parseInt(prop.getProperty("WT"))));
        reqTrans++;
    }
    
    public void listenResponse(ArrayList<Integer>[] transmitQueue,ArrayList<WaitPair>[] listenQueue,SubFrame subFrame,WaitPair wp){
        if(signatureTransmitted == -1) return;
        FPACHResponse FPACHi = subFrame.FPACH[signatureTransmitted % NFPACH];
        
        if(FPACHi != null && FPACHi.getRelativeSFN() == subFrame.getSFN() - transmittedSubFrame){
            granted = true;
        }
        else{
            int T2 = TTI/5;
            if(wp.getWait() == 1){
                int temp = 0;
                while((subFrame.getSFN()+1+temp) % NUpPCH != ASC_UPPCH.get(this.ASC)){
                    temp++;
                }
                transmitQueue[subFrame.getSFN()+1+temp+T2].add(this.id);
                collision++;
            }
            else{
                listenQueue[subFrame.getSFN()+1].add(new WaitPair(this.id,wp.getWait()-1));
            }
        }
    }
    public boolean transmittedSign(){
        return granted;
    }
 /*   public void MACLayerNextIteration(ArrayList<Integer> transmitQueue[],ArrayList<WaitPair> listenQueue[],SubFrame subFrame){
       
        if(Mmax == 0){
            return;
        }
        int T2 = TTI / 5;
        double random = Math.random();
        if(random <= Pi){
            int response = phyLayerNextInteration(transmitQueue,listenQueue,subFrame);
            if(response == 0){
                WaitForSubFrame(T2,transmitQueue,listenQueue,subFrame);
            }
            else{
                /*
                    Procedure Successful.
                    If Any Action has to be performed, it has to be here.
                
            }
        }
        else{
            WaitForSubFrame(T2,transmitQueue,listenQueue,subFrame);
        }
    }
    
    public int phyLayerNextInteration(ArrayList<Integer> transmitQueue[],ArrayList<WaitPair> listenQueue[],SubFrame subFrame){
        /*
            Response Formate :
            0 Indicates NACK
            1 Indicates ACK
        
        if(pltn == 0){
            //some how call next macLayer Procedure.
        }
        int nSignature = Integer.parseInt(prop.getProperty("NSignature"));
        int selectedSignature = (int)Math.floor(Math.random()*(nSignature));
        return 0;
    }*/
    public void WaitForSubFrame(int sf,ArrayList<Integer> transmitQueue[],ArrayList<WaitPair> listenQueue[],SubFrame subFrame){
        // 1 is added to maintain the gap of sf.
        int temp = 0;
        while((subFrame.getSFN()+1+sf+temp) % NUpPCH != ASC_UPPCH.get(this.ASC)){
            temp++;
        }
        transmitQueue[subFrame.getSFN()+1+sf+temp].add(this.id);
    }
    
    public void setStartFrame(int fn){
        this.startFrame = fn;
    }
}
