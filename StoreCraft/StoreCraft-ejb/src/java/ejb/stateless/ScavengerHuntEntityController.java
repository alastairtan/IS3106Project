/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author shawn
 */
@Stateless
@Local (ScavengerHuntEntityControllerLocal.class)

public class ScavengerHuntEntityController implements ScavengerHuntEntityControllerLocal {

    @PersistenceContext(unitName = "StoreCraft-ejbPU")
    private EntityManager em;

    public ScavengerHuntEntityController() {
    }
    
    // keep track of all the winner
    
    
}
