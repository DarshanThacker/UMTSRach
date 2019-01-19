/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umts_rach;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static umts_rach.Simulator_GUI.prop;
import java.util.Properties;
/**
 *
 * @author thacker.d
 */
public class ConfigTest {
    public static void main(String[] args){
        Properties newProp = new Properties();
         OutputStream out = null;
        try{
            out = new FileOutputStream("config.properties");
            newProp.setProperty("Mmax","2");
            newProp.setProperty("NSignature","8");
            newProp.setProperty("Pi", "1");
            newProp.setProperty("NUpPCH_Sub","2");
            newProp.setProperty("NFPACH","8");
            newProp.setProperty("PRACHtoFPACH","8");
            newProp.setProperty("SF","4");
            newProp.setProperty("TTI", "20");
            newProp.setProperty("WT", "4");
            newProp.setProperty("Phy_Transmission","4");
            newProp.setProperty("Li","1");
            
            newProp.store(out,null);
            
        }catch (IOException ie) {
            ie.printStackTrace();
        }
        finally{
            try{
                out.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
