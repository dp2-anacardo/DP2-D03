package controllers.position;

import controllers.AbstractController;
import domain.Actor;
import domain.Company;
import domain.Position;
import domain.Problem;
import forms.SearchForm;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.CompanyService;
import services.PositionService;
import services.ProblemService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/position")
public class PositionController extends AbstractController {

    @Autowired
    private PositionService positionService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ProblemService problemService;

    @RequestMapping(value = "/listNotLogged", method = RequestMethod.GET)
    public ModelAndView listNotLogged(){
        ModelAndView result;
        List<Position> positionsAvailables = this.positionService.getPositionsAvilables();
        result = new ModelAndView("position/listNotLogged");
        result.addObject("positions", positionsAvailables);
        result.addObject("RequestURI", "position/listNotLogged.do");

        return result;
    }

    @RequestMapping(value = "/company/list", method = RequestMethod.GET)
    public ModelAndView list(){
        ModelAndView result;
        try {
            int id = this.actorService.getActorLogged().getId();
            Company c = this.companyService.findOne(id);
            Collection<Position> positions = this.positionService.getPositionsByCompanyAll(c);

            result = new ModelAndView("position/company/list");
            result.addObject("positions", positions);
            result.addObject("RequestURI", "position/company/list.do");
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/misc/403");
        }
        return result;

    }

    @RequestMapping(value = "/company/create", method = RequestMethod.GET)
    public ModelAndView create(){
        ModelAndView result;
        Position position;
        position = new Position();
        Company c = this.companyService.findOne(this.actorService.getActorLogged().getId());
        Collection<Problem> problems = this.problemService.getProblemsFinalByCompany(c);
        result = this.createEditModelAndView(position);
        result.addObject("problems", problems);
        return result;
    }

    @RequestMapping(value = "/company/edit", method = RequestMethod.POST, params = "saveDraft")
    public ModelAndView saveDraft(Position position, BindingResult binding){
        ModelAndView result;
        Collection<Problem> problems = this.problemService.getProblemsFinalByCompany((Company) this.actorService.getActorLogged());

        if (position.getIsFinal() == true){
            result = this.createEditModelAndView(position, "position.commit.error");
            result.addObject("problems", problems);
        }else {
            position.setIsFinal(false);

            try {
                Actor a = this.actorService.getActorLogged();
                Company c = this.companyService.findOne(a.getId());
                Assert.notNull(c);
                position.setCompany(c);

                position = this.positionService.reconstruct(position, binding);
                position = this.positionService.saveDraft(position);
                result = new ModelAndView("redirect:list.do");
            } catch (ValidationException e) {
                result = this.createEditModelAndView(position, null);
                result.addObject("problems", problems);
            } catch (final Throwable oops) {
                result = this.createEditModelAndView(position, "position.commit.error");
                result.addObject("problems", problems);
            }
        }
        return result;
    }

    @RequestMapping(value = "/company/edit", method = RequestMethod.POST, params = "saveFinal")
    public ModelAndView saveFinal(Position position, BindingResult binding){
        ModelAndView result;
        Collection<Problem> problems = this.problemService.getProblemsFinalByCompany((Company) this.actorService.getActorLogged());
        if(position.getProblems()==null){
            result = this.createEditModelAndView(position, "position.commit.error");
            result.addObject("problems", problems);
        }
        else if (position.getProblems().size() < 2 || position.getIsFinal() == true){
           result = this.createEditModelAndView(position, "position.commit.error");
           result.addObject("problems", problems);
       }else {
           position.setIsFinal(true);

           try {
               Actor a = this.actorService.getActorLogged();
               Company c = this.companyService.findOne(a.getId());
               Assert.notNull(c);
               position.setCompany(c);
               position = this.positionService.reconstruct(position, binding);
               position = this.positionService.saveFinal(position);
               result = new ModelAndView("redirect:list.do");
           } catch (ValidationException e) {
               result = this.createEditModelAndView(position, null);
               result.addObject("problems", problems);
           } catch (final Throwable oops) {
               result = this.createEditModelAndView(position, "position.commit.error");
           }
       }
        return result;
    }
    
