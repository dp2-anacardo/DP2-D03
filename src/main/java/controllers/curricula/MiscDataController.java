package controllers.curricula;

import controllers.AbstractController;
import domain.Actor;
import domain.Hacker;
import domain.MiscData;
import domain.PositionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.HackerService;
import services.MiscDataService;

import javax.validation.ValidationException;

@Controller
@RequestMapping("miscData/hacker")
public class MiscDataController extends AbstractController {
    @Autowired
    private MiscDataService miscDataService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private HackerService hackerService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam int curriculaId){
        ModelAndView result;
        try{
            MiscData p = this.miscDataService.create();
            result = this.createEditModelAndView(p,null,curriculaId);
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/misc/403");
        }
        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam int curriculaId, @RequestParam int miscDataId){
        ModelAndView result;
        try{
            MiscData e = this.miscDataService.findOne(miscDataId);
            result = this.createEditModelAndView(e,null,curriculaId);
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/misc/403");
        }
        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("miscD") MiscData miscData, @RequestParam int curriculaId, BindingResult binding){
        ModelAndView result;
        try{
            miscData = this.miscDataService.reconstruct(miscData,binding);
            miscData = this.miscDataService.save(miscData,curriculaId);
            result = new ModelAndView("redirect:/curricula/hacker/list.do");
        }catch(final ValidationException ex){
            result = this.createEditModelAndView(miscData,null,curriculaId);
            for (final ObjectError e : binding.getAllErrors())
                if (e.getDefaultMessage().equals("URL incorrecta") || e.getDefaultMessage().equals("Invalid URL"))
                    result.addObject("attachmentError", e.getDefaultMessage());
        }catch(Throwable oops){
            result = this.createEditModelAndView(miscData,"curricula.commit.error",curriculaId);
        }
        return result;
    }

    private ModelAndView createEditModelAndView(MiscData p, String messageCode,int curriculaId){
        ModelAndView result;

        result = new ModelAndView("miscData/hacker/edit");
        result.addObject("miscD", p);
        result.addObject("message", messageCode);
        result.addObject("curriculaId",curriculaId);

        return result;

    }
}
