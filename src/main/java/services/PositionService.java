
package services;

import java.util.*;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import domain.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.PositionRepository;
import security.UserAccount;
import domain.Company;
import domain.Position;


@Service
@Transactional
public class PositionService {

	//Managed Repositories
	@Autowired
	private PositionRepository	positionRepository;

	//Supporting services
	@Autowired
	private ActorService			actorService;
	@Autowired
	private FinderService 			finderService;
	@Autowired
	private ConfigurationService	configurationService;
	@Autowired
	private Validator validator;

	public Position create() {
		UserAccount userAccount;
		userAccount = this.actorService.getActorLogged().getUserAccount();
		Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("COMPANY"));

		final Position position = new Position();

		return position;
	}

	public Collection<Position> findAll() {
		final Collection<Position> res = this.positionRepository.findAll();
		return res;
	}

	public Position findOne(final int id) {
		return this.positionRepository.findOne(id);
	}

	public Position save(Position position) {
		UserAccount userAccount;
		userAccount = this.actorService.getActorLogged().getUserAccount();
		Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("COMPANY"));

		Assert.notNull(position);

		if (position.getId() == 0) {
			position.setTicker(this.tickerGenerator(position));
			position = this.positionRepository.save(position);
		} else
			position = this.positionRepository.save(position);
		return position;
	}

	public Position saveDraft(Position position){
		Assert.notNull(position);
		position.setIsFinal(false);
		Position result = this.positionRepository.save(position);
		return result;
	}

	public Position saveFinal(Position position) {
		Assert.notNull(position);
		position.setIsFinal(true);
		Position result = this.positionRepository.save(position);
		return result;
	}

		public void delete(final Position position) {
		Assert.notNull(position);

		UserAccount userAccount;
		userAccount = this.actorService.getActorLogged().getUserAccount();
		Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("COMPANY"));

		this.positionRepository.delete(position);

	}

	private String tickerGenerator(final Position position) {
		final String res = position.getCompany().getCommercialName().substring(0, 4).toUpperCase();
		final Random random = new Random();

		final String nums = String.format("%04d", random.nextInt(10000));

		return res + "-" + nums;

	}

	public List<Position> getPositionsAvilables() {
		final List<Position> res = this.positionRepository.getPositionsAvailable();
		return res;
	}

	public Collection<Position> getPositionsByCompany(final Company company) {
		Collection<Position> positions;

		positions = this.positionRepository.getPositionsByCompany(company.getId());

		return positions;
	}

	public Collection<Position> searchPositions(String keyword){
		Collection<Position> res = Collections.emptyList();
		List<Position> proAux1;
		List<Position> proAux2;

		if (!(keyword == null || keyword.equals(""))) {
			proAux1 = (List<Position>) this.finderService.getPositionsByKeyWord(keyword);
			proAux2 = (List<Position>) this.finderService.getPositionsContainsKeyWord(keyword);

			Set<Position> set = new LinkedHashSet<>(proAux1);
			set.addAll(proAux2);
			res = new ArrayList<>(set);
		}

		Configuration conf;
		conf = this.configurationService.getConfiguration();

		res = this.finderService.maxPosition(res, conf);

		return res;
	}

	public Position reconstruct(Position p, BindingResult binding){
		Position result;
		if(p.getId() == 0){
			result = this.create();
		}else{
			result = this.positionRepository.findOne(p.getId());
		}
		result.setTitle(p.getTitle());
		result.setDescription(p.getDescription());
		result.setDeadline(p.getDeadline());
		result.setProfile(p.getProfile());

		validator.validate(result, binding);
		if (binding.hasErrors()){
			throw new ValidationException();
		}
		return result;
	}
}
