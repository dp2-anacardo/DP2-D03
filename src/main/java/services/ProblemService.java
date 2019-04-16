
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProblemRepository;
import security.LoginService;
import security.UserAccount;
import datatype.Url;
import domain.Actor;
import domain.Company;
import domain.Problem;

@Service
@Transactional
public class ProblemService {

	// Manage Repository
	@Autowired
	private ProblemRepository	problemRepository;

	//Supporting devices

	@Autowired
	private ActorService		actorService;

	@Autowired
	private CompanyService		companyService;

	@Autowired
	private Validator			validator;


	// CRUD methods
	public Problem create() {
		final Problem result = new Problem();

		final Collection<Url> attachments = new ArrayList<Url>();
		result.setAttachment(attachments);

		return result;
	}

	public Problem findOne(final int problemID) {
		final Problem result = this.problemRepository.findOne(problemID);
		Assert.notNull(result);

		return result;
	}

	public Problem save(final Problem problem) {
		Assert.notNull(problem);
		final Actor actor = this.actorService.getActorLogged();
		Assert.isTrue(actor instanceof Company);
		final Problem result;

		if (problem.getId() == 0)
			result = this.problemRepository.save(problem);
		else {
			Assert.isTrue(problem.getCompany() == actor);
			result = this.problemRepository.save(problem);
		}

		return result;
	}

	public void delete(final Problem problem) {
		Assert.notNull(problem);
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		this.problemRepository.delete(problem.getId());
	}

	// Other methods

	public Problem reconstruct(final Problem problem, final BindingResult binding) {
		Problem result;

		if (problem.getId() == 0) {
			final Company actor = (Company) this.actorService.getActorLogged();
			result = problem;
			result.setCompany(actor);

			this.validator.validate(result, binding);

		} else {
			result = this.problemRepository.findOne(problem.getId());

			problem.setCompany(result.getCompany());
			problem.setVersion(result.getVersion());

			result = problem;

			this.validator.validate(result, binding);

		}

		return result;

	}

	public Collection<Problem> findAllByCompany(final int companyID) {
		final Collection<Problem> result = this.problemRepository.findAllByCompany(companyID);
		Assert.notNull(result);

		return result;
	}

	public List<Problem> getProblemsFinalByCompany(final Company company) {
		List<Problem> problems;

		problems = this.problemRepository.getProblemsFinalByCompany(company.getId());

		return problems;
	}
}
