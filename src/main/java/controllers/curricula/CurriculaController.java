
package controllers.curricula;

import java.util.Collection;

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
import services.CompanyService;
import services.CurriculaService;
import services.HackerService;
import services.PersonalDataService;
import controllers.AbstractController;
import domain.Actor;
import domain.Company;
import domain.Curricula;
import domain.Hacker;
import domain.PersonalData;

@Controller
@RequestMapping("/curricula")
public class CurriculaController extends AbstractController {

	@Autowired
	private CurriculaService	curriculaService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private HackerService		hackerService;

	@Autowired
	private PersonalDataService	personalDataService;

	@Autowired
	private CompanyService		companyService;


	@RequestMapping(value = "/hacker/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		try {
			final Actor a = this.actorService.getActorLogged();
			final Hacker h = this.hackerService.findOne(a.getId());
			Assert.notNull(h);
			final Collection<Curricula> curriculas = this.curriculaService.getCurriculaByHacker();
			result = new ModelAndView("curricula/hacker/list");
			result.addObject("curriculas", curriculas);
			result.addObject("RequestURI", "curricula/hacker/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	@RequestMapping(value = "/hacker/display", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int curriculaId) {
		ModelAndView result;
		try {
			final Curricula c = this.curriculaService.findOne(curriculaId);
			Assert.notNull(c);
			Assert.isTrue(c.getIsCopy() == false);

			final Actor a = this.actorService.getActorLogged();
			final Hacker h = this.hackerService.findOne(a.getId());
			Assert.notNull(h);
			Assert.isTrue(h.getCurricula().contains(c));
			result = new ModelAndView("curricula/hacker/display");
			result.addObject("curricula", c);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	@RequestMapping(value = "/company/display", method = RequestMethod.GET)
	public ModelAndView showCompany(@RequestParam final int curriculaId) {
		ModelAndView result;
		try {
			final Curricula c = this.curriculaService.findOne(curriculaId);
			Assert.notNull(c);

			final Actor a = this.actorService.getActorLogged();
			final Company company = this.companyService.findOne(a.getId());
			Assert.notNull(company);
			result = new ModelAndView("curricula/company/display");
			result.addObject("curricula", c);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	@RequestMapping(value = "/hacker/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		try {
			final PersonalData p = this.personalDataService.create();
			result = this.createEditModelAndView(p, null);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}

		return result;
	}

	@RequestMapping(value = "/hacker/create", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("personalD") PersonalData p, final BindingResult binding) {
		ModelAndView result;
		try {
			p = this.personalDataService.reconstruct(p, binding);
			p = this.personalDataService.save(p);
			Curricula c = this.curriculaService.create();
			c.setPersonalData(p);
			c = this.curriculaService.save(c);
			result = new ModelAndView("redirect:list.do");
		} catch (final ValidationException e) {
			result = this.createEditModelAndView(p, null);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(p, "curricula.commit.error");
		}
		return result;
	}

	@RequestMapping(value = "/hacker/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int curriculaId) {
		ModelAndView result;
		try {
			final Curricula c = this.curriculaService.findOne(curriculaId);
			this.curriculaService.delete(c);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/");
		}
		return result;
	}

	private ModelAndView createEditModelAndView(final PersonalData p, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("curricula/hacker/create");
		result.addObject("personalD", p);
		result.addObject("messageCode", messageCode);

		return result;

	}
}
