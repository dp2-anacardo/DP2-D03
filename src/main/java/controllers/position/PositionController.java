package controllers.position;

import controllers.AbstractController;
import domain.Position;
import forms.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.PositionService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/position")
public class PositionController extends AbstractController {

    @Autowired
    private PositionService positionService;

    @RequestMapping(value = "/listNotLogged", method = RequestMethod.GET)
    public ModelAndView listNotLogged(){
        ModelAndView result;
        List<Position> positionsAvailables = this.positionService.getPositionsAvilables();
        result = new ModelAndView("position/listNotLogged");
        result.addObject("positions", positionsAvailables);
        result.addObject("RequestURI", "position/listNotLogged.do");

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
                    result = new ModelAndView("redirect:/position/search.do");
                    result.addObject("positions", positions);
                }
            } catch (Throwable oops) {
                result = new ModelAndView("redirect:/");
            }
        }
        return result;
    }
}
