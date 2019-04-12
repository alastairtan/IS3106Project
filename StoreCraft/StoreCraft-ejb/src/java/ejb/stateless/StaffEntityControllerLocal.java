/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.StaffEntity;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.StaffTypeEnum;
import util.exception.CreateNewStaffException;
import util.exception.DeleteStaffException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.UpdateStaffException;

/**
 *
 * @author shawn
 */
public interface StaffEntityControllerLocal {

    public StaffEntity createNewStaff(StaffEntity newStaffEntity) throws InputDataValidationException, CreateNewStaffException;

    public List<StaffEntity> retrieveAllStaffs();

    public StaffEntity retrieveStaffByStaffId(Long staffId) throws StaffNotFoundException;

    public StaffEntity retrieveStaffByUsername(String username) throws StaffNotFoundException;

    public StaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException;

    public void deleteStaff(Long staffId) throws StaffNotFoundException, DeleteStaffException;

    public void updateStaff(StaffEntity staffEntity) throws InputDataValidationException, StaffNotFoundException, UpdateStaffException;

    public void updatePassword(StaffEntity staffEntity, String oldPasword, String newPassword) throws StaffNotFoundException, InvalidLoginCredentialException;

    public List<StaffEntity> filterStaffsByStaffTypeEnum(List<String> staffTypes, String condition);
    
}
