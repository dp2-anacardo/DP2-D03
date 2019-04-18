package controllers.curricula;

import controllers.AbstractController;
import domain.EducationalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.EducationalDataService;
import services.HackerService;

import javax.validation.ValidationException;

@Controller
@RequestMapping("/educationalData/hacker")
public class EducationalDataController extends AbstractController {
    @Autowired
    private EducationalDataService educationalDataService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private HackerService hackerService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam int curriculaId){
        ModelAndView result;
        try{
            EducationalData e = this.educationalDataService.create();
            result = this.createEditModelAndView(e,null,curriculaId);
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/misc/403");
        }
        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam int curriculaId, @RequestParam int educationalDataId){
        ModelAndView result;
        try{
            EducationalData e = this.educationalDataService.findOne(educationalDataId);
            result = this.createEditModelAndView(e,null,curriculaId);
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/misc/403");
        }
        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute("educationalD") EducationalData educationalData, @RequestParam int curriculaId, BindingResult binding){
        ModelAndView result;
        try{
            educationalData = this.educationalDataService.reconstruct(educationalData,binding);
            educationalData = this.educationalDataService.save(educationalData,curriculaId);
            result = new ModelAndView("redirect:/curricula/hacker/list.do");
        }catch(final ValidationException e){
            result = this.createEditModelAndView(educationalData,null,curriculaId);
        }catch(Throwable oops){
            result = this.createEditModelAndView(educationalData,"curricula.commit.error",curriculaId);
        }
        return result;
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam int educationalDataId){
        ModelAndView result;
        try{
            EducationalData e = this.educationalDataService.findOne(educationalDataId);
            this.educationalDataService.delete(e);
            result = new ModelAndView("redirect:/curricula/hacker/list.do");
        }catch(Throwable oops){
            result = new ModelAndView("redirect:/misc/403");
        }
        return result;
    }

    private ModelAndView createEditModelAndView(EducationalData p, String messageCode,int curriculaId){
        ModelAndView result;

        result = new ModelAndView("educationalData/hacker/edit");
        result.addObject("educationalD", p);
        result.addObject("message", messageCode);
        result.addObject("curriculaId",curriculaId);

        return result;

    }
}
