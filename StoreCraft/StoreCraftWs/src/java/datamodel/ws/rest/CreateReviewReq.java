package datamodel.ws.rest;



public class CreateReviewReq
{
    private Long customerId;
    private String newContent;
    private Integer newProductRating;
    private Long productId;

    
    public CreateReviewReq()
    {
    }

    public CreateReviewReq(Long customerId, String newContent, Integer newProductRating, Long productId) {
        this.customerId = customerId;
        this.newContent = newContent;
        this.newProductRating = newProductRating;
        this.productId = productId;
    }


    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    public Integer getNewProductRating() {
        return newProductRating;
    }

    public void setNewProductRating(Integer newProductRating) {
        this.newProductRating = newProductRating;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    
    
    
    
    

}