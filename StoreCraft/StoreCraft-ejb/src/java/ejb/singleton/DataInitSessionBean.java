/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.singleton;

import ejb.stateless.CategoryEntityControllerLocal;
import ejb.stateless.ProductEntityControllerLocal;
import ejb.stateless.StaffEntityControllerLocal;
import ejb.stateless.TagEntityControllerLocal;
import entity.CategoryEntity;
import entity.ProductEntity;
import entity.StaffEntity;
import entity.TagEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.exception.InputDataValidationException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author shawn
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    @EJB
    private CategoryEntityControllerLocal hpCategoryontrollerLocal;
    @EJB
    private TagEntityControllerLocal tagEntityControllerLocal;

    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            staffEntityControllerLocal.retrieveStaffByUsername("manager");
        } catch (StaffNotFoundException ex) {
            initializeData();
        }
    }

    private void initializeData() {

        try {
            staffEntityControllerLocal.createNewStaff(new StaffEntity("John", "Doe", "manager", "password"));

            CategoryEntity categoryEntityElectronics = hpCategoryontrollerLocal.createNewCategoryEntity(new CategoryEntity("Electronics", "Electronics"), null);
            CategoryEntity categoryEntityFashions = hpCategoryontrollerLocal.createNewCategoryEntity(new CategoryEntity("Fashion", "Fashion"), null);
            CategoryEntity asusCategory = hpCategoryontrollerLocal.createNewCategoryEntity(new CategoryEntity("Asus", "Asus Laptop"), categoryEntityElectronics.getCategoryId());
            CategoryEntity dellCategory = hpCategoryontrollerLocal.createNewCategoryEntity(new CategoryEntity("Dell", "Dell Laptop"), categoryEntityElectronics.getCategoryId());
            CategoryEntity hpCategory = hpCategoryontrollerLocal.createNewCategoryEntity(new CategoryEntity("HP", "HP Laptop"), categoryEntityElectronics.getCategoryId());
            CategoryEntity zaraCategory = hpCategoryontrollerLocal.createNewCategoryEntity(new CategoryEntity("ZARA", "Zara Clothing"), categoryEntityFashions.getCategoryId());
            CategoryEntity topmanCategory = hpCategoryontrollerLocal.createNewCategoryEntity(new CategoryEntity("Topman", "Topman Clothing"), categoryEntityFashions.getCategoryId());
            CategoryEntity supremeCategory = hpCategoryontrollerLocal.createNewCategoryEntity(new CategoryEntity("SUPREME", "Supreme Clothing"), categoryEntityFashions.getCategoryId());

            TagEntity tagEntityPopular = tagEntityControllerLocal.createNewTagEntity(new TagEntity("Popular"));
            TagEntity tagEntityDiscount = tagEntityControllerLocal.createNewTagEntity(new TagEntity("Discount"));
            TagEntity tagEntityNew = tagEntityControllerLocal.createNewTagEntity(new TagEntity("New"));

            List<Long> tagIdsPopular = new ArrayList<>();
            tagIdsPopular.add(tagEntityPopular.getTagId());

            List<Long> tagIdsDiscount = new ArrayList<>();
            tagIdsDiscount.add(tagEntityDiscount.getTagId());

            List<Long> tagIdsPopularDiscount = new ArrayList<>();
            tagIdsPopularDiscount.add(tagEntityPopular.getTagId());
            tagIdsPopularDiscount.add(tagEntityDiscount.getTagId());

            List<Long> tagIdsPopularNew = new ArrayList<>();
            tagIdsPopularNew.add(tagEntityPopular.getTagId());
            tagIdsPopularNew.add(tagEntityNew.getTagId());

            List<Long> tagIdsPopularDiscountNew = new ArrayList<>();
            tagIdsPopularDiscountNew.add(tagEntityPopular.getTagId());
            tagIdsPopularDiscountNew.add(tagEntityDiscount.getTagId());
            tagIdsPopularDiscountNew.add(tagEntityNew.getTagId());

            List<Long> tagIdsEmpty = new ArrayList<>();

            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD001", "Asus 1", "Asus 1", 100, 10, new BigDecimal("10.00"), "https://sg-test-11.slatic.net/p/943418d992f48b20018d2555419cef50.jpg"), asusCategory.getCategoryId(), tagIdsPopular);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD002", "Asus 2", "Asus 2", 100, 10, new BigDecimal("25.50"), "https://sg-test-11.slatic.net/p/943418d992f48b20018d2555419cef50.jpg"), asusCategory.getCategoryId(), tagIdsDiscount);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD003", "Asus 3", "Asus 3", 100, 10, new BigDecimal("15.00"), "https://sg-test-11.slatic.net/p/943418d992f48b20018d2555419cef50.jpg"), asusCategory.getCategoryId(), tagIdsPopularDiscount);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD004", "Dell 1", "Dell 1", 100, 10, new BigDecimal("20.00"), "https://smartsystems.jo/image/cache/catalog/products/computer-systems/laptops/3567-1200x1200.jpg"), dellCategory.getCategoryId(), tagIdsPopularNew);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD005", "Dell 2", "Dell 2", 100, 10, new BigDecimal("10.00"), "https://smartsystems.jo/image/cache/catalog/products/computer-systems/laptops/3567-1200x1200.jpg"), dellCategory.getCategoryId(), tagIdsPopularDiscountNew);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD006", "Dell 3", "Dell 3", 100, 10, new BigDecimal("100.00"), "https://smartsystems.jo/image/cache/catalog/products/computer-systems/laptops/3567-1200x1200.jpg"), dellCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD007", "HP 1", "HP 1", 100, 10, new BigDecimal("35.00"), "https://ryanscomputers.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/3/g/3gg73pa.jpg"), hpCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD008", "HP 2", "HP 2", 100, 10, new BigDecimal("20.05"), "https://ryanscomputers.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/3/g/3gg73pa.jpg"), hpCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD009", "HP 3", "HP 3", 100, 10, new BigDecimal("5.50"), "https://ryanscomputers.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/3/g/3gg73pa.jpg"), hpCategory.getCategoryId(), tagIdsEmpty);

            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD010", "Zara 1", "Zara 1", 100, 10, new BigDecimal("20.00"), "https://shop.r10s.jp/jam-ing/cabinet/uploadbox2/01480/wew8413-1.jpg"), zaraCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD011", "Zara 2", "Zara 2", 100, 10, new BigDecimal("30.50"), "https://shop.r10s.jp/jam-ing/cabinet/uploadbox2/01480/wew8413-1.jpg"), zaraCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD012", "Zara 3", "Zara 3", 100, 10, new BigDecimal("18.50"), "https://shop.r10s.jp/jam-ing/cabinet/uploadbox2/01480/wew8413-1.jpg"), zaraCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD013", "Topman 1", "Topman 1", 100, 10, new BigDecimal("50.00"), "https://static-01.daraz.com.bd/original/bd2e1b0011a683bdb8b0028079efc816.jpg"), topmanCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD014", "Topman 2", "Topman 2", 100, 10, new BigDecimal("100.00"), "https://static-01.daraz.com.bd/original/bd2e1b0011a683bdb8b0028079efc816.jpg"), topmanCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD015", "Topman 3", "Topman 3", 100, 10, new BigDecimal("200.00"), "https://static-01.daraz.com.bd/original/bd2e1b0011a683bdb8b0028079efc816.jpg"), topmanCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD016", "Supreme 1", "Supreme 1", 100, 10, new BigDecimal("95.00"), "https://images-na.ssl-images-amazon.com/images/I/61muQxTZRBL._SL1200_.jpg"), supremeCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD017", "Supreme 2", "Supreme 2", 100, 10, new BigDecimal("19.05"), "https://images-na.ssl-images-amazon.com/images/I/61muQxTZRBL._SL1200_.jpg"), supremeCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD018", "Supreme 3", "Supreme 3", 100, 10, new BigDecimal("10.50"), "https://images-na.ssl-images-amazon.com/images/I/61muQxTZRBL._SL1200_.jpg"), supremeCategory.getCategoryId(), tagIdsEmpty);
        } catch (InputDataValidationException ex) {
            System.err.println("********** DataInitializationSessionBean.initializeData(): " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
        }
    }
}