    @RequestMapping(value = "/company/cancel", method = RequestMethod.GET)
    public ModelAndView cancel(@RequestParam final int positionId){
        ModelAndView result;
        Position position = this.positionService.findOne(positionId);
        Company c = this.companyService.findOne(this.actorService.getActorLogged().getId());
            try {
                Assert.notNull(c);
                Assert.isTrue(position.getIsFinal());
                Assert.isTrue(position.getCompany().equals(c));
                position.setIsCancelled(true);
                this.positionService.save(position);
                result = new ModelAndView("redirect:list.do");
            } catch (ValidationException e) {
                result = this.createEditModelAndView(position, null);
            } catch (final Throwable oops) {
                result = this.createEditModelAndView(position, "position.commit.error");
            }
        return result;
    }

    @RequestMapping(value = "/company/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam final int positionId){
        ModelAndView result;
        Position position;

        position = this.positionService.findOne(positionId);
        Company c = this.companyService.findOne(this.actorService.getActorLogged().getId());

        if (position == null || position.getIsFinal() == true || !(position.getCompany().equals(c)))
            result = new ModelAndView("redirect:/misc/403");
        else
            try {
                Collection<Problem> problems = this.problemService.getProblemsFinalByCompany(c);;
                result = this.createEditModelAndView(position);
                result.addObject("problems",problems);
            }catch(Throwable oops) {
                result = new ModelAndView("redirect:/misc/403");
            }
        return result;
    }


    @RequestMapping(value = "/company/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam int positionId) {
        ModelAndView result;
        Position p = this.positionService.findOne(positionId);
        Company c = this.companyService.findOne(this.actorService.getActorLogged().getId());
            try {
                Assert.notNull(c);
                Assert.isTrue(p.getIsFinal() == false);
                Assert.isTrue(p.getCompany().equals(c));
                this.positionService.delete(p);
                result = new ModelAndView("redirect:list.do");
            } catch (final Throwable oops) {
                result = new ModelAndView("redirect:/misc/403");
            }
        return result;
    }


    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public ModelAndView show(@RequestParam int positionId){
        ModelAndView result;
        Position p = this.positionService.findOne(positionId);

        result = new ModelAndView("position/show");
        result.addObject("position", p);

        return result;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView search() {
        ModelAndView result = new ModelAndView("position/search");
        result.addObject("search", new SearchForm());
        return result;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, params = "search")
    public ModelAndView search(@ModelAttribute("search") @Valid final SearchForm search, final BindingResult binding) {
        ModelAndView result;
        final Collection<Position> positions;

        if (binding.hasErrors()) {
            result = new ModelAndView("redirect:/");
        } else {
            try {

                positions = this.positionService.searchPositions(search.getKeyword());

                if (positions.isEmpty()) {
                    result = new ModelAndView("redirect:/position/listNotLogged.do");
                } else {
                    result = new ModelAndView("position/search");
                    result.addObject("positions", positions);
                }
            } catch (Throwable oops) {
                result = new ModelAndView("redirect:/");
            }
        }
        return result;
    }

    private ModelAndView createEditModelAndView(Position position){
        ModelAndView result;
        result =this.createEditModelAndView(position, null);
        return result;
    }

    private ModelAndView createEditModelAndView(Position position, String messageCode){
        ModelAndView result;

        result = new ModelAndView("position/company/edit");
        result.addObject("position",position);
        result.addObject("message", messageCode);
        return result;
    }

    /*// MODEL&VIEW
    protected ModelAndView editModelAndView(final Configuration config) {
        ModelAndView result;
        final ConfigurationForm configF = new ConfigurationForm(config);
        result = this.editModelAndView(configF, null);
        return result;
    }

    protected ModelAndView editModelAndView(final ConfigurationForm configF, final String messageCode) {
        ModelAndView result;

        result = new ModelAndView("configuration/administrator/edit");
        result.addObject("configF", configF);
        result.addObject("messageCode", messageCode);

        return result;
    }*/


}
