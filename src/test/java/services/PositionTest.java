package services;

import domain.Company;
import domain.Position;
import domain.Problem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PositionTest extends AbstractTest {

    @Autowired
    private PositionService positionService;

    @Autowired
    private  CompanyService companyService;

    @Autowired
    private ProblemService problemService;


    /*
     * Testing functional requirement : 9.1 An actor that is authenticated as a company must be able to manage their positions
     * Positive: A company create a position as draft and as final
     * Negative: A company tries to create a position with invalid data
     * Sentence coverage: 84%
     * Data coverage: 40%
     */

    @Test
    public void createPositionDriver(){
        Object testingData[][] = {
                {
                        "prueba", "prueba", new Date(02/02/2025), "prueba", "skill", "technology", 20, false, false, "company1", null
                }, {
                        "prueba", "prueba", new Date(02/02/2025), "prueba", "skill", "technology", 20, true, false, "company1", null
                 }, {
                        "", "prueba", new Date(02/02/2025), "prueba", "skill", "technology", 20, false, false, "company1", ValidationException.class
                },
                {
                        "prueba", "", new Date(02/02/2025), "prueba", "skill", "technology", 20, false, false, "company1", ValidationException.class
                }
        };
        for (int i = 0; i < testingData.length; i++){
            this.createPositionTemplate((String) testingData[i][0],(String) testingData[i][1],
                    (Date) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4],
                    (String) testingData[i][5], (int) testingData[i][6], (boolean) testingData[i][7],
                    (boolean) testingData[i][8], (String) testingData[i][9], super.getEntityId((String) testingData[i][9]), (Class<?>) testingData[i][10]);
        }
    }

    private void createPositionTemplate(String title, String description, Date deadline,
                                        String profile, String skill,
                                        String technology, int salary, boolean isFinal,
                                        boolean isCancelled, String company, int entityId,
                                        Class<?> expected){
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(company);

            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 1);
            final Date nextYear = cal.getTime();

            Position p = this.positionService.create();
            p.setTitle(title);
            p.setDescription(description);
            p.setDeadline(nextYear);
            p.setProfile(profile);
            Collection<String> s = new ArrayList<String>();
            s.add(skill);
            p.setSkill(s);
            Collection<String> t = new ArrayList<String>();
            t.add(technology);
            p.setTechnology(t);
            p.setSalary(salary);
            p.setIsFinal(isFinal);
            p.setIsCancelled(isCancelled);
            Company c = this.companyService.findOne(entityId);
            Collection<Problem> problems = this.problemService.findAllByCompany(c.getId());
            p.setCompany(c);
            p.setProblems(problems);
            final DataBinder binding = new DataBinder(new Position());
            p = this.positionService.reconstruct(p, binding.getBindingResult());
            if(p.getIsFinal()){
                this.positionService.saveFinal(p);
            }
            else {
                this.positionService.saveDraft(p);
            }
        } catch (Throwable oops){
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }


    /*
     * Testing functional requirement : 9.1 An actor that is authenticated as a company must be able to manage their positions
     * Positive: A company save a position as final
     * Negative: A company tries to save as final a position with less than 2 problems
     * Sentence coverage: 84%
     * Data coverage: 20%
     */
    @Test
    public void editPositionToFinalDriver(){
        Object testingData[][] = {
                {
                      "position2", "company1", null
                }, {
                    "position1", "company1", IllegalArgumentException.class
                }
        };
        for (int i = 0; i < testingData.length; i++){
            this.editPositionToFinalTemplate(super.getEntityId((String) testingData[i][0]),
                  (String) testingData[i][1], (Class<?>) testingData[i][2]);
        }
    }

    private void editPositionToFinalTemplate(int entityId, String company, Class<?> expected) {
            Class<?> caught;
            caught = null;

            try {
              this.authenticate(company);
              Position p = this.positionService.findOne(entityId);
              final DataBinder binding = new DataBinder(new Position());
              p = this.positionService.reconstruct(p, binding.getBindingResult());
              this.positionService.saveFinal(p);
            } catch (Throwable oops){
                caught = oops.getClass();
            }

            super.checkExceptions(expected, caught);
    }


    /*
     * Testing functional requirement : 9.1 An actor that is authenticated as a company must be able to manage their positions
     * Positive: A company edit a position
     * Negative: A company tries to edit a position with invalid data
     * Sentence coverage: 84%
     * Data coverage: 20%
     */
    @Test
    public void editPositionDriver(){
        Object testingData[][] = {
                {
                        "position2", "company1", null, "prueba"
                }, {
                "position2", "company1", ValidationException.class, ""
        }
        };
        for (int i = 0; i < testingData.length; i++){
            this.editPositionTemplate(super.getEntityId((String) testingData[i][0]),
                    (String) testingData[i][1], (Class<?>) testingData[i][2], (String) testingData[i][3]);
        }
    }

    private void editPositionTemplate(int entityId, String company, Class<?> expected, String s) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(company);
            Position p = this.positionService.findOne(entityId);
            final DataBinder binding = new DataBinder(new Position());
            p.setTitle(s);
            p = this.positionService.reconstruct(p, binding.getBindingResult());
            this.positionService.saveDraft(p);
        } catch (Throwable oops){
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Testing functional requirement : 9.1 An actor that is authenticated as a company must be able to manage their positions
     * Positive: A company delete a position
     * Negative: A company tries to delete a position in final mode
     * Sentence coverage: 84%
     * Data coverage: Not applicable 
     */
    @Test
    public void deletePositionDriver(){
        Object testingData[][] = {
                {
                        "position1", "company1", IllegalArgumentException.class
                }, {
                        "position2", "company1", null
        }
        };
        for (int i = 0; i < testingData.length; i++){
            this.deletePositionTemplate(super.getEntityId((String) testingData[i][0]),
                    (String) testingData[i][1], (Class<?>) testingData[i][2]);
        }
    }

    private void deletePositionTemplate(int entityId, String company, Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(company);
            Position p = this.positionService.findOne(entityId);
            this.positionService.delete(p);
        } catch (Throwable oops){
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }



}
