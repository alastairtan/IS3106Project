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
import java.text.SimpleDateFormat;
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
            customerEntityControllerLocal.retrieveCustomerByEmail("Steve@gmail.com");
        } catch (StaffNotFoundException | CustomerNotFoundException ex) {
            initializeData();
        }
    }

    private void initializeData() {

        try {
            staffEntityControllerLocal.createNewStaff(new StaffEntity("John", "Doe", "manager", "password", StaffTypeEnum.MANAGER, "http://www.gstatic.com/tv/thumb/persons/1805/1805_v9_bb.jpg"));

            CustomerEntity c = customerEntityControllerLocal.createNewCustomer(new CustomerEntity("Steve", "Rogers", "Steve@gmail.com", "password", "United States", "https://avatarfiles.alphacoders.com/130/130595.jpg"));
            CustomerEntity c1 = customerEntityControllerLocal.createNewCustomer(new CustomerEntity("Peter", "Parker", "peter@gmail.com", "password", "United States", "https://www.gannett-cdn.com/-mm-/51e30e00349d6f72262284dc0b87892012a4e819/c=1343-90-2398-883/local/-/media/2017/06/26/USATODAY/USATODAY/636340759929048028-XXX-SPIDER-MAN-HOMECOMING-87249008.JPG?width=534&height=401&fit=crop"));
            CustomerEntity c2 = customerEntityControllerLocal.createNewCustomer(new CustomerEntity("Bruce", "Banner", "bruce@gmail.com", "password", "United States", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTtRb2rkBsxg1q60ki00KULSIqAS2SXmKTnObZO7qWQA7AV5pSbsg"));
            CustomerEntity c3 = customerEntityControllerLocal.createNewCustomer(new CustomerEntity("Ant", "Man", "ant@gmail.com", "password", "Singapore", "https://images-na.ssl-images-amazon.com/images/I/61bJf%2B2Z%2BXL._SY741_.jpg"));

            List<Long> customerEntityIds = new ArrayList<>();
            customerEntityIds.add(c.getCustomerId());
            List<Long> productEntityIds = new ArrayList<>();
            Date startDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.add(Calendar.DAY_OF_YEAR, 7);
            Date endDate = cal.getTime();
            DiscountCodeEntity newDiscountCodeEntity = new DiscountCodeEntity(startDate, endDate, 10, "AbCdEf", DiscountCodeTypeEnum.PERCENTAGE, BigDecimal.valueOf(5.00));
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

            CategoryEntity bagsCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Bags", "Bags"), categoryEntityFashions.getCategoryId());
            CategoryEntity superdryCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Superdry", "Superdry Bags"), bagsCategory.getCategoryId());
            CategoryEntity herschelCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Herschel", "Herschel Bags"), bagsCategory.getCategoryId());
            CategoryEntity coachCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Coach", "Coach"), bagsCategory.getCategoryId());

            CategoryEntity shoesCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Shoes", "Shoes"), categoryEntityFashions.getCategoryId());
            CategoryEntity sperryCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Sperry", "Sperry Shoes"), shoesCategory.getCategoryId());
            CategoryEntity adidasCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Adidas", "Adidas Shoes"), shoesCategory.getCategoryId());
            CategoryEntity vansCategory = categoryEntityControllerLocal.createNewCategoryEntity(new CategoryEntity("Vans", "Vans Shoes"), shoesCategory.getCategoryId());

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

            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD001", "ASUS ZenBook 14 UX433FA", "Creativity. Style. Innovation. These are the qualities that define the elegant new ZenBook 14. Designed to give you the freedom to discover your creative power.", 100, 10, new BigDecimal("1598.00"), "https://laz-img-sg.alicdn.com/p/b02b734a0340c3e5debddc5c4481382f.jpg_720x720q80.jpg"), asusCategory.getCategoryId(), tagIdsPopular);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD002", "ASUS ROG Zephyrus S GX531", "The all new ROG Zephyrus S is redefining ultra-slim gaming laptops yet again with innovative engineering to cool its 8th Gen Intel Core i7 processor and up to GeForce GTX 1070 with Max-Q design, so you can immerse yourself in its no-compromise 144Hz/3ms display.", 100, 10, new BigDecimal("2999.00"), "https://laz-img-sg.alicdn.com/p/ee4537225869947eed33a94999064c4f.jpg_720x720q80.jpg"), asusCategory.getCategoryId(), tagIdsDiscount);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD003", "ASUS VivoBook 14 X405", "Everyday: All-rounder for work and play. Windows 10. Full HD display", 100, 10, new BigDecimal("1200.00"), "https://brain-images-ssl.cdn.dixons.com/0/4/10157740/u_10157740.jpg"), asusCategory.getCategoryId(), tagIdsPopularDiscount);

            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD004", "Dell Inspiron 3567", "Intel® Core™ i3-6006U, 6th Generation", 100, 10, new BigDecimal("1250.00"), "https://smartsystems.jo/image/cache/catalog/products/computer-systems/laptops/3567-1200x1200.jpg"), dellCategory.getCategoryId(), tagIdsPopularNew);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD005", "Dell Inspiron 7370", "Dell Inspiron laptop features Intel Core i5-8250U offers seamless multitasking", 100, 10, new BigDecimal("1170.00"), "https://smartsystems.jo/image/cache/catalog/products/computer-systems/laptops/3567-1200x1200.jpg"), dellCategory.getCategoryId(), tagIdsPopularDiscountNew);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD006", "Dell XPS 13", "Our smallest 13.3\" (33.8cm) laptop has a virtually borderless InfinityEdge display — available with touch.", 100, 10, new BigDecimal("1852.00"), "https://cf2.s3.souqcdn.com/item/2017/10/18/26/03/11/71/item_XL_26031171_47987159.jpg"), dellCategory.getCategoryId(), tagIdsEmpty);

            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD007", "HP Laptop 14s-df0004TU", "Intel® Core™ i3-8130U (2.2 GHz base frequency, up to 4 GHz with Intel® Turbo Boost Technology, 4 MB cache, 2 cores)", 100, 10, new BigDecimal("799.00"), "https://laz-img-sg.alicdn.com/p/17822154c1ec5b2a5b0f48b24767e673.jpg_720x720q80.jpg"), hpCategory.getCategoryId(), tagIdsDiscount);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD008", "HP Pavilion - 14-ce0090tx", "Intel® Core™ i5-8250U Processor (1.6 GHz base frequency, up to 3.4 GHz with Intel® Turbo Boost Technology, 6 MB cache, 4 cores)", 100, 10, new BigDecimal("1349.00"), "https://laz-img-sg.alicdn.com/original/42f121cd9468e029926f76f4306ae0ad.jpg_720x720q80.jpg"), hpCategory.getCategoryId(), tagIdsPopular);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD009", "HP ChromeBook 11 G5", "Inspire learning and help elevate productivity to the next level with HP Chromebook 11. Affordable collaboration at school and work has never been so easy with Intel processors, and a long battery life.", 100, 10, new BigDecimal("248.50"), "https://laz-img-sg.alicdn.com/p/7e6c0340ea80265a2784052fb19ba7e6.jpg_720x720q80.jpg"), hpCategory.getCategoryId(), tagIdsEmpty);

            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD010", "Samsung Galaxy S10 128GB", "The next generation of Galaxy has arrived.", 100, 10, new BigDecimal("1298.00"), "https://laz-img-sg.alicdn.com/p/caa88493ac370e5ff7c7e1923ee0e7df.jpg_720x720q80.jpg"), samsungCategory.getCategoryId(), tagIdsPopular);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD011", "Samsung Galaxy J6 Plus 64/4GB", "Fingerprint (side-mounted), accelerometer, gyro, proximity, compass. Messaging SMS(threaded view), MMS, Email, Push Email, IM", 100, 10, new BigDecimal("234.00"), "https://laz-img-sg.alicdn.com/p/04818b0a1195e205b88fe73a68464185.jpg_720x720q80.jpg"), samsungCategory.getCategoryId(), tagIdsPopularDiscountNew);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD012", "Samsung Galaxy Note 9 (512GB) ", "Galaxy Note has always put powerful technology in the hands of those who demand more", 100, 10, new BigDecimal("1548.00"), "https://laz-img-sg.alicdn.com/original/466087ff32b3080fe54b9d9fe2064ffe.jpg_720x720q80.jpg"), samsungCategory.getCategoryId(), tagIdsPopularDiscount);

            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD013", "Huawei Y6 Pro 2019 3GB RAM+32GB ROM", "EMUI 9.0 (compatible with Android 9). Screen Size: 6.09inches. Battery capacity: 3020mAh", 100, 10, new BigDecimal("198"), "https://laz-img-sg.alicdn.com/p/e5abb002682cdf84d4bb4201cec5c1bf.jpg_720x720q80.jpg"), huaweiCategory.getCategoryId(), tagIdsPopularDiscountNew);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD014", "Huawei Mate 20X", "Huawei Mate20X, Blue, 7.2\", 6GB+128GB, Leica Cam, Supercharge, Fingerprint 51093BDKHuawei Mate20X, Silver, 7.2\", 6GB+128GB, Leica", 100, 10, new BigDecimal("1148"), "https://laz-img-sg.alicdn.com/p/07fad8987a3be9dae8d3dc42d882fa1b.jpg_720x720q80.jpg"), huaweiCategory.getCategoryId(), tagIdsPopularNew);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD015", "Huawei Nova 3i 4GB RAM", "A touch of magic", 100, 10, new BigDecimal("399.00"), "https://laz-img-sg.alicdn.com/p/mdc/43e47d4399af3a7aee6845316f7fe369.jpg_720x720q80.jpg"), huaweiCategory.getCategoryId(), tagIdsEmpty);

            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD016", "Xiaomi Redmi Note 6 Pro", "Redmi Note 6 Pro is Xiaomi’s first smartphone with AI-powered quad-camera", 100, 10, new BigDecimal("289.00"), "https://laz-img-sg.alicdn.com/original/bc85ac9401d71cab225c50dabfe83d24.jpg_720x720q80.jpg"), xiaomiCategory.getCategoryId(), tagIdsPopularDiscount);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD017", "Xiaomi Mi 8 Lite", "Gradient design (three colors). 24MP high-resolution front camera + 6.26’’ display", 100, 10, new BigDecimal("349"), "https://laz-img-sg.alicdn.com/p/7eaa950611841dbfbf90625f0a0cbab5.jpg_720x720q80.jpg"), xiaomiCategory.getCategoryId(), tagIdsPopularDiscount);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD018", "Xiaomi POCOPHONE F1", "Keep the core components of the POCOPHONE F1 nice and cool with the LiquidCool Technology designed exclusively for gaming phones.No matter how hard you're gaming, the cooled processor has no problem keeping up stability and high frequency output. Say goodbye to slow response time and frozen screens, this phone stays faster than fast.", 100, 10, new BigDecimal("367.99"), "https://my-test-11.slatic.net/original/d25a7b61c0a4e914f490254a750aabb8.jpg_720x720q80.jpg"), xiaomiCategory.getCategoryId(), tagIdsEmpty);

            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD019", "Superdry 17 Inch Laptop Backpack", "Most backpacks look pretty boring designed with lots of functionality but not much style. This one's a little different, if you had to carry a backpack around the city, this is the one you would want to be seen with", 100, 10, new BigDecimal("66.00"), "https://laz-img-sg.alicdn.com/p/f3b87cc4dd960dcf7aee1f67331a88fa.jpg_720x720q80.jpg"), superdryCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD020", "Original Superdry fully waterproof backpack", "Fully waterproof materials are used.", 100, 10, new BigDecimal("56.90"), "https://laz-img-sg.alicdn.com/p/42eed34d9b524f8e5fa3ba7b0cf763c1.jpg_720x720q80.jpg"), superdryCategory.getCategoryId(), tagIdsPopular);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD021", "Superdry Camo J Tarp Backpack", "Monochrome camo toned backpack with brand embossed details", 100, 10, new BigDecimal("129.00"), "https://laz-img-sg.alicdn.com/p/ef69ab42e19e3efd0ca60cd2e0544b50.jpg_720x720q80.jpg"), superdryCategory.getCategoryId(), tagIdsDiscount);
            
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD022", "Herschel Supply Co. Little America Backpack 17L", "The sized-down Herschel Little America Mid-Volume backpack is inspired by classic mountaineering style and constructed for everyday use.", 100, 10, new BigDecimal("66.00"), "https://laz-img-sg.alicdn.com/original/6f52a34b53a7fea14df1b3017b53d582.jpg_720x720q80.jpg"), herschelCategory.getCategoryId(), tagIdsPopularDiscount);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD023", "Herschel Supply Co. Retreat Backpack 19.5L", "Based on Herschel's Little America Bag, the Retreat Bag offers the same classic mountaineering inspired style, but in a more compact style for everyday use.", 100, 10, new BigDecimal("63.90"), "https://laz-img-sg.alicdn.com/p/fd4515275aea4d5b9cd7cb08d20cae26.jpg_720x720q80.jpg"), herschelCategory.getCategoryId(), tagIdsPopular);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD024", "Herschel Supply Co. Novel Duffel Bag Navy/Tan 42.5L", "An ideal weekender that features plenty of storage, including the convenient signature shoe compartment.", 100, 10, new BigDecimal("64.80"), "https://laz-img-sg.alicdn.com/p/7/herschel-supply-co-novel-duffel-bag-navytan-425l-9831-56443815-85d6365604db81d5dcd006f061817d17-catalog.jpg_720x720q80.jpg"), herschelCategory.getCategoryId(), tagIdsPopularNew);
            
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD025", "Coach F23216 Charles Crossbody With Varsity Stripes", "Adjustable strap with 53\" drop for shoulder or crossbody wear", 100, 10, new BigDecimal("239.00"), "https://laz-img-sg.alicdn.com/original/21e7eee2c554967ef11f633c9db9ed70.jpg_720x720q80.jpg"), coachCategory.getCategoryId(), tagIdsPopularDiscount);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD026", "Coach Charles Backpack", "Coach Signature coated canvas backpack", 100, 10, new BigDecimal("599.99"), "https://laz-img-sg.alicdn.com/original/222d46c69b99d26a26588b5e54d92e22.jpg_720x720q80.jpg"), coachCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD027", "Coach Perry Slim Brief In Crossgrain Leather Black", "Detachable strap with 53 1/4\" drop for shoulder or crossbody wear", 100, 10, new BigDecimal("395.00"), "https://laz-img-sg.alicdn.com/p/d28d784a506c74c074366a55c72832d6.jpg_720x720q80.jpg"), coachCategory.getCategoryId(), tagIdsPopularDiscountNew);
            
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD028", "Sperry Men's Authentic Original 2-Eye Boat Shoes", "Genuine hand-sewn Tru-Moc construction for durable comfort", 100, 10, new BigDecimal("89.00"), "https://laz-img-sg.alicdn.com/original/dfe77df3165e94eb4b5d6518101c351b.jpg_720x720q80.jpg"), sperryCategory.getCategoryId(), tagIdsPopularNew);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD029", "Sperry Mens Wave Driver Shoe", "Stain and water resistant leather provides durability and lasting wear", 100, 10, new BigDecimal("280.28"), "https://my-test-11.slatic.net/original/847f140cb3ad9198e86f38c8a7c43ff0.jpg_720x720q80.jpg"), sperryCategory.getCategoryId(), tagIdsDiscount);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD030", "Sperry Womens Angelfish Metallic Linen", "The Signature Cole Haan® ballet flats.", 100, 10, new BigDecimal("242.29"), "https://my-test-11.slatic.net/original/39229555f1679075499770c64214a8e8.jpg_720x720q80.jpg"), sperryCategory.getCategoryId(), tagIdsPopular);
            
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD031", "Adidas Originals Superstar Sneaker Shoes", "Express your creativity through our adidas originals shoes and footwear", 100, 10, new BigDecimal("87.65"), "https://my-test-11.slatic.net/p/ff31aa59c442f85dd1ca7a4ef485a479.jpg_720x720q80.jpg"), adidasCategory.getCategoryId(), tagIdsPopularDiscountNew);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD032", "Adidas YEEZY BOOST 350 V2", "Back with another installment, Kanye West sent the sneaker world into a frenzy once again with the Yeezy Beluga, version two of his renowned collaboration with adidas.", 100, 10, new BigDecimal("215.06"), "https://my-test-11.slatic.net/original/beec8cf42fe88002d73898daa9409f09.jpg_720x720q80.jpg"), adidasCategory.getCategoryId(), tagIdsEmpty);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD033", "Adidas PUREBOOST", "Recommended for: Street and city running; Supportive wide platform; Fitcounter heel for unrestricted fit", 100, 10, new BigDecimal("159.00"), "https://laz-img-sg.alicdn.com/p/2060c58f17c540c8bc35cf36b537820d.jpg_720x720q80.jpg"), adidasCategory.getCategoryId(), tagIdsDiscount);
            
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD034", "Vans Sneaker Old Skool", "Features: Balanced,Light,Anti-Slippery,Support,Encapsulated,Impact resistance", 100, 10, new BigDecimal("129.00"), "https://my-test-11.slatic.net/p/3258b874dc27e4e0b8e8458f3f4436fb.jpg_720x720q80.jpg"), vansCategory.getCategoryId(), tagIdsPopularDiscountNew);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD035", "Vans Authentic Sneakers Unisex", "Off the wall", 100, 10, new BigDecimal("41.71"), "https://my-test-11.slatic.net/p/e581596b9f4ca610746d18956ecf434e.jpg_720x720q80.jpg"), vansCategory.getCategoryId(), tagIdsPopularNew);
            productEntityControllerLocal.createNewProduct(new ProductEntity("PROD036", "Vans G-DRAGON Unisex", "Constructed from hard wearing leather and suede, it features contrasting leather on the side stripe and sits on top of a classic waffle sole.", 100, 10, new BigDecimal("79.90"), "https://laz-img-sg.alicdn.com/p/190cdc9429e46af18369065970b9a1e2.jpg_720x720q80.jpg"), vansCategory.getCategoryId(), tagIdsDiscount);
            

            

            
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            //January
            Date date = sdf.parse("05-01-2019");
            List<SaleTransactionLineItemEntity> janSaleTransactionLineItemEntities = new ArrayList<>();
            janSaleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(1, productEntityControllerLocal.retrieveProductByProductSkuCode("PROD003"), 1, new BigDecimal(1200), new BigDecimal(1200)));
            janSaleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(2, productEntityControllerLocal.retrieveProductByProductSkuCode("PROD004"), 1, new BigDecimal(1250), new BigDecimal(1250)));
            saleTransactionEntityControllerLocal.createNewSaleTransaction(new Long(1), new SaleTransactionEntity(2, 2, new BigDecimal(2450), date, Boolean.FALSE, customerEntityControllerLocal.retrieveCustomerByEmail("peter@gmail.com"), janSaleTransactionLineItemEntities, null, null));
            //Febuary
            Date date1 = sdf.parse("05-02-2019");
            List<SaleTransactionLineItemEntity> febSaleTransactionLineItemEntities = new ArrayList<>();
            febSaleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(3, productEntityControllerLocal.retrieveProductByProductSkuCode("PROD005"), 1, new BigDecimal(1170), new BigDecimal(1170)));
            febSaleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(4, productEntityControllerLocal.retrieveProductByProductSkuCode("PROD006"), 1, new BigDecimal(1852), new BigDecimal(1852)));
            saleTransactionEntityControllerLocal.createNewSaleTransaction(new Long(2), new SaleTransactionEntity(2, 2, new BigDecimal(3022), date1, Boolean.FALSE, customerEntityControllerLocal.retrieveCustomerByEmail("bruce@gmail.com"), febSaleTransactionLineItemEntities, null, null));
            //March
            Date date2 = sdf.parse("05-03-2019");
            List<SaleTransactionLineItemEntity> marSaleTransactionLineItemEntities = new ArrayList<>();
            marSaleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(5, productEntityControllerLocal.retrieveProductByProductSkuCode("PROD007"), 1, new BigDecimal(799), new BigDecimal(799)));
            marSaleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(6, productEntityControllerLocal.retrieveProductByProductSkuCode("PROD008"), 1, new BigDecimal(1349), new BigDecimal(1349)));
            saleTransactionEntityControllerLocal.createNewSaleTransaction(new Long(3), new SaleTransactionEntity(2, 2, new BigDecimal(2148), date2, Boolean.FALSE, customerEntityControllerLocal.retrieveCustomerByEmail("bruce@gmail.com"), marSaleTransactionLineItemEntities, null, null));
            
            CustomerEntity ce1 = customerEntityControllerLocal.retrieveCustomerByCustomerId(2L);
            ce1.setPointsForCurrentMonth(BigDecimal.ZERO);
            CustomerEntity ce2 = customerEntityControllerLocal.retrieveCustomerByCustomerId(1L);
            ce2.setPointsForCurrentMonth(BigDecimal.ZERO);
            CustomerEntity ce3 = customerEntityControllerLocal.retrieveCustomerByCustomerId(3L);
            ce3.setPointsForCurrentMonth(BigDecimal.ZERO);
            
            // Load transactions
            List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities = new ArrayList<>();
            saleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(7, productEntityControllerLocal.retrieveProductByProductSkuCode("PROD001"), 1, new BigDecimal(1598), new BigDecimal(1598)));
            saleTransactionLineItemEntities.add(new SaleTransactionLineItemEntity(8, productEntityControllerLocal.retrieveProductByProductSkuCode("PROD002"), 1, new BigDecimal(1852), new BigDecimal(1852)));
            saleTransactionEntityControllerLocal.createNewSaleTransaction(new Long(4), new SaleTransactionEntity(2, 2, new BigDecimal(3450), new Date(), Boolean.FALSE, customerEntityControllerLocal.retrieveCustomerByEmail("Steve@gmail.com"), saleTransactionLineItemEntities, null, null));
            
            
            CommunityGoalEntity cge = new CommunityGoalEntity(startDate, endDate, new BigDecimal(10000), "United States", "Impeach Donald Trump", "Get many, many points. All kinds of points. I have the best points.", new BigDecimal(4), false);
            communityGoalEntityControllerLocal.createNewCommunityGoal(cge, staffEntityControllerLocal.retrieveStaffByUsername("manager").getStaffId());

            CommunityGoalEntity cge2 = new CommunityGoalEntity(startDate, endDate, new BigDecimal(10000), "Malaysia", "For the Horde", "Fight for Azeroth and claim your prize", new BigDecimal(4), false);
            communityGoalEntityControllerLocal.createNewCommunityGoal(cge2, staffEntityControllerLocal.retrieveStaffByUsername("manager").getStaffId());
            CommunityGoalEntity cge3 = new CommunityGoalEntity(startDate, endDate, new BigDecimal(10000), "Singapore", "Rush B", "Counter terrorists win", new BigDecimal(4), false);
            communityGoalEntityControllerLocal.createNewCommunityGoal(cge3, staffEntityControllerLocal.retrieveStaffByUsername("manager").getStaffId());

            CommunityGoalEntity cge4 = new CommunityGoalEntity(startDate, endDate, new BigDecimal(5000), "United States", "Winter is Coming", "The North Remembers", new BigDecimal(4), false);
            communityGoalEntityControllerLocal.createNewCommunityGoal(cge4, staffEntityControllerLocal.retrieveStaffByUsername("manager").getStaffId());
            CommunityGoalEntity cge5 = new CommunityGoalEntity(startDate, endDate, new BigDecimal(5000), "Singapore", "National Day", "Stand up for Singapore", new BigDecimal(4), false);
            communityGoalEntityControllerLocal.createNewCommunityGoal(cge5, staffEntityControllerLocal.retrieveStaffByUsername("manager").getStaffId());

            reviewEntityControllerLocal.createNewReview(c.getCustomerId(), "Not worth the price", 3, new Long(1));
            reviewEntityControllerLocal.createNewReview(c1.getCustomerId(), "Asus laptop is the best", 5, new Long(1));
            reviewEntityControllerLocal.createNewReview(c1.getCustomerId(), "What a scam !!", 1, new Long(1));
            reviewEntityControllerLocal.createNewReview(c2.getCustomerId(), "Broke within 2 seconds", 2, new Long(1));

        } catch (InputDataValidationException ex) {
            System.err.println("********** DataInitializationSessionBean.initializeData(): " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("********** DataInitializationSessionBean.initializeData(): An error has occurred while loading initial test data: " + ex.getMessage());
        }
    }
}
