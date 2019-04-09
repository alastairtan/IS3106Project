package datamodel.ws.rest;

import entity.TagEntity;
import java.util.List;



public class RetrieveAllTagsRsp
{
    private List<TagEntity> tagEntities;

    
    public RetrieveAllTagsRsp()
    {
    }
    
    
    
    public RetrieveAllTagsRsp(List<TagEntity> tagEntities)
    {
        this.tagEntities = tagEntities;
    }

    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public void setTagEntities(List<TagEntity> tagEntities) {
        this.tagEntities = tagEntities;
    }

    

}