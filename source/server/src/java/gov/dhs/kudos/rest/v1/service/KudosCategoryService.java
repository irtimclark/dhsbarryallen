package gov.dhs.kudos.rest.v1.service;

import gov.dhs.kudos.rest.v1.model.KudosCategory;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Service layer for handling logic for the Kudos Category v1 endpoints
 * @author bsuneson
 */
public class KudosCategoryService extends UserService
{
    /** The logger for this class **/
    private static final Logger LOG = Logger.getLogger(KudosCategoryService.class);
        
    /**
     * Finds a kudos category by name
     * @param name The name of the kudos category
     * @return A matching kudos category object
     */
    public KudosCategory findKudosCatByName(String name) 
    {
        if(LOG.isDebugEnabled())
            LOG.debug("Finding kudos-category by name");
        
        return kudosCatRepo.findByNameIgnoreCase(name);
    }
    
    /**
     * Finds a kudos category by id
     * @param id The id of the kudos category
     * @return A matching kudos category object
     */
    public KudosCategory findKudosCatById(String id) 
    {
        if(LOG.isDebugEnabled())
            LOG.debug("Finding kudos-category by id");
        
        return kudosCatRepo.findOne(id);
    }
    
    /**
     * Finds all kudos categories
     * @return A List of all kudos categories
     */
    public List<KudosCategory> findAllKudosCats() 
    {
        if(LOG.isDebugEnabled())
            LOG.debug("Finding all kudos-categories within an org");
        
        return kudosCatRepo.findAll();
    }
    
    /**
     * Saves a kudos category
     * @param kudosCat The kudos category to save
     * @return The saved kudos category object
     */
    public KudosCategory saveKudosCat(KudosCategory kudosCat)
    {
        if(LOG.isDebugEnabled())
            LOG.debug("Saving kudos-categories");
        
        kudosCat.setName(kudosCat.getName().trim());
        
        return kudosCatRepo.save(kudosCat);
    }
    
    /**
     * Updates a kudos category
     * @param kudosCat The kudos category to save
     * @return The saved kudos category
     */
    public KudosCategory updateKudosCat(KudosCategory kudosCat)
    {
        if(LOG.isDebugEnabled())
            LOG.debug("Updating kudos-categories");
        
        kudosCat.setName(kudosCat.getName().trim());
        
        return kudosCatRepo.save(kudosCat);
    }
}
