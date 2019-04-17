
package controllers.hacker;

import javax.validation.Valid;

import org.hibernate.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.FinderService;
import services.HackerService;
import controllers.AbstractController;
import datatype.CreditCard;
import domain.Finder;
import domain.Hacker;
import forms.HackerForm;

@Controller
@RequestMapping("/hacker")
public class RegisterHackerController extends AbstractController {

	@Autowired
	private HackerService			hackerService;

	@Autowired
	private ActorService			actorService;
	@Autowired
	private AdministratorService	administratorService;
	@Autowired
	private FinderService			finderService;


	@ExceptionHandler(TypeMismatchException.class)
	public ModelAndView handleMismatchException(final TypeMismatchException oops) {
		return new ModelAndView("redirect:/misc/403");
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		HackerForm hackerForm;
		hackerForm = new HackerForm();
		result = this.createEditModelAndView(hackerForm);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView save(@Valid final HackerForm hackerForm, final BindingResult binding) {
		ModelAndView result;
		Hacker hacker;

		if (this.actorService.existUsername(hackerForm.getUsername()) == false) {
			binding.rejectValue("username", "error.username");
			result = this.createEditModelAndView(hackerForm);
		} else if (this.administratorService.checkPass(hackerForm.getPassword(), hackerForm.getConfirmPass()) == false) {
			binding.rejectValue("password", "error.password");
			result = this.createEditModelAndView(hackerForm);
		} else if (binding.hasErrors())
			result = this.createEditModelAndView(hackerForm);
		else
			try {
				Finder finderCreate;
				Finder finder;
				finderCreate = this.finderService.create();
				finder = this.finderService.save(finderCreate);
				final CreditCard c = new CreditCard();
				c.setBrandName(hackerForm.getBrandName());
				c.setCvv(hackerForm.getCvvCode());
				c.setExpirationYear(hackerForm.getExpiration());
				c.setHolder(hackerForm.getHolderName());
				c.setNumber(hackerForm.getNumber());
				hacker = this.hackerService.reconstruct(hackerForm, binding);
				hacker.setFinder(finder);
				hacker.setCreditCard(c);
				this.hackerService.save(hacker);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				if (binding.hasErrors())
					result = this.createEditModelAndView(hackerForm, "error.duplicated");
				result = this.createEditModelAndView(hackerForm, "error.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final HackerForm hackerForm) {
		ModelAndView result;
		result = this.createEditModelAndView(hackerForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final HackerForm hackerForm, final String messageCode) {

		final ModelAndView result;

		result = new ModelAndView("hacker/create");
		result.addObject("hackerForm", hackerForm);
		result.addObject("message", messageCode);

		return result;
	}
}
