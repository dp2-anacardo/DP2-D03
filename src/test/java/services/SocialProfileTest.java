package services;

import domain.SocialProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SocialProfileTest extends AbstractTest {

    @Autowired
    private SocialProfileService socialProfileService;


    @Test
    public void createSocialProfileDriver(){
        Object testingData [][] ={
                {
                    "prueba", "prueba", "http://www.twitter.com", null, "admin1"
                }, {
                    "", "prueba", "http://www.twitter.com", ValidationException.class, "admin1"
                }

        };
        for(int i=0; i<testingData.length; i++)
            this.createSocialProfileTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3],
                    (String) testingData[i][4]);
    }

    private void createSocialProfileTemplate(String s1, String s2, String s3, Class<?> expected,
                                             String s4){
        Class<?> caught;
        caught = null;

        try{
            this.authenticate(s4);
            SocialProfile s = this.socialProfileService.create();
            s.setNick(s1);
            s.setSocialNetworkName(s2);
            s.setProfileLink(s3);
            DataBinder binding = new DataBinder(new SocialProfile());
            s = this.socialProfileService.reconstruct(s, binding.getBindingResult());
            this.socialProfileService.save(s);
        } catch (Throwable oops){
            caught = oops.getClass();
        }

        super.checkExceptions(expected,caught);
    }
/*

    @Test
    public void editSocialProfileDriver(){
        Object testingData[][] = {
                {
                    "pruebesita","admin1", null
                }, {

                }
        };
        for (int i = 0; i<testingData.length; i++)
            this.editSocialProfileTemplate((String) testingData[i][0],(String) testingData[i][1], (Class<?>) testingData[i][2]);
    }

    private void editSocialProfileTemplate(String s1, String admin, Class<?> expected){
        Class<?> caught;
        caught = null;

        try{
            this.authenticate(admin);
            SocialProfile s = this.socialProfileService.findOne();
            s.setNick(s1);
            DataBinder binding = new DataBinder(new SocialProfile());
            s = this.socialProfileService.reconstruct(s, binding.getBindingResult());
            this.socialProfileService.save(s);
        } catch (Throwable oops){
            caught = oops.getClass();
        }
        super.checkExceptions(expected, caught);
    } */
}
