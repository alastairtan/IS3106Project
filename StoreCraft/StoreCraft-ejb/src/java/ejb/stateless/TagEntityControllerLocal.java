/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.TagEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewTagException;
import util.exception.DeleteTagException;
import util.exception.InputDataValidationException;
import util.exception.TagNotFoundException;
import util.exception.UpdateTagException;

/**
 *
 * @author shawn
 */
@Local
public interface TagEntityControllerLocal {

    public TagEntity createNewTagEntity(TagEntity newTagEntity) throws InputDataValidationException, CreateNewTagException;

    public List<TagEntity> retrieveAllTags();

    public TagEntity retrieveTagByTagId(Long tagId) throws TagNotFoundException;

    public void updateTag(TagEntity tagEntity) throws InputDataValidationException, TagNotFoundException, UpdateTagException;

    public void deleteTag(Long tagId) throws TagNotFoundException, DeleteTagException;
    
}
