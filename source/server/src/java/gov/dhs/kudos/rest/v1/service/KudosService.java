package gov.dhs.kudos.rest.v1.service;

import gov.dhs.kudos.rest.v1.model.Kudos;
import gov.dhs.kudos.rest.v1.model.KudosCategory;
import gov.dhs.kudos.rest.v1.model.Organization;
import gov.dhs.kudos.rest.v1.model.User;
import gov.dhs.kudos.rest.v1.to.EmailNotificationTO;
import gov.dhs.kudos.rest.v1.to.KudosOneToManyTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Service layer for handling logic for the Kudos v1 endpoints
 * @author bsuneson
 */
@Service
public class KudosService extends KudosCategoryService
{
    /** The logger for this class **/
    private static final Logger LOG = Logger.getLogger(KudosService.class);
    
    public KudosService() 
    {
        
    }
    
    /**
     * Finds all kudos from a user
     * @param fromUserId The id of the user
     * @return A List of all kudos from a user
     */
    public List<Kudos> findAllKudosFromUser(String fromUserId)
    {
        if(LOG.isDebugEnabled())
            LOG.debug("Finding all kudos from a user");
        
        return kudosRepo.findByFromUser(userRepo.findOne(fromUserId));
    }
    
    /**
     * Finds all kudos to a user
     * @param toUserId The id of the user
     * @return A List of all kudos to a user
     */
    public List<Kudos> findAllKudosToUser(String toUserId)
    {
        if(LOG.isDebugEnabled())
            LOG.debug("Finding all kudos to a user");
        
        return kudosRepo.findByToUser(userRepo.findOne(toUserId));
    }
    
    /**
     * Finds all kudos
     * @param catId The id of the kudos category
     * @return A List of all kudos associated to the category
     */
    public List<Kudos> findAllKudosForCat(String catId)
    {
        if(LOG.isDebugEnabled())
            LOG.debug("Finding all kudos for a kudos-category");
        
        return kudosRepo.findByKudosCat(kudosCatRepo.findOne(catId));
    }
    
    /**
     * Saves a kudo
     * @param fromUserId The from user id
     * @param toUserId The to user id
     * @param kudosCatId The category id
     * @param kudo The Kudo object
     * @return The saved kudos object
     */
    public Kudos saveKudos(String fromUserId, String toUserId, String kudosCatId, Kudos kudo)
    {
        if(LOG.isDebugEnabled())
            LOG.debug("Saving kudos");
        
        kudo.setFromUser(userRepo.findOne(fromUserId));
        kudo.setToUser(userRepo.findOne(toUserId));
        kudo.setKudosCat(kudosCatRepo.findOne(kudosCatId));
        
        emailNotifier.queueEmail(new EmailNotificationTO(kudo.getFromUser().getEmail(), 
                                                         "You have recieved a KUDOS from " + kudo.getFromUser().getEmail() + ". '" + kudo.getComments() + "'", 
                                                         "KUDOS TO YOU!", 
                                                         Arrays.<String>asList(new String[]{kudo.getToUser().getEmail()})));
        
        return kudosRepo.save(kudo);
    }
    
    /**
     * Saves a kudo to multiple users
     * @param fromUserId The from user id
     * @param kudosCatId The category id 
     * @param kudosOneToMany The KudosOneToMany object
     * @return The saved List of kudos object
     */
    public List<Kudos> saveKudosOneToMany(String fromUserId, String kudosCatId, KudosOneToManyTO kudosOneToMany)
    {
        if(LOG.isDebugEnabled())
            LOG.debug("Saving kudos one-to-many");
                
        List<Kudos> resultList = new ArrayList<>();
        
        KudosCategory kudosCat = kudosCatRepo.findOne(kudosCatId);
        User fromUser = userRepo.findOne(fromUserId);
        
        EmailNotificationTO emailNotTO = new EmailNotificationTO(fromUser.getEmail(), kudosOneToMany.getComments(), "You have recieved a KUDOS from " + fromUser.getEmail());
        
        for(String toUserId : kudosOneToMany.getUserIds())
        {
            Kudos kudo = new Kudos(kudosCat, fromUser, userRepo.findOne(toUserId), kudosOneToMany.getComments());
            resultList.add(kudosRepo.save(kudo));
            
            emailNotTO.addTo(kudo.getToUser().getEmail());
        }
        
        emailNotifier.queueEmail(emailNotTO);
        
        return resultList;
    }
    
