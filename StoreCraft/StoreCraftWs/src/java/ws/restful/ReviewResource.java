package ws.restful;

import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.ReviewChainRsp;
import ejb.stateless.ReviewEntityControllerLocal;
import entity.ReviewEntity;
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
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("Review")
public class ReviewResource { 

    @Context
    private UriInfo context;

    ReviewEntityControllerLocal reviewEntityControllerLocal; 
    
    public ReviewResource() {
        reviewEntityControllerLocal = lookupReviewEntityControllerLocal();
    }

  
    
    @Path("retrieveReviewChain")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCategoryByCategoryId(@QueryParam("reviewId") Long reviewId){
        try{
            
            System.out.println("hello");
            
            List<ReviewEntity> reviewChain = reviewEntityControllerLocal.getReviewChain(reviewId);
            System.out.println("hello2");
            for (ReviewEntity re : reviewChain){
                if (re.getCustomerEntity() != null) {
                    re.getCustomerEntity().getReviewEntities().clear();
                    re.getCustomerEntity().setDiscountCodeEntities(null);
                    re.getCustomerEntity().setSaleTransactionEntities(null);
                }
                re.setParentReviewEntity(null);
                re.setProductEntity(null);
                re.setReplyReviewEntity(null);
                if (re.getStaffEntity() != null) re.getStaffEntity().getReviewEntities().clear();
            }
            reviewChain.remove(0);
            System.out.println("hello3");
            
            return Response.status(Status.OK).entity(new ReviewChainRsp(reviewChain)).build();
                 
        } catch (Exception ex){
            
            System.out.println("hello4");
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    private ReviewEntityControllerLocal lookupReviewEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ReviewEntityControllerLocal) c.lookup("java:global/StoreCraft/StoreCraft-ejb/ReviewEntityController!ejb.stateless.ReviewEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }



}
