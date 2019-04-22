package services;

import domain.Actor;
import domain.Message;
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
public class CalculateSpamAndBanTest extends AbstractTest {

    @Autowired
    private ActorService actorService;

    @Autowired
    private AdministratorService administratorService;

    /*
     * Testing functional requirement : 23.2 An actor that is authenticated must be able to manage their messages
     * Positive: A company deletes a message
     * Negative: A company tries to delete a message
     * Sentence coverage: 84%
     * Data coverage: Not applicable
     */

    @Test
    public void calculateSpamDriver() {
        Object testingData[][] = {
                {
                        "admin1", null
                }, {
                "hacker1", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.calculateSpamTemplate((String) testingData[i][0],
                     (Class<?>) testingData[i][1]);
        }
    }

    private void calculateSpamTemplate(String user, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(user);
            this.administratorService.computeAllSpam();
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 23.2 An actor that is authenticated must be able to manage their messages
     * Positive: A company deletes a message
     * Negative: A company tries to delete a message
     * Sentence coverage: 84%
     * Data coverage: Not applicable
     */

    @Test
    public void banDriver() {
        Object testingData[][] = {
                {
                        "admin1", "hacker1", null
                }, {
                "admin1", "hacker2", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.banTemplate((String) testingData[i][0], (String) testingData[i][1],
                    (Class<?>) testingData[i][2]);
        }
    }

    private void banTemplate(String admin, String user,  Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(admin);
            final Actor actor = this.actorService.findOne(super.getEntityId(user));
            this.administratorService.ban(actor);
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 23.2 An actor that is authenticated must be able to manage their messages
     * Positive: A company deletes a message
     * Negative: A company tries to delete a message
     * Sentence coverage: 84%
     * Data coverage: Not applicable
     */

    @Test
    public void unbanDriver() {
        Object testingData[][] = {
                {
                        "admin1", "hacker1", "hacker1", null
                }, {
                "admin1", "hacker1","hacker2", IllegalArgumentException.class
        }
        };
        for (int i = 0; i < testingData.length; i++) {
            this.unbanTemplate((String) testingData[i][0], (String) testingData[i][1],
                    (String) testingData[i][2], (Class<?>) testingData[i][3]);
        }
    }

    private void unbanTemplate(String admin, String ban, String unban,  Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(admin);
            final Actor toBan = this.actorService.findOne(super.getEntityId(ban));
            final Actor toUnban = this.actorService.findOne(super.getEntityId(unban));
            this.administratorService.ban(toBan);
            this.administratorService.unban(toUnban);
        } catch (Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

}
