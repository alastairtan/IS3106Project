/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.RetrieveDiscountCodesRsp;
import ejb.stateless.DiscountCodeEntityControllerLocal;
import entity.DiscountCodeEntity;
import entity.ProductEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.CustomerNotFoundException;

/**
 * REST Web Service
 *
 * @author shawn
 */
@Path("DiscountCode")
public class DiscountCodeResource {

    DiscountCodeEntityControllerLocal discountCodeEntityControllerLocal = lookupDiscountCodeEntityControllerLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DiscountCodeResource
     */
    public DiscountCodeResource() {
    }

    @Path("retrieveDiscountCodesForCustomer")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveDiscountCodesForCustomer(@QueryParam("customerId") Long customerId) {
        try {
            List<DiscountCodeEntity> discountCodes = discountCodeEntityControllerLocal.retrieveDiscountCodesForCustomer(customerId);

            for (DiscountCodeEntity dce : discountCodes) {
                if (dce.getCustomerEntities() != null) {
                    dce.getCustomerEntities().clear();
                }
                System.out.println("** DC 1 **");
                if (dce.getProductEntities() != null) {
                    for (ProductEntity p : dce.getProductEntities()) {
                        if (p.getDiscountCodeEntities() != null) {
                            p.getDiscountCodeEntities().clear();
                        }
                        p.getCategoryEntity().getProductEntities().clear();
                        p.getCategoryEntity().setParentCategoryEntity(null);
                        p.getCategoryEntity().setSubCategoryEntities(null);
                        if (p.getReviewEntities() != null) {
                            p.getReviewEntities().clear();
                        }
                        if (p.getTagEntities() != null) {
                            p.getTagEntities().clear();
                        }
                    }
                }
                
                System.out.println("** DC 2 **");
            }

            RetrieveDiscountCodesRsp rsp = new RetrieveDiscountCodesRsp(discountCodes);
            System.out.println("** DC 3 **");
            return Response.status(Response.Status.OK).entity(rsp).build();

        } catch (CustomerNotFoundException ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }

    }

    /**
     * PUT method for updating or creating an instance of DiscountCodeResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    private DiscountCodeEntityControllerLocal lookupDiscountCodeEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (DiscountCodeEntityControllerLocal) c.lookup("java:global/StoreCraft/StoreCraft-ejb/DiscountCodeEntityController!ejb.stateless.DiscountCodeEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
