/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package umts_rach;

import java.io.*;
import java.util.*;

/**
 *
 * @author thacker.d
 */
public class NodeB {
    Queue<FPACHResponse> response[];
    static Properties prop;
    static final int NFPACH;
    static final int Li;
    static final int prachToFpach;
    
    static{
        prop = new Properties();
        InputStream input;
        try{
            input = new FileInputStream("config.properties");
            prop.load(input);
        }
        catch(IOException ie){
            ie.printStackTrace();
        }
        NFPACH = Integer.parseInt(prop.getProperty("NFPACH"));
        Li = Integer.parseInt(prop.getProperty("Li"));
        prachToFpach = Integer.parseInt(prop.getProperty("PRACHtoFPACH"));
    }
    
    NodeB(){
        //Initializing Response Queues which in with ith queue indicates
        // ith FPACH holding signatures and Subframe Reference Number.
        response = new Queue[NFPACH];
        for(int i = 0; i < response.length ; i++){
            response[i] = new LinkedList<FPACHResponse>();
        }
    }
    public void receiveSubFrame(SubFrame currSubFrame){
        ArrayList<Integer> Rsignature[] = currSubFrame.getSignatures();
        for(int i = 0; i < Rsignature.length; i++){
            if(Rsignature[i].size() == 1){
                response[i%NFPACH].add(new FPACHResponse(i,currSubFrame.sfn)); // Here, CurrSubFrame.sfn is used insted of relative SFN which should be taken into cosideration.
            }
        }
    }
    public void setResponse(SubFrame currSubFrame){
        FPACHResponse[] FPACH = new FPACHResponse[NFPACH];
        if(currSubFrame.sfn % Li >= prachToFpach) return;
        for(int i = 0; i < response.length; i++){
            if(!response[i].isEmpty()){
                FPACHResponse temp = response[i].poll();
                FPACH[i] = new FPACHResponse(i,currSubFrame.getSFN()- temp.getRelativeSFN() );
            }
        }
        currSubFrame.setResponses(FPACH);
    }
}
