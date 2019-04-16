package controllers.curricula;

import domain.PersonalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.HackerService;
import services.PersonalDataService;

@Controller
@RequestMapping("/personalData/hacker")
public class PersonalDataController {

    @Autowired
    private PersonalDataService personalDataService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private HackerService hackerService;

    public ModelAndView create(){
        return null;
    }
}
