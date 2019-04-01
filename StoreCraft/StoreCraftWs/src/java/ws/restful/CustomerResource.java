/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.CustLoginRsp;
import datamodel.ws.rest.ErrorRsp;
import ejb.stateless.CustomerEntityControllerLocal;
import entity.CustomerEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InvalidLoginCredentialException;

/**
 * REST Web Service
 *
 * @author shawn
 */
@Path("Customer")
public class CustomerResource {

    CustomerEntityControllerLocal customerEntityController = lookupCustomerEntityControllerLocal();

    @Context
    private UriInfo context;
    
    private CustomerEntityControllerLocal customerEntityControllerLocal;
    
    

    /**
     * Creates a new instance of CustomerResource
     */
    public CustomerResource() {
        this.customerEntityControllerLocal = lookupCustomerEntityControllerLocal();
    }


    @Path("customerLogin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerLogin(@QueryParam("email") String email, @QueryParam("password") String password) {
        try {
            CustomerEntity customerEntity = customerEntityControllerLocal.customerLogin(email, password);
            customerEntity.setPassword(null);
            customerEntity.setSalt(null);
            //customerEntity.getSaleTransactionEntities().clear();
            //customerEntity.getReviewEntities().clear();
            //customerEntity.getDiscountCodeEntities().clear();

            
            CustLoginRsp response = new CustLoginRsp(customerEntity);
            
            return Response.status(Status.OK).entity(response).build();
    
        } catch (InvalidLoginCredentialException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.UNAUTHORIZED).entity(errorRsp).build();
        } catch(Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            System.out.println("*******************ERRORR*****");
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
            
    }

    /**
     * PUT method for updating or creating an instance of CustomerResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    private CustomerEntityControllerLocal lookupCustomerEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerEntityControllerLocal) c.lookup("java:global/StoreCraft/StoreCraft-ejb/CustomerEntityController!ejb.stateless.CustomerEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
