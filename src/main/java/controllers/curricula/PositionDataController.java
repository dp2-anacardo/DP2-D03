
package controllers.curricula;

import javax.validation.ValidationException;

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
import controllers.AbstractController;
import domain.PositionData;

@Controller
@RequestMapping("/positionData/hacker")
public class PositionDataController extends AbstractController {

	@Autowired
	private PositionDataService	positionDataService;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private HackerService		hackerService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int curriculaId) {
		ModelAndView result;
		try {
			final PositionData p = this.positionDataService.create();
			result = this.createEditModelAndView(p, null, curriculaId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int curriculaId, @RequestParam final int positionDataId) {
		ModelAndView result;
		try {
			final PositionData e = this.positionDataService.findOne(positionDataId);
			result = this.createEditModelAndView(e, null, curriculaId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("positionD") PositionData positionData, @RequestParam final int curriculaId, final BindingResult binding) {
		ModelAndView result;
		try {
			positionData = this.positionDataService.reconstruct(positionData, binding);
			positionData = this.positionDataService.save(positionData, curriculaId);
			result = new ModelAndView("redirect:/curricula/hacker/list.do");
		} catch (final ValidationException e) {
			result = this.createEditModelAndView(positionData, null, curriculaId);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(positionData, "curricula.commit.error", curriculaId);
		}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int positionDataId) {
		ModelAndView result;
		try {
			final PositionData e = this.positionDataService.findOne(positionDataId);
			this.positionDataService.delete(e);
			result = new ModelAndView("redirect:/curricula/hacker/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	private ModelAndView createEditModelAndView(final PositionData p, final String messageCode, final int curriculaId) {
		ModelAndView result;

		result = new ModelAndView("positionData/hacker/edit");
		result.addObject("positionD", p);
		result.addObject("message", messageCode);
		result.addObject("curriculaId", curriculaId);

		return result;

	}

}
