
package services;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
	private ActorService		actorService;


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
}
