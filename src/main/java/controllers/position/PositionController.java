package controllers.position;

import controllers.AbstractController;
import domain.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.PositionService;

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
}
