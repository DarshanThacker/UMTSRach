/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umts_rach;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.JTextArea;

/**
 *
 * @author thacker.d
 */
public class UMTS_RACH {
    /**
     * @param args the command line arguments
     */
    JTextArea statusJTextArea;
    StringBuffer sb;
    UMTS_RACH(){
        statusJTextArea=new JTextArea();
        sb = new StringBuffer("");
    }
    UMTS_RACH(JTextArea area){
        statusJTextArea = area;
        sb = new StringBuffer("");
        statusJTextArea.setText("");
        statusJTextArea.setVisible(true);
    }
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Number of Users : ");
        int nUser = sc.nextInt();
        System.out.println("Enter the time span : ");
        int timespan = sc.nextInt();
        new UMTS_RACH().startExperiment(nUser, timespan);
    }
    public SimulationResult startExperiment(int nUsers,int timeSpan) {
        sb.append("Experiment with "+nUsers+" For "+timeSpan+"seconds \n\n");
        int totalSubFrames = (timeSpan+1)*200; // 200 sub-Frame per sub-Frame.
        //Extra second has been added so that UE's which are starting their procedures at the end of 
        //the total time, they get time to complete the procedure.
        
        //Randomly Distributing Start time of RACH Proc for different UE's.
        
        UserEquipment UE[] = new UserEquipment[nUsers];
        ArrayList<Integer> transmitQueue[] = new ArrayList[totalSubFrames];
        ArrayList<WaitPair> listenQueue[] = new ArrayList[totalSubFrames];
        
        for(int i = 0; i < transmitQueue.length ; i++){
            transmitQueue[i] = new ArrayList<>();
        }
        for(int i = 0; i < listenQueue.length; i++){
            listenQueue[i] = new ArrayList<>();
        }
        
        //Initializing UE's with UE id & it's Corresponding ASC.
        for(int i = 0 ; i < UE.length ; i++)
        {
            //Ensuring 1:10 ratio for delay tolerant and delay sensitive devices.
            UE[i] = new UserEquipment(i,(i%4 == 0) ? 1 : 0);
            int temp;
            do{
                temp = (int)Math.floor(Math.random()*(200*timeSpan+1));
            }while(temp % UserEquipment.NUpPCH != UserEquipment.ASC_UPPCH.get(UE[i].ASC));
            UE[i].setStartFrame(temp);
            transmitQueue[temp].add(UE[i].id);
        }
        //Preparing to begin by taking a queue on for response.
        NodeB nodeB = new NodeB();     
        for(int i = 0; i < totalSubFrames; i++){
            SubFrame subFrame = new SubFrame(i);
            nodeB.setResponse(subFrame);
            //Printing ith Tranmit Queue
            sb.append("Transmit Queue "+i+" : ");
            
            for(int id : transmitQueue[i]){
                sb.append(id+" ");
            }
            sb.append("\n");
            
            for(int id : transmitQueue[i]){
                UE[id].performNextIteration(transmitQueue, listenQueue, subFrame);
            }
            for(WaitPair wp : listenQueue[i] ){
                UE[wp.getID()].listenResponse(transmitQueue, listenQueue, subFrame, wp);
            }
            nodeB.receiveSubFrame(subFrame);

            //Printing for debugging.

            //Printing Listen Queue.
            sb.append("Listen Queue "+i+" : ");
            
            for(WaitPair wp : listenQueue[i]){
                sb.append("<"+wp.getID()+", "+wp.getWait()+" > ,");
            }
            sb.append("\n");
            
            printSubFrame(subFrame);
            statusJTextArea.append(sb.toString());
            statusJTextArea.setCaretPosition(statusJTextArea.getDocument().getLength()-1);
            sb = new StringBuffer("");
        }
        int success = 0;
        int totalCollision = 0;
        int totalTransmitted = 0;
        for (UserEquipment UE1 : UE) {
            if (UE1.granted == true) {
                success++;
            }
            totalCollision+= UE1.collision;
            totalTransmitted += UE1.reqTrans;
        }
        double collision = ((double)totalCollision/totalTransmitted);
        double accessSuccess = ((double)success / nUsers);
        sb.append("Access Granted : "+success);
        System.out.printf("Collison Probability : %.2f\n",collision);
        return new SimulationResult(collision, accessSuccess);
    }
    void printSubFrame(SubFrame subFrame){
        sb.append("Sub Frame : "+subFrame.sfn+"\n");
        sb.append("FPACH : "+"\n");
       
        for(int i = 0; i < subFrame.FPACH.length; i++){
            if(subFrame.FPACH[i] == null) continue;
            sb.append("FPACH "+i+": <"+subFrame.FPACH[i].getSignatureRefNo()+", "+subFrame.FPACH[i].getRelativeSFN()+">,"+"\n");
        }
        sb.append("Transmitted Signature : "+"\n");
        for(int i = 0; i < subFrame.Tsignature.length; i++){
            sb.append(i+" : ");
            for(int id : subFrame.Tsignature[i]){
                sb.append(id+" ");
            }
            sb.append("\n");
        }
        sb.append("********"+"\n");
        sb.append("\n");
    }
}

