
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import domain.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ApplicationRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Application;
import domain.Company;
import domain.Hacker;

@Service
@Transactional
public class ApplicationService {

	@Autowired
	private ApplicationRepository	applicationRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private HackerService			hackerService;

	@Autowired
	private CompanyService			companyService;

	@Autowired
	private Validator				validator;


	public Application reconstruct(final Application application, final BindingResult binding) {
		Application result;
		if (application.getId() == 0) {
			result = application;
			this.validator.validate(application, binding);
		} else {
			result = this.applicationRepository.findOne(application.getId());
			application.setStatus("SUBMITTED");
			application.setSubmitMoment(new Date());
			application.setVersion(result.getVersion());
			application.setCurricula(result.getCurricula());
			application.setExplanation(result.getExplanation());
			application.setLink(result.getLink());
			application.setProblem(result.getProblem());
			application.setHacker(result.getHacker());
			application.setMoment(result.getMoment());
			this.validator.validate(application, binding);
			result = application;
		}
		return result;
	}

	public Application create() {
		Assert.isTrue(this.actorService.getActorLogged().getUserAccount().getAuthorities().iterator().next().getAuthority().equals("HACKER"));
		Application application;

		application = new Application();
		application.setMoment(new Date());
		application.setStatus("PENDING");
		final Actor user = this.actorService.findByUsername(LoginService.getPrincipal().getUsername());
		final Hacker hacker = this.hackerService.findOne(user.getId());
		application.setHacker(hacker);
		//Aqui hay que ponerle un problem aleatorio para la position
		//a la que se le crea esta Application
		//application.setProblem();

		return application;
	}

	public Application findOne(final int applicationId) {
		Assert.notNull(applicationId);

		final Application application = this.applicationRepository.findOne(applicationId);
		Assert.notNull(application);

		return application;
	}

	public Collection<Application> findAll() {
		Collection<Application> applications;

		applications = this.applicationRepository.findAll();
		Assert.notNull(applications);

		return applications;
	}

	public Application save(final Application application) {
		Assert.notNull(application);
		Assert.isTrue(this.actorService.getActorLogged().getUserAccount().getAuthorities().iterator().next().getAuthority().equals("HACKER"));

		Application result;

		result = this.applicationRepository.save(application);

		return result;
	}

	public void acceptApplication(final Application application) {
		Assert.notNull(application);
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final Company company = this.companyService.findOne(this.actorService.getActorLogged().getId());
		final Collection<Application> applications = this.getApplicationsByCompany(company);

		Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("COMPANY"));
		Assert.isTrue(application.getStatus().equals("SUBMITTED"));
		Assert.isTrue(applications.contains(application));
		application.setStatus("ACCEPTED");
	}

	public void rejectApplication(final Application application) {
		Assert.notNull(application);
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final Company company = this.companyService.findOne(this.actorService.getActorLogged().getId());
		final Collection<Application> applications = this.getApplicationsByCompany(company);

		Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("COMPANY"));
		Assert.isTrue(application.getStatus().equals("SUBMITTED"));
		Assert.isTrue(applications.contains(application));
		Assert.isTrue(!application.getRejectComment().equals(""));
		application.setStatus("REJECTED");
	}

	public Collection<Application> getApplicationsByHacker(final Hacker hacker) {
		Assert.notNull(hacker);
		Collection<Application> applications;

		applications = this.applicationRepository.getApplicationsByHacker(hacker.getId());

		return applications;
	}

	public Collection<Application> getApplicationsByCompany(final Company company) {
		Assert.notNull(company);
		Collection<Application> applications;

		applications = this.applicationRepository.getApplicationsByCompany(company.getId());

		return applications;
	}

}
