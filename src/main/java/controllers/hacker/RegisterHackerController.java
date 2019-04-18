
package controllers.hacker;

import javax.validation.Valid;

import org.hibernate.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.HackerService;
import controllers.AbstractController;
import datatype.CreditCard;
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

				final CreditCard c = new CreditCard();
				c.setBrandName(hackerForm.getBrandName());
				c.setCvv(hackerForm.getCvvCode());
				c.setExpirationYear(hackerForm.getExpiration());
				c.setHolder(hackerForm.getHolderName());
				c.setNumber(hackerForm.getNumber());
				hacker = this.hackerService.reconstruct(hackerForm, binding);
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

	@RequestMapping(value="/company/show", method = RequestMethod.GET)
	public ModelAndView showCompany(@RequestParam int hackerId){
		ModelAndView result;

		try{
			Assert.notNull(hackerId);
			Hacker hacker = hackerService.findOne(hackerId);
			result = new ModelAndView("hacker/company/show");
			result.addObject("hacker", hacker);
		}catch (Throwable oops){
			result = new ModelAndView("redirect/misc/403");
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
