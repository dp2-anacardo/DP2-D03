
package services;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ApplicationRepository;
import security.LoginService;
import security.UserAccount;

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
	private CurriculaService curriculaService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private Validator				validator;


	public Application reconstruct(final Application application, final BindingResult binding) {
		Application result;

		result = this.applicationRepository.findOne(application.getId());
		result.setExplanation(application.getExplanation());
		result.setLink(application.getLink());
		validator.validate(result,binding);
		if(binding.hasErrors())
			throw new ValidationException();

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

	public Application create() {
		Assert.isTrue(this.actorService.getActorLogged().getUserAccount().getAuthorities().iterator().next().getAuthority().equals("HACKER"));
		Application application;

		application = new Application();

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
	public Application saveCompany(Application application){
		Assert.notNull(application);
		Assert.isTrue(this.actorService.getActorLogged().getUserAccount().getAuthorities().iterator().next().getAuthority().equals("COMPANY"));

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

		//TODO Probar cuando se puedan aceptar las applications
		Message msg = this.messageService.create();
		msg.setRecipient(application.getHacker());
		msg.setSubject("An application has changed its status.");
		msg.setBody("The application for the position has changed its status to ACCEPTED");
		msg.getTags().add("NOTIFICATION");
 		this.messageService.save(msg);



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

		//TODO Probar cuando se puedan rechazar las applications
		Message msg = this.messageService.create();
		msg.setRecipient(application.getHacker());
		msg.setSubject("An application has changed its status.");
		msg.setBody("The application for the position has changed its status to REJECTED");
		msg.getTags().add("NOTIFICATION");
		this.messageService.save(msg);
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

	public Collection<Application> getApplicationsByPosition(int positionId){
		Assert.notNull(positionId);
		Collection<Application> applications;

		applications = applicationRepository.getApplicationsByPosition(positionId);

		return applications;
	}

	public Position getPositionByApplication(int applicationId){
		Assert.notNull(applicationId);

		Position position = this.applicationRepository.getPositionByApplication(applicationId);

		return position;
	}

	//PARTE DEL HACKER--------------------------------------------------------------------------------------------------

	public Application saveHacker(Application application, int positionId) {
		Application result;
		Assert.notNull(application);
		Assert.isTrue(application.getId()==0);
		Assert.notNull(positionId);
		Position p = this.positionService.findOne(positionId);
		Assert.notNull(p);
		Assert.isTrue(p.getIsFinal()==true && p.getIsCancelled() == false);


		Actor a = this.actorService.getActorLogged();
		Hacker h = this.hackerService.findOne(a.getId());
		Assert.notNull(h);

		application.setHacker(h);
		application.setMoment(new Date());
		application.setStatus("PENDING");
		application.setCurricula(this.curriculaService.copy(application.getCurricula()));
		List<Problem> problems = (List<Problem>) p.getProblems();
		Random random = new Random();
		int valorRandom = random.nextInt(problems.size());
		application.setProblem(problems.get(valorRandom));

		result = this.applicationRepository.save(application);

		p.getApplications().add(result);

		return result;

	}

	public Application saveHackerUpdate(final Application application) {
		Assert.notNull(application);
		Assert.isTrue(this.actorService.getActorLogged().getUserAccount().getAuthorities().iterator().next().getAuthority().equals("HACKER"));
		Assert.isTrue(application.getStatus().equals("PENDING"));
		Assert.isTrue(!application.getExplanation().equals(""));
		Assert.isTrue(!application.getLink().equals(""));

		Application result = application;
		result.setSubmitMoment(new Date());
		result.setStatus("SUBMITTED");

		result = this.applicationRepository.save(application);

		return result;
	}
}
