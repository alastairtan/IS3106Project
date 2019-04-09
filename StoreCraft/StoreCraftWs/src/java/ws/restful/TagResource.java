/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.RetrieveAllTagsRsp;
import ejb.stateless.TagEntityControllerLocal;
import entity.TagEntity;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * REST Web Service
 *
 * @author shawn
 */
@Path("Tag")
public class TagResource {

    TagEntityControllerLocal tagEntityControllerLocal = lookupTagEntityControllerLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TagResource
     */
    public TagResource() {
    }

    @Path("retrieveAllTags")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllTags() {
         try {

            List<TagEntity> tagEntities = tagEntityControllerLocal.retrieveAllTags();

            for (TagEntity te : tagEntities) {

                   te.getProductEntities().clear();
                
            }

            return Response.status(Status.OK).entity(new RetrieveAllTagsRsp(tagEntities)).build();

        } catch (Exception ex) {

            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());

            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }


    private TagEntityControllerLocal lookupTagEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (TagEntityControllerLocal) c.lookup("java:global/StoreCraft/StoreCraft-ejb/TagEntityController!ejb.stateless.TagEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
