package controllers.position;

import controllers.AbstractController;
import domain.Actor;
import domain.Company;
import domain.Position;
import forms.SearchForm;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.CompanyService;
import services.PositionService;

import javax.validation.Valid;
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
            Collection<Position> positions = this.positionService.getPositionsByCompany(c);

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
        result = this.createEditModelAndView(position);
        return result;
    }

    @RequestMapping(value = "/company/edit", method = RequestMethod.POST, params = "saveDraft")
    public ModelAndView saveDraft(Position position, BindingResult binding){
        ModelAndView result;
        position.setIsFinal(false);
        position = this.positionService.reconstruct(position, binding);

        if (binding.hasErrors())
            result = this.createEditModelAndView(position);
        else
            try {
                this.positionService.saveDraft(position);
                result = new ModelAndView("redirect:list.do");
            } catch (final Throwable oops) {
                result = this.createEditModelAndView(position, "position.commit.error");
            }
        return result;
    }

    @RequestMapping(value = "/company/edit", method = RequestMethod.POST, params = "saveFinal")
    public ModelAndView saveFinal(Position position, BindingResult binding){
        ModelAndView result;
        position.setIsFinal(true);
        position = this.positionService.reconstruct(position, binding);

        if (binding.hasErrors())
            result = this.createEditModelAndView(position);
        else
            try {
                this.positionService.saveFinal(position);
                result = new ModelAndView("redirect:list.do");
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

        if (position == null || position.getIsFinal() == true)
            result = new ModelAndView("redirect:/misc/403");
        else
            result = this.createEditModelAndView(position);
        return result;
    }

    @RequestMapping(value = "/company/edit", method = RequestMethod.POST, params = "delete")
    public ModelAndView delete(final Position position) {
        ModelAndView result;
        try {
            this.positionService.delete(this.positionService.findOne(position.getId()));
            result = new ModelAndView("redirect:list.do");
        } catch (final Throwable oops) {
            result = this.createEditModelAndView(position, "position.commit.error");
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
