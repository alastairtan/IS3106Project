package ws.restful;

import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.RetrieveAllCategoriesRsp;
import ejb.stateless.CategoryEntityControllerLocal;
import ejb.stateless.StaffEntityControllerLocal;
import entity.CategoryEntity;
import entity.StaffEntity;
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
import util.exception.InvalidLoginCredentialException;



@Path("Category")
public class CategoryResource
{    


    @Context
    private UriInfo context;
    
    private CategoryEntityControllerLocal categoryEntityControllerLocal;

    
    public CategoryResource() 
    {
        categoryEntityControllerLocal = lookupCategoryEntityControllerLocal();
    }
    
    
    
    @Path("retrieveAllCategories")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCategories()
    {
        try{
        
            List<CategoryEntity> categoryEntities = categoryEntityControllerLocal.retrieveAllCategories();
            
            for(CategoryEntity categoryEntity:categoryEntities)
            {
                if(categoryEntity.getParentCategoryEntity() != null)
                {
                    categoryEntity.getParentCategoryEntity().getSubCategoryEntities().clear();
                }
                
                categoryEntity.getSubCategoryEntities().clear();
                categoryEntity.getProductEntities().clear();
            }
            
            return Response.status(Status.OK).entity(new RetrieveAllCategoriesRsp(categoryEntities)).build();
        
            
        }catch(Exception ex){
            
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    private CategoryEntityControllerLocal lookupCategoryEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CategoryEntityControllerLocal) c.lookup("java:global/StoreCraft/StoreCraft-ejb/CategoryEntityController!ejb.stateless.CategoryEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    
    

}
