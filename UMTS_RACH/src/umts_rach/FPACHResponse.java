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
public class FPACHResponse {
    private int signatureRefNo;
    private int relativeSFN; // Relative SubFrame Number

    public FPACHResponse(int signatureRefNo, int relativeSFN) {
        this.signatureRefNo = signatureRefNo;
        this.relativeSFN = relativeSFN;
    }
    
    public int getSignatureRefNo(){
        return signatureRefNo;
    }
    
    public int getRelativeSFN(){
        return relativeSFN;
    }
    
}
