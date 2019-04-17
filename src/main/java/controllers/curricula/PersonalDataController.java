package controllers.curricula;

import controllers.AbstractController;
import domain.Actor;
import domain.Curricula;
import domain.Hacker;
import domain.PersonalData;
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
import services.HackerService;
import services.PersonalDataService;

import javax.naming.Binding;
import javax.validation.ValidationException;

@Controller
@RequestMapping("/personalData/hacker")
public class PersonalDataController extends AbstractController {

    @Autowired
    private PersonalDataService personalDataService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private HackerService hackerService;

    @RequestMapping(value = "/edit",method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam int personalDataId){
        ModelAndView result;
        try{
            PersonalData p = this.personalDataService.findOne(personalDataId);
            Actor a = this.actorService.getActorLogged();
            Hacker h = this.hackerService.findOne(a.getId());
            boolean canEdit = false;
            for(Curricula c: h.getCurricula()){
                if(c.getPersonalData().equals(p))
                    canEdit = true;
            }
            Assert.isTrue(canEdit);
            result = this.createEditModelAndView(p,null);
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/misc/403");
        }
        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("personalD") PersonalData p, BindingResult binding){
        ModelAndView result;
        try{
            p = this.personalDataService.reconstruct(p,binding);
            p = this.personalDataService.save(p);
            result = new ModelAndView("redirect:/curricula/hacker/list.do");
        }catch(final ValidationException e){
            result = this.createEditModelAndView(p,null);
        }catch(Throwable oops){
            result = this.createEditModelAndView(p,"curricula.commit.error");
        }
        return result;
    }


    private ModelAndView createEditModelAndView(PersonalData p, String messageCode){
        ModelAndView result;

        result = new ModelAndView("personalData/hacker/edit");
        result.addObject("personalD", p);
        result.addObject("message", messageCode);

        return result;

    }
}
