package controllers.curricula;

import controllers.AbstractController;
import domain.EducationalData;
import domain.PositionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.HackerService;
import services.PositionDataService;

import javax.validation.ValidationException;

@Controller
@RequestMapping("/positionData/hacker")
public class PositionDataController extends AbstractController {
    @Autowired
    private PositionDataService positionDataService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private HackerService hackerService;


    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam int curriculaId){
        ModelAndView result;
        try{
            PositionData p = this.positionDataService.create();
            result = this.createEditModelAndView(p,null,curriculaId);
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/misc/403");
        }
        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam int curriculaId, @RequestParam int positionDataId){
        ModelAndView result;
        try{
            PositionData e = this.positionDataService.findOne(positionDataId);
            result = this.createEditModelAndView(e,null,curriculaId);
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/misc/403");
        }
        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("positionD") PositionData positionData, @RequestParam int curriculaId, BindingResult binding){
        ModelAndView result;
        try{
            positionData = this.positionDataService.reconstruct(positionData,binding);
            positionData = this.positionDataService.save(positionData,curriculaId);
            result = new ModelAndView("redirect:/curricula/hacker/list.do");
        }catch(final ValidationException e){
            result = this.createEditModelAndView(positionData,null,curriculaId);
        }catch(Throwable oops){
            result = this.createEditModelAndView(positionData,"curricula.commit.error",curriculaId);
        }
        return result;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam int positionDataId){
        ModelAndView result;
        try{
            PositionData e = this.positionDataService.findOne(positionDataId);
            this.positionDataService.delete(e);
            result = new ModelAndView("redirect:/curricula/hacker/list.do");
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/misc/403");
        }
        return result;
    }

    private ModelAndView createEditModelAndView(PositionData p, String messageCode,int curriculaId){
        ModelAndView result;

        result = new ModelAndView("positionData/hacker/edit");
        result.addObject("positionD", p);
        result.addObject("message", messageCode);
        result.addObject("curriculaId",curriculaId);

        return result;

    }

}
