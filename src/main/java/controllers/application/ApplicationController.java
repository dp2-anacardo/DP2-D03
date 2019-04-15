
package controllers.application;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.ApplicationService;
import controllers.AbstractController;
import domain.Actor;
import domain.Application;
import domain.Hacker;

@Controller
@RequestMapping("application")
public class ApplicationController extends AbstractController {

	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private HackerService		hackerService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private PositionService		positionService;

	@Autowired
	private CompanyService		companyService;

	@Autowired
	private PositionService		positionService;


	@RequestMapping(value = "/hacker/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Application> applications;

		final Actor user = this.actorService.findByUsername(LoginService.getPrincipal().getUsername());
		final Hacker hacker = this.hackerService.findOne(user.getId());
		applications = this.applicationService.getApplicationsByHacker(hacker);

		result = new ModelAndView("application/hacker/list");
		result.addObject("applications", applications);
		result.addObject("requestURI", "application/hacker/list.do");
		return result;
	}

	@RequestMapping(value = "/company/list", method = RequestMethod.GET)
	public ModelAndView listCompany() {
		ModelAndView result;
		Collection<Application> applications;
		Collection<Position> positions;

		final Actor user = this.actorService.findByUsername(LoginService.getPrincipal().getUsername());
		final Company company = this.companyService.findOne(user.getId());

		positions = this.positionService.findPositionsByCompany(company.getId());
		for (final Position position : positions)
			applications = applications.addAll(position.getApplications());

		result = new ModelAndView("application/company/list");
		result.addObject("applications", applications);
		result.addObject("requestURI", "application/company/list.do");

		return result;
	}

	@RequestMapping(value = "/hacker/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int positionId) {
		ModelAndView result;
		Application application;
		Position position;

		try {
			Assert.notNull(positionId);
			position = this.positionService.findOne(positionId);
			final Collection<Application> applications = position.getApplications();
			applications.add(application);
			application = this.applicationService.create();
			result = this.createModelAndView(application);
			return result;
		} catch (final Exception e) {
			result = new ModelAndView("redirect:/position/hacker/list.do");
			return result;
		}
	}

	@RequestMapping(value = "/hacker/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Application application, final BindingResult binding) {
		final ModelAndView result;
		application = this.applicationService.reconstruct(application, binding);

		if (binding.hasErrors())
			result = this.createModelAndView(application);
		else
			try {
				this.applicationService.save(application);
				result = new ModelAndView("redirect:/application/hacker/list.do");
			} catch (final Exception e) {
				result = this.createModelAndView(application, "application.commit.error");
			}
	}

	@RequestMapping(value = "/hacker/update", method = RequestMethod.GET)
	public ModelAndView update(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application;

		try {
			Assert.notNull(applicationId);
			application = this.applicationService.findOne(applicationId);
			result = this.updateModelAndView(application);
			return result;
		} catch (final Exception e) {
			result = new ModelAndView("redirect:/application/hacker/list.do");
			return result;
		}
	}

	@RequestMapping(value = "/hacker/update", method = RequestMethod.POST, params = "update")
	public ModelAndView update(Application application, final BindingResult binding) {
		ModelAndView result;
		application = this.applicationService.reconstruct(application, binding);

		if (binding.hasErrors())
			result = this.updateModelAndView(application);
		else
			try {
				this.applicationService.save(application);
				result = new ModelAndView("redirect:/application/hacker/list.do");
				return result;
			} catch (final Exception e) {
				result = this.updateModelAndView(application, "application.commit.error");
				return result;
			}
	}

	@RequestMapping(value = "/company/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application;

		try {
			Assert.notNull(applicationId);
			application = this.applicationService.findOne(applicationId);
			this.applicationService.acceptApplication(application);
			this.applicationService.save(application);
			result = new ModelAndView("redirect:/application/company/list.do");
			return result;
		} catch (final Exception e) {
			result = new ModelAndView("redirect:/application/company/list.do");
			return result;
		}
	}

	@RequestMapping(value = "/company/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int applicationId) {
		ModelAndView result;
		Application application;

		try {
			Assert.notNull(applicationId);
			application = this.applicationService.findOne(applicationId);
			result = this.rejectModelAndView(application);
			return result;
		} catch (final Exception e) {
			result = new ModelAndView("redirect:/application/company/list.do");
			return result;
		}
	}

	@RequestMapping(value = "/company/reject", method = RequestMethod.POST, params = "reject")
	public ModelAndView reject(Application application, final BindingResult binding) {
		ModelAndView result;
		application = this.applicationService.reconstruct(application, binding);

		if (application.getRejectComment().equals("")) {
			binding.rejectValue("rejectComment", "error.rejectComment");
			result = this.rejectModelAndView(application);
		} else if (binding.hasErrors()) {
			result = this.rejectModelAndView(application);
			return result;
		} else
			try {
				this.applicationService.rejectApplication(application);
				result = new ModelAndView("redirect:/application/company/list.do");
				return result;
			} catch (final Exception e) {
				result = this.rejectModelAndView(application, "application.commit.error");
				return result;
			}
	}

	protected ModelAndView rejectModelAndView(final Application application) {
		ModelAndView result;

		result = this.rejectModelAndView(application, null);

		return result;
	}

	protected ModelAndView rejectModelAndView(final Application application, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("application/company/reject");
		result.addObject("application", application);
		result.addObject("message", messageCode);

		return result;
	}

	protected ModelAndView updateModelAndView(final Application application) {
		ModelAndView result;

		result = this.updateModelAndView(application, null);

		return result;
	}

	protected ModelAndView updateModelAndView(final Application application, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("application/hacker/update");
		result.addObject("application", application);
		result.addObject("message", messageCode);

		return result;
	}

	protected ModelAndView createModelAndView(final Application application) {
		ModelAndView result;

		result = this.createModelAndView(application, null);

		return result;
	}

	protected ModelAndView createModelAndView(final Application application, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("application/hacker/create");
		result.addObject("application", application);
		result.addObject("message", messageCode);

		return result;
	}

}