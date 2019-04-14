/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful;

import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.ProductRsp;
import datamodel.ws.rest.RetrieveProdByCategoryRsp;
import ejb.stateless.ProductEntityControllerLocal;
import entity.CategoryEntity;
import entity.DiscountCodeEntity;
import entity.ProductEntity;
import entity.ReviewEntity;
import entity.TagEntity;
import java.util.ArrayList;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.CategoryNotFoundException;
import util.exception.ProductNotFoundException;

/**
 * REST Web Service
 *
 * @author shawn
 */
@Path("Product")
public class ProductResource {

    ProductEntityControllerLocal productEntityControllerLocal;

    @Context
    private UriInfo context;

    public ProductResource() {
        this.productEntityControllerLocal = lookupProductEntityControllerLocal();
    }

    @Path("getProductsByCategory")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsByCategory(@QueryParam("categoryId") String categoryId) {
        try {
            List<ProductEntity> productEntities = productEntityControllerLocal.filterProductsByCategory((Long.parseLong(categoryId)));
            for (ProductEntity productEntity : productEntities) {
                
                clearParentToChildrenRelationship(productEntity.getCategoryEntity());
   
                for (ReviewEntity reviewEntity : productEntity.getReviewEntities()) {
                    reviewEntity.setProductEntity(null); //unidirectional between product and review
                    reviewEntity.setCustomerEntity(null); //unidirectional between product's review and customer
                    reviewEntity.setReplyReviewEntity(null);
                    reviewEntity.setStaffEntity(null);
                    reviewEntity.setParentReviewEntity(null);
                }
                
                List<TagEntity> tagEntities = productEntity.getTagEntities();
                for (TagEntity tagEntity : tagEntities) {
                    tagEntity.getProductEntities().clear(); //unidirectional between product and tags
                }

                for (DiscountCodeEntity dce : productEntity.getDiscountCodeEntities()) {
                    dce.getProductEntities().clear(); //undirectional between product and discount codes
                    dce.getCustomerEntities().clear(); //unidirectional between product's discount codes and customer
                }
                
            }
        
            RetrieveProdByCategoryRsp response = new RetrieveProdByCategoryRsp(productEntities);
           
            return Response.status(Response.Status.OK).entity(response).build();

        } catch (CategoryNotFoundException ex) {

            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorRsp).build();

        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    

    @Path("index")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRandomProductsForIndexPage() {
        try {
            List<ProductEntity> productEntities = productEntityControllerLocal.retrieveRandomProducts();
            for (ProductEntity productEntity : productEntities) {
                
                clearParentToChildrenRelationship(productEntity.getCategoryEntity());

                List<TagEntity> tagEntities = productEntity.getTagEntities();
                for (TagEntity tagEntity : tagEntities) {
                    tagEntity.getProductEntities().clear(); //unidirectional between product and tags
                }
                               
                for (ReviewEntity reviewEntity : productEntity.getReviewEntities()) {
                    reviewEntity.setProductEntity(null); //unidirectional between product and review
                    reviewEntity.getCustomerEntity().getReviewEntities().clear(); //unidirectional between product's review and customer
                }

                for (DiscountCodeEntity dce : productEntity.getDiscountCodeEntities()) {
                    dce.getProductEntities().clear(); //undirectional between product and discount codes
                    dce.getCustomerEntities().clear(); //unidirectional between product's discount codes and customer
                }
                
            }
        
            RetrieveProdByCategoryRsp response = new RetrieveProdByCategoryRsp(productEntities);
           
            return Response.status(Response.Status.OK).entity(response).build();

        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        }
        
    }

    @Path("getProductById")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductById(@QueryParam("productId") String productId) {
        
        try {
            
            ProductEntity productEntity = productEntityControllerLocal.retrieveProductByProductId(new Long(productId));
            
            for(ReviewEntity review : productEntity.getReviewEntities()) {
                review.setProductEntity(null);
                review.setReplyReviewEntity(null);
                review.setStaffEntity(null);
                review.setParentReviewEntity(null);
                review.getCustomerEntity().getDiscountCodeEntities().clear();
                review.getCustomerEntity().getSaleTransactionEntities().clear();
                review.getCustomerEntity().getReviewEntities().clear();
            }
            
            for(TagEntity tag : productEntity.getTagEntities()) {
                tag.getProductEntities().clear();
            }
            
            productEntity.setCategoryEntity(null);
            
            for(DiscountCodeEntity discoutCode : productEntity.getDiscountCodeEntities())
            {
                discoutCode.getProductEntities().clear();
            }
        
            ProductRsp productRsp = new ProductRsp(productEntity);
           
            return Response.status(Response.Status.OK).entity(productRsp).build();

        } catch (ProductNotFoundException | NumberFormatException ex) {

            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();

        } catch (Exception ex) {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }


    private void clearParentToChildrenRelationship(CategoryEntity subCategory) { //so relationships are unidirectional
        if (subCategory.getParentCategoryEntity() != null) {
            
            System.out.println("STEP 1");
            subCategory.getParentCategoryEntity().getSubCategoryEntities().clear();
            subCategory.setProductEntities(new ArrayList<>());
            
            System.out.println(subCategory.getName() + " " +subCategory.getSubCategoryEntities().size());
            clearParentToChildrenRelationship(subCategory.getParentCategoryEntity());
        } else {
            subCategory.getSubCategoryEntities().clear();
            System.out.println(subCategory.getName() + " " + subCategory.getSubCategoryEntities().size());
        }
    }

    private ProductEntityControllerLocal lookupProductEntityControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ProductEntityControllerLocal) c.lookup("java:global/StoreCraft/StoreCraft-ejb/ProductEntityController!ejb.stateless.ProductEntityControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
