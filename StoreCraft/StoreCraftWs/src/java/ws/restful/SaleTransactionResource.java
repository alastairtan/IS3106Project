/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.SaleTransactionReq;
import datamodel.ws.rest.SaleTransactionRsp;
import ejb.stateless.SaleTransactionEntityControllerLocal;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.CreateNewSaleTransactionException;
import util.exception.CustomerNotFoundException;
import util.exception.SaleTransactionNotFoundException;

/**
 * REST Web Service
 *
 * @author Win Phong
 */
@Path("SaleTransaction")
public class SaleTransactionResource {

    @Context
    private UriInfo context;
    
    SaleTransactionEntityControllerLocal saleTransactionEntityControllerLocal = lookupSaleTransactionEntityControllerLocal();
    /**
     * Creates a new instance of SaleTransactionResource
     */
    public SaleTransactionResource() {
    }

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response retrieveAllSaleTransaction() 
//    {
//        try {
//            
//            List<SaleTransactionEntity> saleTransactionEntities = saleTransactionEntityController.retrieveAllSaleTransactions();
//
//            for(SaleTransactionEntity saleTransactionEntity : saleTransactionEntities)
//            {
//                saleTransactionEntity.getCustomerEntity().getSaleTransactionEntities().clear();
//                saleTransactionEntity.getCustomerEntity().getDiscountCodeEntities().clear();
//                saleTransactionEntity.getCustomerEntity().getReviewEntities().clear();
//                for(SaleTransactionLineItemEntity saleTransactionLineItemEntity : saleTransactionEntity.getSaleTransactionLineItemEntities()) 
//                {
//                    saleTransactionLineItemEntity.setProductEntity(null);
//                }
//            }
//
//            SaleTransactionRsp saleTransactionRsp = new SaleTransactionRsp(saleTransactionEntities);
//
//            return Response.status(Response.Status.OK).entity(saleTransactionRsp).build();
//        }
//        catch(Exception ex)
//        {
//            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
//            
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
//        }
//    }
//    
//    @Path("retrieveSaleTransactionById")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response retrieveSaleTransactionById(@QueryParam("saleTransactionId") Long saleTransactionId) 
//    {
//        try {
//            
//            SaleTransactionEntity saleTransactionEntity = saleTransactionEntityController.retrieveSaleTransactionBySaleTransactionId(saleTransactionId);
//            
//            saleTransactionEntity.getCustomerEntity().getSaleTransactionEntities().clear();
//            saleTransactionEntity.getCustomerEntity().getDiscountCodeEntities().clear();
//            saleTransactionEntity.getCustomerEntity().getReviewEntities().clear();
//            
//            for(SaleTransactionLineItemEntity saleTransactionLineItemEntity : saleTransactionEntity.getSaleTransactionLineItemEntities()) 
//            {
//                saleTransactionLineItemEntity.setProductEntity(null);
//            }
//            
//            SaleTransactionRsp saleTransactionRsp = new SaleTransactionRsp(saleTransactionEntity);
//
//            return Response.status(Response.Status.OK).entity(saleTransactionRsp).build();
//        }
//        catch(SaleTransactionNotFoundException ex)
//        {
//            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
//            
//            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
//        }
//        catch(Exception ex)
//        {
//            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
//            
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
//        }
//    }


    @Path("createSaleTransaction")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSaleTransaction(SaleTransactionReq saleTransactionReq) {
        
        
        if (saleTransactionReq != null) 
        {
            try
            {   
                Long customerId = saleTransactionReq.getSaleTransactionEntity().getCustomerEntity().getCustomerId();
                System.out.println("Customer ID" + customerId);
                
                SaleTransactionEntity saleTransactionEntity = saleTransactionEntityControllerLocal.createNewSaleTransaction(
                        customerId, saleTransactionReq.getSaleTransactionEntity());
                
                saleTransactionEntity.getCustomerEntity().getDiscountCodeEntities().clear();
                saleTransactionEntity.getCustomerEntity().getReviewEntities().clear();
                saleTransactionEntity.getCustomerEntity().getSaleTransactionEntities().clear();
                saleTransactionEntity.getCustomerEntity().setSalt(null);
                saleTransactionEntity.getCustomerEntity().setPassword(null);

                SaleTransactionRsp saleTransactionRsp = new SaleTransactionRsp(saleTransactionEntity);
                
                System.out.println(saleTransactionRsp.getSaleTransactionEntity().getSaleTransactionId());
                System.out.println(saleTransactionRsp.getSaleTransactionEntity().getCustomerEntity().getTotalPoints());
                
                return Response.status(Response.Status.OK).entity(saleTransactionRsp).build();
            }
            catch (CreateNewSaleTransactionException ex) 
            {
                ErrorRsp errorRsp = new ErrorRsp("An error occured when creating the sale transaction!");

                return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
            }
            catch (CustomerNotFoundException ex)
            {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

                return Response.status(Response.Status.UNAUTHORIZED).entity(errorRsp).build();
            }
            catch (Exception ex)
            {
                ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
            }
        }
        else
        {
            ErrorRsp errorRsp = new ErrorRsp("Invalid create sale transaction request");
                            
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        }
    }

    private SaleTransactionEntityControllerLocal lookupSaleTransactionEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (SaleTransactionEntityControllerLocal) c.lookup("java:global/StoreCraft/StoreCraft-ejb/SaleTransactionEntityController!ejb.stateless.SaleTransactionEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
