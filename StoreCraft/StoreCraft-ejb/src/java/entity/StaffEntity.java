/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.StaffTypeEnum;
import util.security.CryptographicHelper;

/**
 *
 * @author shawn
 */
@Entity
public class StaffEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;
   
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String firstName;
    
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String lastName;
    
    @Column(nullable = false, unique = true, length = 32)
    @NotNull
    @Size(min = 4, max = 32)
    private String username;
    
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    @NotNull
    private String password;
    
    private String profilePicUrl; 
    
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt;
    
    @OneToMany(mappedBy = "staffEntity")
    private List<CommunityGoalEntity> communityGoalEntities;
    
    @OneToMany(mappedBy = "staffEntity")
    private List<ReviewEntity> reviewEntities; 
    
    @NotNull
    //@Enumerated(EnumType.STRING)
    private StaffTypeEnum staffTypeEnum;

    public StaffEntity() {
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
        this.communityGoalEntities = new ArrayList<>();
        this.reviewEntities = new ArrayList<>();
    }

    public StaffEntity(String firstName, String lastName, String username, String password, StaffTypeEnum staffTypeEnum, String profilePicUrl) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.staffTypeEnum = staffTypeEnum;
        this.profilePicUrl = profilePicUrl;
        
        setPassword(password);
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (staffId != null ? staffId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the staffId fields are not set
        if (!(object instanceof StaffEntity)) {
            return false;
        }
        StaffEntity other = (StaffEntity) object;
        if ((this.staffId == null && other.staffId != null) || (this.staffId != null && !this.staffId.equals(other.staffId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.StaffEntity[ id=" + staffId + " ]";
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password != null)
        {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
        }
        else
        {
            this.password = null;
        }
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<CommunityGoalEntity> getCommunityGoalEntities() {
        return communityGoalEntities;
    }

    public void setCommunityGoalEntities(List<CommunityGoalEntity> communityGoalEntities) {
        this.communityGoalEntities = communityGoalEntities;
    }

    public StaffTypeEnum getStaffTypeEnum() {
        return staffTypeEnum;
    }

    public void setStaffTypeEnum(StaffTypeEnum staffTypeEnum) {
        this.staffTypeEnum = staffTypeEnum;
    }

    public List<ReviewEntity> getReviewEntities() {
        return reviewEntities;
    }

    public void setReviewEntities(List<ReviewEntity> reviewEntities) {
        this.reviewEntities = reviewEntities;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }
    
}
