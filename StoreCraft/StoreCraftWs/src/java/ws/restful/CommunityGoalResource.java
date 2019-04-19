/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.CommunityGoalRsp;
import datamodel.ws.rest.ErrorRsp;
import ejb.stateless.CommunityGoalEntityControllerLocal;
import entity.CommunityGoalEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.CommunityGoalNotFoundException;

/**
 * REST Web Service
 *
 * @author Win Phong
 */
@Path("CommunityGoal")
public class CommunityGoalResource {

    CommunityGoalEntityControllerLocal communityGoalEntityController = lookupCommunityGoalEntityControllerLocal();

    @Context
    private UriInfo context;
    
    

    /**
     * Creates a new instance of CommunityGoalResource
     */
    public CommunityGoalResource() {
    }
    
    @Path("retrieveAllCommunityGoals")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCommuntiyGoals() 
    {
        try {
            List<CommunityGoalEntity> communityGoalEntities = communityGoalEntityController.retrieveAllCommunityGoals();
            List<CommunityGoalEntity> copy = new ArrayList();
            copy.addAll(communityGoalEntities);
            
            for(CommunityGoalEntity communityGoalEntity : copy) 
            {
                if (communityGoalEntity.getEndDate().before(new Date()) ) {
                    communityGoalEntities.remove(communityGoalEntity);
                } else {
                    communityGoalEntity.getStaffEntity().getCommunityGoalEntities().clear();
                }
            }

            CommunityGoalRsp communityGoalRsp = new CommunityGoalRsp(communityGoalEntities);
            
            return Response.status(Response.Status.OK).entity(communityGoalRsp).build();
        }
        catch (Exception ex) {
            
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    
    
    @Path("retrieveCommunityGoalById")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCommunityGoalById(@QueryParam("communityGoalId") Long communityGoalId) 
    {
        try {
            
            CommunityGoalEntity communityGoalEntity = communityGoalEntityController.retrieveCommunityGoalByCommunityGoalId(communityGoalId);
        
            communityGoalEntity.getStaffEntity().getCommunityGoalEntities().clear();

            CommunityGoalRsp communityGoalRsp = new CommunityGoalRsp(communityGoalEntity);
            
            return Response.status(Response.Status.OK).entity(communityGoalRsp).build();
        }
        catch (CommunityGoalNotFoundException ex) {
            
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        }
        catch (Exception ex) {
            
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("retrieveCurrentCommunityGoalsByCountry")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCurrentCommunityGoalsByCountry(@QueryParam("country") String country)
                                                           
    {
        try {
            List<CommunityGoalEntity> communityGoalEntitys = communityGoalEntityController.retrieveCurrentCommunityGoal(country);
            System.out.println("communityGoalEntitys.length" + communityGoalEntitys.size());
            for(CommunityGoalEntity cge: communityGoalEntitys) {
                cge.getStaffEntity().getCommunityGoalEntities().clear();
                cge.setStaffEntity(null);
            }
            

            CommunityGoalRsp communityGoalRsp = new CommunityGoalRsp(communityGoalEntitys);
            return Response.status(Response.Status.OK).entity(communityGoalRsp).build();
        }
        catch (Exception ex) {
            
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

//    @PUT
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void putJson(String content) {
//    }

    private CommunityGoalEntityControllerLocal lookupCommunityGoalEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CommunityGoalEntityControllerLocal) c.lookup("java:global/StoreCraft/StoreCraft-ejb/CommunityGoalEntityController!ejb.stateless.CommunityGoalEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
