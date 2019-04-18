package controllers.curricula;

import controllers.AbstractController;
import domain.Actor;
import domain.Curricula;
import domain.Hacker;
import domain.PersonalData;
import org.hibernate.stat.internal.ConcurrentCollectionStatisticsImpl;
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
import services.CurriculaService;
import services.HackerService;
import services.PersonalDataService;

import javax.validation.ValidationException;
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

    @Autowired
    private PersonalDataService personalDataService;

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

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(){
        ModelAndView result;
        try{
            PersonalData p = this.personalDataService.create();
            result = this.createEditModelAndView(p,null);
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/misc/403");
        }

        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("personalD") PersonalData p, BindingResult binding){
        ModelAndView result;
        try{
            p = this.personalDataService.reconstruct(p,binding);
            p = this.personalDataService.save(p);
            Curricula c = this.curriculaService.create();
            c.setPersonalData(p);
            c = this.curriculaService.save(c);
            result = new ModelAndView("redirect:list.do");
        }catch(final ValidationException e){
            result = this.createEditModelAndView(p,null);
        }catch(Throwable oops){
            result = this.createEditModelAndView(p,"curricula.commit.error");
        }
        return result;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam int curriculaId){
        ModelAndView result;
        try{
            Curricula c = this.curriculaService.findOne(curriculaId);
            this.curriculaService.delete(c);
            result = new ModelAndView("redirect:list.do");
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/misc/403");
        }
        return result;
    }

    private ModelAndView createEditModelAndView(PersonalData p, String messageCode){
        ModelAndView result;

        result = new ModelAndView("curricula/hacker/create");
        result.addObject("personalD", p);
        result.addObject("messageCode", messageCode);

        return result;

    }
}
