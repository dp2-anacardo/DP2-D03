
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.HackerRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Curricula;
import domain.Hacker;
import domain.Message;
import domain.SocialProfile;
import forms.HackerForm;

@Service
@Transactional
public class HackerService {

	//Managed Repositories
	@Autowired
	private ActorService			actorService;
	//Supporting services
	@Autowired
	private ConfigurationService	configurationService;
	@Autowired
	private HackerRepository		hackerRepository;
	@Autowired
	private Validator				validator;


	public Hacker create() {

		final Authority auth;
		final UserAccount userAccount;
		final Collection<Authority> authorities;
		final Collection<SocialProfile> profiles;
		final Collection<Message> sent;
		final Collection<Message> received;
		final Collection<Curricula> curriculas;
		final Hacker a = new Hacker();
		userAccount = new UserAccount();
		auth = new Authority();
		authorities = new ArrayList<Authority>();
		profiles = new ArrayList<SocialProfile>();
		sent = new ArrayList<Message>();
		received = new ArrayList<Message>();
		curriculas = new ArrayList<Curricula>();

		auth.setAuthority(Authority.HACKER);
		authorities.add(auth);
		userAccount.setAuthorities(authorities);
		a.setUserAccount(userAccount);
		a.setIsBanned(false);
		a.setIsSpammer(false);
		a.setMessagesR(received);
		a.setMessagesS(sent);
		a.setSocialProfiles(profiles);
		a.setCurricula(curriculas);
		return a;
	}

	public Collection<Hacker> findAll() {
		Collection<Hacker> result;

		result = this.hackerRepository.findAll();

		return result;
	}

	public Hacker findOne(final int hackerId) {
		Assert.isTrue(hackerId != 0);

		Hacker result;

		result = this.hackerRepository.findOne(hackerId);

		return result;
	}

	public Hacker save(final Hacker hacker) {

		Assert.notNull(hacker);
		Hacker result;
		final char[] c = hacker.getPhoneNumber().toCharArray();
		if ((!hacker.getPhoneNumber().equals(null) && !hacker.getPhoneNumber().equals("")))
			if (c[0] != '+') {
				final String i = this.configurationService.findAll().get(0).getCountryCode();
				hacker.setPhoneNumber("+" + i + " " + hacker.getPhoneNumber());
			}
		if (hacker.getId() == 0) {
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String res = encoder.encodePassword(hacker.getUserAccount().getPassword(), null);
			hacker.getUserAccount().setPassword(res);
		}
		result = this.hackerRepository.save(hacker);
		return result;
	}

	public void delete(final Hacker hacker) {

		final Actor actor = this.actorService.getActorLogged();
		Assert.isTrue(actor.getUserAccount().getAuthorities().iterator().next().getAuthority().equals("HACKER"));
		Assert.notNull(hacker);

		this.hackerRepository.delete(hacker);
	}

	public Hacker reconstruct(final Hacker hacker, final BindingResult binding) {

		Hacker result;
		if (hacker.getId() == 0) {
			this.validator.validate(hacker, binding);
			result = hacker;
		} else {
			result = this.hackerRepository.findOne(hacker.getId());

			result.setName(hacker.getName());
			result.setPhoto(hacker.getPhoto());
			result.setPhoneNumber(hacker.getPhoneNumber());
			result.setEmail(hacker.getEmail());
			result.setAddress(hacker.getAddress());
			result.setVatNumber(hacker.getVatNumber());
			result.setSurname(hacker.getSurname());
			this.validator.validate(hacker, binding);
		}
		return result;
	}

	public Hacker reconstruct(final HackerForm a, final BindingResult binding) {

		final Hacker result = this.create();
		result.setAddress(a.getAddress());
		result.setEmail(a.getEmail());
		result.setId(a.getId());
		result.setName(a.getName());
		result.setPhoneNumber(a.getPhoneNumber());
		result.setPhoto(a.getPhoto());
		result.setSurname(a.getSurname());
		result.getUserAccount().setPassword(a.getPassword());
		result.getUserAccount().setUsername(a.getUsername());
		result.setVersion(a.getVersion());
		result.setVatNumber(a.getVatNumber());

		this.validator.validate(result, binding);
		return result;
	}

}
