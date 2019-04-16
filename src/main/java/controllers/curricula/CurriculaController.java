package controllers.curricula;

import controllers.AbstractController;
import domain.Actor;
import domain.Curricula;
import domain.Hacker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.CurriculaService;
import services.HackerService;

import java.util.Collection;

@Controller
@RequestMapping("/curricula/hacker")
public class CurriculaController extends AbstractController {

    @Autowired
    private CurriculaService curriculaService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private HackerService hackerService;

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ModelAndView list(){
        ModelAndView result;
        try{
            Actor a = actorService.getActorLogged();
            Hacker h = hackerService.findOne(a.getId());
            Assert.notNull(h);
            Collection<Curricula> curriculas = this.curriculaService.getCurriculaByHacker();
            result = new ModelAndView("curricula/hacker/list");
            result.addObject("curriculas",curriculas);
            result.addObject("RequestURI","curricula/hacker/list.do");
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/misc/403");
        }
        return result;
    }

    @RequestMapping(value = "/display", method = RequestMethod.GET)
    public ModelAndView show(@RequestParam int curriculaId){
        ModelAndView result;
        try{
            Curricula c = this.curriculaService.findOne(curriculaId);
            Assert.notNull(c);
            Assert.isTrue(c.getIsCopy()==false);

            Actor a = actorService.getActorLogged();
            Hacker h = hackerService.findOne(a.getId());
            Assert.notNull(h);
            Assert.isTrue(h.getCurricula().contains(c));

            result = new ModelAndView("curricula/hacker/display");
            result.addObject("curricula",c);
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/misc/403");
        }
        return result;
    }
}
