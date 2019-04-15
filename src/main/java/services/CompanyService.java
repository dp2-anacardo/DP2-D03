
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

import repositories.CompanyRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Company;
import domain.Message;
import domain.SocialProfile;

@Service
@Transactional
public class CompanyService {

	//Managed Repositories
	@Autowired
	private ActorService			actorService;
	//Supporting services
	@Autowired
	private ConfigurationService	configurationService;
	@Autowired
	private CompanyRepository		companyRepository;
	@Autowired
	private Validator				validator;


	public Company create() {

		final Authority auth;
		final UserAccount userAccount;
		final Collection<Authority> authorities;
		final Collection<SocialProfile> profiles;
		final Collection<Message> boxes;
		final Collection<String> surnames;
		final Company a = new Company();
		userAccount = new UserAccount();
		auth = new Authority();
		authorities = new ArrayList<Authority>();
		profiles = new ArrayList<SocialProfile>();
		boxes = new ArrayList<Message>();
		surnames = new ArrayList<String>();

		auth.setAuthority(Authority.COMPANY);
		authorities.add(auth);
		userAccount.setAuthorities(authorities);
		a.setUserAccount(userAccount);
		a.setIsBanned(false);
		a.setIsSpammer(false);
		a.setMessages(boxes);
		a.setSocialProfiles(profiles);
		a.setSurname(surnames);

		return a;
	}

	public Collection<Company> findAll() {
		Collection<Company> result;

		result = this.companyRepository.findAll();

		return result;
	}

	public Company findOne(final int companyId) {
		Assert.isTrue(companyId != 0);

		Company result;

		result = this.companyRepository.findOne(companyId);

		return result;
	}

	public Company save(final Company company) {

		Assert.notNull(company);
		Company result;
		final char[] c = company.getPhoneNumber().toCharArray();
		if ((!company.getPhoneNumber().equals(null) && !company.getPhoneNumber().equals("")))
			if (c[0] != '+') {
				final String i = this.configurationService.findAll().get(0).getDefaultCC();
				company.setPhoneNumber("+" + i + " " + company.getPhoneNumber());
			}
		if (company.getId() == 0) {
			final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			final String res = encoder.encodePassword(company.getUserAccount().getPassword(), null);
			company.getUserAccount().setPassword(res);
		}
		result = this.companyRepository.save(company);
		return result;
	}

	public void delete(final Company company) {

		final Actor actor = this.actorService.getActorLogged();
		Assert.isTrue(actor.getUserAccount().getAuthorities().iterator().next().getAuthority().equals("ADMIN"));
		Assert.notNull(company);

		this.companyRepository.delete(company);
	}

	public Company reconstruct(final Company company, final BindingResult binding) {

		Company result;
		if (company.getId() == 0) {
			this.validator.validate(company, binding);
			result = company;
		} else {
			result = this.companyRepository.findOne(company.getId());

			result.setName(company.getName());
			result.setPhoto(company.getPhoto());
			result.setPhoneNumber(company.getPhoneNumber());
			result.setEmail(company.getEmail());
			result.setAddress(company.getAddress());
			result.setVatNumber(company.getVatNumber());
			result.setSurname(company.getSurname());
			result.setCommercialName(company.getCommercialName());

			this.validator.validate(company, binding);
		}
		return result;
	}

}
