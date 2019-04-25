
package controllers.curricula;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import controllers.AbstractController;
import domain.Actor;
import domain.Curricula;
import domain.Hacker;
import domain.PersonalData;

@Controller
@RequestMapping("/personalData/hacker")
public class PersonalDataController extends AbstractController {

	@Autowired
	private PersonalDataService	personalDataService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private HackerService		hackerService;


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int personalDataId) {
		ModelAndView result;
		try {
			final PersonalData p = this.personalDataService.findOne(personalDataId);
			final Actor a = this.actorService.getActorLogged();
			final Hacker h = this.hackerService.findOne(a.getId());
			boolean canEdit = false;
			for (final Curricula c : h.getCurricula())
				if (c.getPersonalData().equals(p))
					canEdit = true;
			Assert.isTrue(canEdit);
			result = this.createEditModelAndView(p, null);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("personalD") PersonalData p, final BindingResult binding) {
		ModelAndView result;
		try {
			p = this.personalDataService.reconstruct(p, binding);
			p = this.personalDataService.save(p);
			result = new ModelAndView("redirect:/curricula/hacker/list.do");
		} catch (final ValidationException e) {
			result = this.createEditModelAndView(p, null);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(p, "curricula.commit.error");
		}
		return result;
	}

	private ModelAndView createEditModelAndView(final PersonalData p, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("personalData/hacker/edit");
		result.addObject("personalD", p);
		result.addObject("message", messageCode);

		return result;

	}
}