    public void initAdminData() 
    {
        Organization dhs = organizationRepo.findByOrgName("DHS");
        if(dhs == null)
        {
            usageStatisticRepo.deleteAll();
            userRepo.deleteAll();
            kudosRepo.deleteAll();
            kudosCatRepo.deleteAll();
            organizationRepo.deleteAll();
            
            User admin = userRepo.save(new User("admin@kudos.com", "kudos", "admin", "11!!qqQQaaAAzzZZ", true));
            KudosCategory dhsKudosCat0 = kudosCatRepo.save(new KudosCategory("AWWW SNAP!", "Outstanding performance. We are all impressed!","fa-thumbs-up",getRandomColor()));
            KudosCategory dhsKudosCat1 = kudosCatRepo.save(new KudosCategory("Good Citizenship", "Promoting positive morale through actions of godd spirit","fa-smile-o",getRandomColor()));
            KudosCategory dhsKudosCat2 = kudosCatRepo.save(new KudosCategory("Collaboration / Helping / Mentoring", "Leading others through partnership","fa-group",getRandomColor()));
            KudosCategory dhsKudosCat3 = kudosCatRepo.save(new KudosCategory("Bright Idea / Creativity", "Honoring the creative problem solver","fa-lightbulb-o",getRandomColor()));
            KudosCategory dhsKudosCat4 = kudosCatRepo.save(new KudosCategory("Above and Beyond", "Modeling superior service","fa-bolt",getRandomColor()));
            KudosCategory dhsKudosCat5 = kudosCatRepo.save(new KudosCategory("Make it Happen", "Relentlessly resourceful and productive","fa-coffee",getRandomColor()));
            KudosCategory dhsKudosCat6 = kudosCatRepo.save(new KudosCategory("Unsung Hero", "Working behind the scenes","fa-star-half-o",getRandomColor()));
            KudosCategory dhsKudosCat7 = kudosCatRepo.save(new KudosCategory("Going Green", "Providing outstanding contributions towards sustainability","fa-tree",getRandomColor()));
            KudosCategory dhsKudosCat8 = kudosCatRepo.save(new KudosCategory("Saved Money", "Reduced cost or prevented unnecessary expenditures","fa-money",getRandomColor()));
            KudosCategory dhsKudosCat9 = kudosCatRepo.save(new KudosCategory("Increased Throughput", "Increased the speed or productivity of peers or processes","fa-fighter-jet",getRandomColor()));
            KudosCategory dhsKudosCat10 = kudosCatRepo.save(new KudosCategory("Enhanced Quality", "Enhanced the output of people or processes","fa-check",getRandomColor()));
            KudosCategory dhsKudosCat11 = kudosCatRepo.save(new KudosCategory("Holy Grail of Efficiency", "Saved Money, Increased Throughput and Enhanced Quality all at once","fa-thumbs-up",getRandomColor()));
            
            dhs = organizationRepo.save(new Organization("DHS", Arrays.<User>asList(new User[] {admin}), 
                                                                Arrays.<KudosCategory>asList(new KudosCategory[] 
                                                                                                                {
                                                                                                                    dhsKudosCat0,dhsKudosCat1,dhsKudosCat2,
                                                                                                                    dhsKudosCat3,dhsKudosCat4,dhsKudosCat5,
                                                                                                                    dhsKudosCat6,dhsKudosCat7,dhsKudosCat8,
                                                                                                                    dhsKudosCat9,dhsKudosCat10,dhsKudosCat11
                                                                                                                })));
            
            kudosCatRepo.save(new KudosCategory("GLOBAL 1", "BANG"));
        }
    }
    
    private String getRandomColor()
    {
        String r,g,b;
        
        r = Integer.toHexString((int) (Math.round(Math.random()*127)+127));
        g = Integer.toHexString((int) (Math.round(Math.random()*127)+127));
        b = Integer.toHexString((int) (Math.round(Math.random()*127)+127));
        
        return ("#"+r+""+g+""+b);
    }
}
