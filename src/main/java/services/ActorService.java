
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;

@Service
@Transactional
public class ActorService {

	//Managed Repositories
	@Autowired
	private ActorRepository			actorRepository;

	//Supporting services
	@Autowired
	private UserAccountService		userAccountService;
	@Autowired
	private SocialProfileService	socialProfileService;
	@Autowired
	private MessageService			messageService;
	@Autowired
	private AdministratorService 	administratorService;
	@Autowired
	private HackerService 			hackerService;
	@Autowired
	private CurriculaService		curriculaService;
	@Autowired
	private ApplicationService		applicationService;
	@Autowired
	private FinderService			finderService;
	@Autowired
	private CompanyService			companyService;
	@Autowired
	private ProblemService			problemService;
	@Autowired
	private PositionService			positionService;


	public Collection<Actor> findAll() {
		Collection<Actor> result;

		result = this.actorRepository.findAll();

		return result;
	}

	public Actor findOne(final int actorId) {
		Assert.isTrue(actorId != 0);

		Actor result;

		result = this.actorRepository.findOne(actorId);

		return result;
	}

	public void delete(final Actor actor) {
		Assert.notNull(actor);
		Assert.isTrue(actor.getId() != 0);
		Assert.isTrue(this.actorRepository.exists(actor.getId()));

		this.actorRepository.delete(actor);
	}

	public UserAccount findUserAccount(final Actor actor) {
		Assert.notNull(actor);

		UserAccount result;

		result = this.userAccountService.findByActor(actor);

		return result;
	}

	public Actor findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Actor result;

		result = this.actorRepository.findByUserAccountId(userAccount.getId());

		return result;
	}

	public Actor getActorLogged() {
		UserAccount userAccount;
		Actor actor;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		actor = this.findByUserAccount(userAccount);
		Assert.notNull(actor);

		return actor;
	}

	public Actor save(final Actor actor) {
		Assert.notNull(actor);

		Actor result;

		result = this.actorRepository.save(actor);

		return result;
	}

	public Actor findByUsername(final String username) {
		Assert.notNull(username);

		Actor result;
		result = this.actorRepository.findByUsername(username);
		return result;
	}

	public Collection<Actor> findSuspiciousActors() {
		Collection<Actor> result;
		result = this.actorRepository.findSuspiciousActors();
		return result;
	}

	public Collection<Actor> findBannedActors() {
		Collection<Actor> result;
		result = this.actorRepository.findBannedActors();
		return result;
	}

	public Boolean existUsername(final String username) {
		Boolean res = false;
		final List<String> lista = new ArrayList<String>();
		for (final Actor a : this.actorRepository.findAll())
			lista.add(a.getUserAccount().getUsername());
		if (!(lista.contains(username)))
			res = true;
		return res;
	}

	public Boolean existIdSocialProfile(final Integer id) {
		Boolean res = false;
		final List<Integer> lista = new ArrayList<Integer>();
		for (final SocialProfile s : this.socialProfileService.findAll())
			lista.add(s.getId());
		if (lista.contains(id))
			res = true;
		return res;
	}

	public void deleteInformation() {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		final Actor user = this.findByUserAccount(userAccount);

		//Borrado de los socialProfiles de los actores
		if (!(user.getSocialProfiles().isEmpty())) {
			final List<SocialProfile> a = new ArrayList<>();
			final Collection<SocialProfile> ad = user.getSocialProfiles();
			a.addAll(ad);
			for (final SocialProfile i : a)
				this.socialProfileService.delete(i);
		}

		//Borrado de los mensajes que recibes y envias
		final Collection<Message> msgs = this.messageService.findAllSentByActor(user.getId());
		msgs.addAll(this.messageService.findAllReceivedByActor(user.getId()));

		if (msgs.size() > 0)
			for (final Message m : msgs)
				this.messageService.deleteForced(m);

		//Borrado si admin
		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN")) {

			final Administrator admin = this.administratorService.findOne(user.getId());

			//Borrado de la informacion del administrador
			this.administratorService.delete(admin);
		}

		//Borrado si hacker
		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("HACKER")) {

			//Faltan las relaciones de hacker
			final Hacker h = this.hackerService.findOne(user.getId());

			//Borrado de las applications del hacker
			final Collection<Application> applications = this.applicationService.getApplicationsByHacker(h);
			final Collection<Position> allPositions = this.positionService.findAll();
			this.deleteApplications(applications, allPositions);

			//Borrado de todos las curriculas de hacker
			final Collection<Curricula> curriculas = h.getCurricula();
			for(final Curricula c : curriculas)
				this.curriculaService.delete(c);

			//Borrado del hacker
			this.hackerService.delete(h);

			//Borrado del finder
			Finder finder = this.finderService.findOne(h.getFinder().getId());
			finder.setPositions(new ArrayList<Position>());
			this.finderService.delete(finder);

		}

		//Borrado si company
		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("COMPANY")) {

			final Company company = this.companyService.findOne(user.getId());

			//Borrado de los Applications de company
			final Collection<Application> applications = this.applicationService.getApplicationsByCompany(company);
			final Collection<Position> allPositions = this.positionService.findAll();
			this.deleteApplications(applications, allPositions);

			//Borrado de los problems de company
			final Collection<Problem> problems = this.problemService.findAllByCompany(company.getId());
			final Collection<Application> allApplications = this.applicationService.findAll();
			for (Problem p : problems) {
				for (Application a : allApplications) {
					if (a.getProblem().equals(p)) this.applicationService.delete(a);
				}
				this.problemService.deleteForced(p);
			}


			//Borrado de las positions
			final Collection<Position> positions = this.positionService.getPositionsByCompanyAll(company);
			for (Position p : positions)
				this.positionService.deleteForced(p);

			this.companyService.delete(company);
		}
	}

	private void deleteApplications(Collection<Application> applications, Collection<Position> positions){
		for(Application a : applications) {
			for (Position p : positions) {
				if (p.getApplications().contains(a)) p.getApplications().remove(a);
			}
			this.applicationService.delete(a);
		}
	}
}
