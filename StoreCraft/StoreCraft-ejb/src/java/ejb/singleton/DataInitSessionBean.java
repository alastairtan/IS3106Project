/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.singleton;

import ejb.stateless.CategoryEntityControllerLocal;
import ejb.stateless.CommunityGoalEntityControllerLocal;
import ejb.stateless.CustomerEntityControllerLocal;
import ejb.stateless.DiscountCodeEntityControllerLocal;
import ejb.stateless.ProductEntityControllerLocal;
import ejb.stateless.ReviewEntityControllerLocal;
import ejb.stateless.SaleTransactionEntityControllerLocal;
import ejb.stateless.ScavengerHuntEntityControllerLocal;
import ejb.stateless.StaffEntityControllerLocal;
import ejb.stateless.TagEntityControllerLocal;
import entity.CategoryEntity;
import entity.CommunityGoalEntity;
import entity.CustomerEntity;
import entity.DiscountCodeEntity;
import entity.ProductEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import entity.StaffEntity;
import entity.TagEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.DiscountCodeTypeEnum;
import util.enumeration.StaffTypeEnum;
import util.exception.CustomerNotFoundException;
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

    @EJB(name = "CommunityGoalEntityControllerLocal")
    private CommunityGoalEntityControllerLocal communityGoalEntityControllerLocal;

    @EJB
    private ScavengerHuntEntityControllerLocal scavengerHuntEntityControllerLocal;

    @EJB
    private SaleTransactionEntityControllerLocal saleTransactionEntityControllerLocal;

    @EJB
    private CustomerEntityControllerLocal customerEntityControllerLocal;

    @EJB
    private StaffEntityControllerLocal staffEntityControllerLocal;
    @EJB
    private ProductEntityControllerLocal productEntityControllerLocal;
    @EJB
    private CategoryEntityControllerLocal categoryEntityControllerLocal;
    @EJB
    private TagEntityControllerLocal tagEntityControllerLocal;
    @EJB
    private DiscountCodeEntityControllerLocal discountCodeEntityControllerLocal;
    @EJB
    private ReviewEntityControllerLocal reviewEntityControllerLocal;
    
    

    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            staffEntityControllerLocal.retrieveStaffByUsername("manager");
            customerEntityControllerLocal.retrieveCustomerByEmail("steve@gmail.com");
        } catch (StaffNotFoundException | CustomerNotFoundException ex) {
            initializeData();
        }
    }

    private void initializeData() {

        try {
            staffEntityControllerLocal.createNewStaff(new StaffEntity("John", "Doe", "manager", "password", StaffTypeEnum.MANAGER, "http://www.gstatic.com/tv/thumb/persons/1805/1805_v9_bb.jpg"));

            CustomerEntity c = customerEntityControllerLocal.createNewCustomer(new CustomerEntity("Steve", "Rogers", "Steve@gmail.com", "password", "America", "https://avatarfiles.alphacoders.com/130/130595.jpg"));
            CustomerEntity c1 = customerEntityControllerLocal.createNewCustomer(new CustomerEntity("Peter", "Parker", "peter@gmail.com", "password", "America", "https://www.gannett-cdn.com/-mm-/51e30e00349d6f72262284dc0b87892012a4e819/c=1343-90-2398-883/local/-/media/2017/06/26/USATODAY/USATODAY/636340759929048028-XXX-SPIDER-MAN-HOMECOMING-87249008.JPG?width=534&height=401&fit=crop"));
            CustomerEntity c2 = customerEntityControllerLocal.createNewCustomer(new CustomerEntity("Bruce", "Banner", "bruce@gmail.com", "password", "America", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTtRb2rkBsxg1q60ki00KULSIqAS2SXmKTnObZO7qWQA7AV5pSbsg"));
            CustomerEntity c3 = customerEntityControllerLocal.createNewCustomer(new CustomerEntity("Ant", "Man", "ant@gmail.com", "password", "Singapore", "https://images-na.ssl-images-amazon.com/images/I/61bJf%2B2Z%2BXL._SY741_.jpg"));

            List<Long> customerEntityIds = new ArrayList<>();
            customerEntityIds.add(c.getCustomerId());
            List<Long> productEntityIds = new ArrayList<>();
            Date startDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.add(Calendar.DAY_OF_YEAR, 7);
            Date endDate = cal.getTime();
            DiscountCodeEntity newDiscountCodeEntity = new DiscountCodeEntity(startDate, endDate, 10, "ABCDEF", DiscountCodeTypeEnum.PERCENTAGE, BigDecimal.valueOf(5.00));
            DiscountCodeEntity dce = discountCodeEntityControllerLocal.createNewDiscountCode(newDiscountCodeEntity, customerEntityIds, productEntityIds);

            CategoryEntity categoryEntityElectronics = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Electronics", "Electronics"), null);
            CategoryEntity categoryEntityFashions = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Fashion", "Fashion"), null);
            CategoryEntity laptopsCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Laptops", "Laptops"), categoryEntityElectronics.getCategoryId());
            CategoryEntity asusCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Asus", "Asus Laptop"), laptopsCategory.getCategoryId());
            CategoryEntity dellCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Dell", "Dell Laptop"), laptopsCategory.getCategoryId());
            CategoryEntity hpCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("HP", "HP Laptop"), laptopsCategory.getCategoryId());
            CategoryEntity phonesCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Phones", "Mobile Phones"), categoryEntityElectronics.getCategoryId());
            CategoryEntity samsungCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Samsung", "Samsung Phones"), phonesCategory.getCategoryId());
            CategoryEntity huaweiCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Huawei", "Huawei Phones"), phonesCategory.getCategoryId());
            CategoryEntity xiaomiCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Xiaomi", "Xiaomi Phones"), phonesCategory.getCategoryId());
            CategoryEntity zaraCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("ZARA", "Zara Clothing"), categoryEntityFashions.getCategoryId());
            CategoryEntity topmanCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Topman", "Topman Clothing"), categoryEntityFashions.getCategoryId());
            CategoryEntity supremeCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("SUPREME", "Supreme Clothing"), categoryEntityFashions.getCategoryId());

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

            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD001", "Asus 1", "This is Asus 2", 100, 10, new BigDecimal("10.00"), "https://sg-test-11.slatic.net/p/943418d992f48b20018d2555419cef50.jpg"), asusCategory.getCategoryId(), tagIdsPopular);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD002", "Asus 2", "This is Asus 2", 100, 10, new BigDecimal("25.50"), "https://sg-test-11.slatic.net/p/943418d992f48b20018d2555419cef50.jpg"), asusCategory.getCategoryId(), tagIdsDiscount);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD003", "ASUS VivoBook 14 X405", "Everyday: All-rounder for work and play. Windows 10. Full HD display", 100, 10, new BigDecimal("120.00"), "https://brain-images-ssl.cdn.dixons.com/0/4/10157740/u_10157740.jpg"), asusCategory.getCategoryId(), tagIdsPopularDiscount);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD004", "Dell Inspiron 3567", "Intel® Core™ i3-6006U, 6th Generation", 100, 10, new BigDecimal("20.00"), "https://smartsystems.jo/image/cache/catalog/products/computer-systems/laptops/3567-1200x1200.jpg"), dellCategory.getCategoryId(), tagIdsPopularNew);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD005", "Dell Inspiron 7370", "Dell Inspiron laptop features Intel Core i5-8250U offers seamless multitasking", 100, 10, new BigDecimal("120.00"), "https://smartsystems.jo/image/cache/catalog/products/computer-systems/laptops/3567-1200x1200.jpg"), dellCategory.getCategoryId(), tagIdsPopularDiscountNew);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD006", "Dell XPS 13", "Our smallest 13.3\" (33.8cm) laptop has a virtually borderless InfinityEdge display — available with touch.", 100, 10, new BigDecimal("145.00"), "https://cf2.s3.souqcdn.com/item/2017/10/18/26/03/11/71/item_XL_26031171_47987159.jpg"), dellCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD007", "HP 1", "This is HP 1", 100, 10, new BigDecimal("35.00"), "https://ryanscomputers.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/3/g/3gg73pa.jpg"), hpCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD008", "HP 2", "This is HP 2", 100, 10, new BigDecimal("20.05"), "https://ryanscomputers.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/3/g/3gg73pa.jpg"), hpCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD009", "HP 3", "This is HP 3", 100, 10, new BigDecimal("5.50"), "https://ryanscomputers.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/3/g/3gg73pa.jpg"), hpCategory.getCategoryId(), tagIdsEmpty);

            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD010", "Zara 1", "This is Zara 1", 100, 10, new BigDecimal("20.00"), "https://shop.r10s.jp/jam-ing/cabinet/uploadbox2/01480/wew8413-1.jpg"), zaraCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD011", "Zara 2", "This is Zara 2", 100, 10, new BigDecimal("30.50"), "https://shop.r10s.jp/jam-ing/cabinet/uploadbox2/01480/wew8413-1.jpg"), zaraCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD012", "Zara 3", "This is Zara 3", 100, 10, new BigDecimal("18.50"), "https://shop.r10s.jp/jam-ing/cabinet/uploadbox2/01480/wew8413-1.jpg"), zaraCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD013", "Topman 1", "This is Topman 1", 100, 10, new BigDecimal("50.00"), "https://static-01.daraz.com.bd/original/bd2e1b0011a683bdb8b0028079efc816.jpg"), topmanCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD014", "Topman 2", "This is Topman 2", 100, 10, new BigDecimal("100.00"), "https://static-01.daraz.com.bd/original/bd2e1b0011a683bdb8b0028079efc816.jpg"), topmanCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD015", "Topman 3", "This is Topman 3", 100, 10, new BigDecimal("200.00"), "https://static-01.daraz.com.bd/original/bd2e1b0011a683bdb8b0028079efc816.jpg"), topmanCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD016", "Supreme 1", "This is Supreme 1", 100, 10, new BigDecimal("95.00"), "https://images-na.ssl-images-amazon.com/images/I/61muQxTZRBL._SL1200_.jpg"), supremeCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD017", "Supreme 2", "This is Supreme 2", 100, 10, new BigDecimal("19.05"), "https://images-na.ssl-images-amazon.com/images/I/61muQxTZRBL._SL1200_.jpg"), supremeCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD018", "Supreme 3", "This is Supreme 3", 100, 10, new BigDecimal("10.50"), "https://images-na.ssl-images-amazon.com/images/I/61muQxTZRBL._SL1200_.jpg"), supremeCategory.getCategoryId(), tagIdsEmpty);
            
            
            
            // Load transactions
            List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities = new ArrayList<>();
            saleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(001, productEntityControllerLocal.retrieveProductByProductSkuCode("PROD001"), 5, new BigDecimal(10), new BigDecimal(50)));
            saleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(002, productEntityControllerLocal.retrieveProductByProductSkuCode("PROD002"), 2, new BigDecimal(25.50), new BigDecimal(51)));
            saleTransactionEntityControllerLocal.createNewSaleTransaction(new Long(1), new SaleTransactionEntity(2, 7, new BigDecimal(101), new Date(), Boolean.FALSE, customerEntityControllerLocal.retrieveCustomerByEmail("Steve@gmail.com"), saleTransactionLineItemEntities, null, null));
            
            
            CommunityGoalEntity cge = new CommunityGoalEntity(startDate,endDate,new BigDecimal(1000),"America", "This is goal 1", "Goalllllllll", new BigDecimal(4), false);
//            cge.setDescription("fsdfsd");
//            cge.setGoalTitle("har");
//            cge.setRewardPercentage(new BigDecimal(12));
            communityGoalEntityControllerLocal.createNewCommunityGoal(cge, staffEntityControllerLocal.retrieveStaffByUsername("manager").getStaffId());
            
            
            reviewEntityControllerLocal.createNewReview(c.getCustomerId(), "Not worth the price", 4, new Long(1) );
            reviewEntityControllerLocal.createNewReview(c1.getCustomerId(), "Asus laptop is very good", 4, new Long(1) );
            reviewEntityControllerLocal.createNewReview(c1.getCustomerId(), "What a scam", 5, new Long(1) );
            reviewEntityControllerLocal.createNewReview(c2.getCustomerId(), "Is this good?", 4, new Long(1) );
            

        } catch (InputDataValidationException ex) {
            System.err.println("********** DataInitializationSessionBean.initializeData(): " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
        }
    }
}

