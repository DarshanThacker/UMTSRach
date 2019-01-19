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
public class SimulationResult {
    double collision;
    double accessSuccess;
    SimulationResult(double collision,double accessSuccess){
        this.collision = collision;
        this.accessSuccess = accessSuccess;
    }
}
