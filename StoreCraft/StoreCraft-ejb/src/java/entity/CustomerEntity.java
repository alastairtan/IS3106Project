/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;
import util.enumeration.MembershipTierEnum;
import util.security.CryptographicHelper;

/**
 * Should we add an attribute of hasWon? To make sure that they don't win again
 */

@Entity
public class CustomerEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String firstName; 
    
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String lastName;
    
    @Column(nullable = false, unique = true, length = 64)
    @NotNull
    @Size(max = 64)
    @Email
    private String email; //email is username
    
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    @NotNull
    private String password;
    
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt; 
    
    @NotNull
    private MembershipTierEnum membershipTierEnum;
    
    @NotNull
    private String country;
    
    @DecimalMin("0.00")
    private BigDecimal totalPoints;
    @DecimalMin("0.00")
    private BigDecimal remainingPoints;
    @DecimalMin("0.00")
    private BigDecimal pointsForCurrentMonth;
    @DecimalMin("1.00")
    private BigDecimal multiplier;  
    
    private String profilePicUrl; 
    
    //RELATIONSHIPS
    
    @ManyToMany
    private List<DiscountCodeEntity> discountCodeEntities;
    
    @OneToMany(mappedBy = "customerEntity")
    private List<ReviewEntity> reviewEntities; 
    
    @OneToMany(mappedBy = "customerEntity")
    private List<SaleTransactionEntity> saleTransactionEntities;
    
    
    
    public CustomerEntity() {
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
        this.reviewEntities = new ArrayList<>();
        this.totalPoints = new BigDecimal(0.00);
        this.remainingPoints = new BigDecimal(0.00);
        this.pointsForCurrentMonth = new BigDecimal(0.00);
        this.multiplier = new BigDecimal(1.00);
    }

    public CustomerEntity(String firstName, String lastName, String email, String password, String country) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.country = country;
    }
    
    public void setPassword(String password)
    {
        if(password != null)
        {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
        }
        else
        {
            this.password = null;
        }
    }
    
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof CustomerEntity)) {
            return false;
        }
        CustomerEntity other = (CustomerEntity) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerEntity[ id=" + customerId + " ]";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public MembershipTierEnum getMembershipTierEnum() {
        return membershipTierEnum;
    }

    public void setMembershipTierEnum(MembershipTierEnum membershipTierEnum) {
        this.membershipTierEnum = membershipTierEnum;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BigDecimal getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(BigDecimal totalPoints) {
        this.totalPoints = totalPoints;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }

    public BigDecimal getRemainingPoints() {
        return remainingPoints;
    }

    public void setRemainingPoints(BigDecimal remainingPoints) {
        this.remainingPoints = remainingPoints;
    }

    public BigDecimal getPointsForCurrentMonth() {
        return pointsForCurrentMonth;
    }

    public void setPointsForCurrentMonth(BigDecimal pointsForCurrentMonth) {
        this.pointsForCurrentMonth = pointsForCurrentMonth;
    }
    
    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public List<ReviewEntity> getReviewEntities() {
        return reviewEntities;
    }

    public void setReviewEntities(List<ReviewEntity> reviewEntities) {
        this.reviewEntities = reviewEntities;
    }

    public List<SaleTransactionEntity> getSaleTransactionEntities() {
        return saleTransactionEntities;
    }

    public void setSaleTransactionEntities(List<SaleTransactionEntity> saleTransactionEntities) {
        this.saleTransactionEntities = saleTransactionEntities;
    }

    public List<DiscountCodeEntity> getDiscountCodeEntities() {
        return discountCodeEntities;
    }

    public void setDiscountCodeEntities(List<DiscountCodeEntity> discountCodeEntities) {
        this.discountCodeEntities = discountCodeEntities;
    }

    public String getId() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
