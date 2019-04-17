
package services;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

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
import domain.Position;
import domain.Problem;

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
	private ProblemService			problemService;

	@Autowired
	private PositionService			positionService;

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
			application.setRejectComment(result.getRejectComment());
			this.validator.validate(application, binding);
			result = application;
		}
		return result;
	}

	public Application reconstructReject(final Application application, final BindingResult binding) {
		Application result;
			result = this.applicationRepository.findOne(application.getId());
			application.setStatus(result.getStatus());
			application.setSubmitMoment(result.getSubmitMoment());
			application.setVersion(result.getVersion());
			application.setCurricula(result.getCurricula());
			application.setExplanation(result.getExplanation());
			application.setLink(result.getLink());
			application.setProblem(result.getProblem());
			application.setHacker(result.getHacker());
			application.setMoment(result.getMoment());
			application.setRejectComment(application.getRejectComment());
			this.validator.validate(application, binding);
			result = application;
		return result;
	}

	public Application create(final int positionId) {
		Assert.isTrue(this.actorService.getActorLogged().getUserAccount().getAuthorities().iterator().next().getAuthority().equals("HACKER"));
		Application application;

		//Tenemos que coger la lista de problemas de la posición a la que se le
		//va a hacer la application
		final List<Problem> problems;
		//Cogemos la position usando el findOne y usando el id como parametro
		final Position position = this.positionService.findOne(positionId);
		Problem problem;

		application = new Application();
		application.setMoment(new Date());
		application.setStatus("PENDING");
		final Actor user = this.actorService.findByUsername(LoginService.getPrincipal().getUsername());
		final Hacker hacker = this.hackerService.findOne(user.getId());
		application.setHacker(hacker);

		//Cogemos los problemas que son finales y que esten relacionados con la
		//position que cogemos antes, que esta luego esta relacionada con la
		//company que tiene los problemas
		problems = this.problemService.getProblemsFinalByCompany(position.getCompany());

		//Generamos un int random que vaya de 0 a el tamaño de los problemas -1
		//random.nextInt genera un int random desde 0 a el valor como parametro -1
		Random random = new Random();
		int valorRandom = random.nextInt(problems.size());

		//Cogemos un problema random de la lista de problemas
		problem = problems.get(valorRandom);
		//Le hacemos el set a la application
		application.setProblem(problem);

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
		Assert.isTrue(this.actorService.getActorLogged().getUserAccount().getAuthorities().iterator().next().getAuthority().equals("HACKER")
		|| this.actorService.getActorLogged().getUserAccount().getAuthorities().iterator().next().getAuthority().equals("COMPANY"));

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
