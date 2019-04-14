/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.ProductEntity;
import java.util.List;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewProductException;
import util.exception.DeleteProductException;
import util.exception.InputDataValidationException;
import util.exception.ProductInsufficientQuantityOnHandException;
import util.exception.ProductNotFoundException;
import util.exception.TagNotFoundException;
import util.exception.UpdateProductException;

/**
 *
 * @author shawn
 */
public interface ProductEntityControllerLocal {

    public ProductEntity createNewProduct(ProductEntity newProductEntity, Long categoryId, List<Long> tagIds) throws InputDataValidationException, CreateNewProductException;

    public List<ProductEntity> retrieveAllProducts();

    public List<ProductEntity> searchProductsByName(String searchString);

    public List<ProductEntity> filterProductsByCategory(Long categoryId) throws CategoryNotFoundException;

    public List<ProductEntity> filterProductsByTags(List<Long> tagIds, String condition);

    public ProductEntity retrieveProductByProductId(Long productId) throws ProductNotFoundException;

    public ProductEntity retrieveProductByProductSkuCode(String skuCode) throws ProductNotFoundException;

    public void updateProduct(ProductEntity productEntity, Long categoryId, List<Long> tagIds) throws InputDataValidationException, ProductNotFoundException, CategoryNotFoundException, TagNotFoundException, UpdateProductException;

    public void deleteProduct(Long productId) throws ProductNotFoundException, DeleteProductException;

    public void debitQuantityOnHand(Long productId, Integer quantityToDebit) throws ProductNotFoundException, ProductInsufficientQuantityOnHandException;

    public void creditQuantityOnHand(Long productId, Integer quantityToCredit) throws ProductNotFoundException;
    
    public boolean hasCustomerPurchasedProduct(Long productId, Long customerId);

    public List<ProductEntity> retrieveRandomProducts();

}
