
package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PriorityRepository;
import domain.Actor;
import domain.Priority;
import forms.PriorityForm;

@Service
@Transactional
public class PriorityService {

	@Autowired
	private PriorityRepository	priorityRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;


	public Priority create() {

		Priority result;
		result = new Priority();
		final Map<String, String> name = new HashMap<String, String>();
		result.setName(name);
		return result;

	}
	public Priority findOne(final Integer priorityId) {

		Priority result;
		Assert.notNull(priorityId);
		result = this.priorityRepository.findOne(priorityId);
		Assert.notNull(result);
		return result;
	}

	public Collection<Priority> findAll() {

		Collection<Priority> result;
		result = this.priorityRepository.findAll();
		Assert.notEmpty(result);
		return result;
	}

	public Priority save(final Priority priority) {

		Priority result;
		Assert.notNull(priority);
		final Actor actor = this.actorService.getActorLogged();
		Assert.isTrue(actor.getUserAccount().getAuthorities().iterator().next().getAuthority().equals("ADMIN"));
		result = this.priorityRepository.save(priority);
		return result;

	}

	public Integer getPriorityCount(final Priority priority) {
		Assert.notNull(priority);

		final Integer result;

		result = this.priorityRepository.getPriorityCount(priority.getId());

		return result;
	}

	public void delete(final Priority priority) {
		Assert.notNull(priority);
		final Actor actor = this.actorService.getActorLogged();
		Assert.isTrue(actor.getUserAccount().getAuthorities().iterator().next().getAuthority().equals("ADMIN"));
		this.priorityRepository.delete(priority);
	}

	public Priority reconstruct(final PriorityForm priorityForm, final BindingResult binding) {
		final Priority result = this.create();

		if (priorityForm.getId() == 0) {

			result.getName().put("ES", priorityForm.getTitleES());
			result.getName().put("EN", priorityForm.getTitleEN());

			this.validator.validate(result, binding);

		} else {
			Priority bbdd;
			bbdd = this.priorityRepository.findOne(priorityForm.getId());

			result.getName().put("ES", priorityForm.getTitleES());
			result.getName().put("EN", priorityForm.getTitleEN());
			result.setId(bbdd.getId());
			result.setVersion(bbdd.getVersion());

			bbdd = result;

			this.validator.validate(bbdd, binding);
		}

		return result;

	}
	// TEST

	public Boolean exists(final Priority a) {
		Boolean exist = false;

		final Collection<Priority> priorities = this.findAll();
		for (final Priority p : priorities)
			if (a.equals(p)) {
				exist = true;
				break;
			}

		return exist;
	}

	public Priority getHighPriority() {
		return this.priorityRepository.getHighPriority();
	}
}
