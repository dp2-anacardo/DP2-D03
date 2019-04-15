
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.SocialProfile;

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

}
