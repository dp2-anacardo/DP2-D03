
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CurriculaRepository;
import security.UserAccount;
import domain.Actor;
import domain.Curricula;
import domain.EducationalData;
import domain.Hacker;
import domain.MiscData;
import domain.PersonalData;
import domain.PositionData;

@Service
@Transactional
public class CurriculaService {

	//Managed repository
	@Autowired
	private CurriculaRepository	curriculaRepository;
	//Services
	@Autowired
	private ActorService		actorService;
	@Autowired
	private HackerService		hackerService;


	public Curricula create() {
		UserAccount userAccount;
		userAccount = this.actorService.getActorLogged().getUserAccount();
		Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("HACKER"));

		final Curricula result = new Curricula();
		return result;
	}

	public Collection<Curricula> findAll() {
		return this.curriculaRepository.findAll();
	}

	public Curricula findOne(final int id) {
		return this.curriculaRepository.findOne(id);
	}

	public Curricula save(Curricula curricula) {
		Assert.notNull(curricula);
		final Actor a = this.actorService.getActorLogged();
		final Hacker h = this.hackerService.findOne(a.getId());
		Assert.notNull(h);

		if (curricula.getId() == 0) {
			curricula = this.curriculaRepository.save(curricula);
			h.getCurricula().add(curricula);
		} else
			curricula = this.curriculaRepository.save(curricula);

		return curricula;
	}

	public void delete(final Curricula curricula) {
		Assert.notNull(curricula);
		final Actor a = this.actorService.getActorLogged();
		final Hacker h = this.hackerService.findOne(a.getId());
		Assert.notNull(h);
		Assert.isTrue(h.getCurricula().contains(curricula));
		h.getCurricula().remove(curricula);
		this.curriculaRepository.delete(curricula);
	}

	public Curricula copy(final Curricula curricula) {
		Assert.notNull(curricula);
		Curricula result;
		final Collection<EducationalData> educationalData = new ArrayList<>();
		final MiscData miscData = new MiscData();
		final PersonalData personalData = new PersonalData();
		final Collection<PositionData> positionData = new ArrayList<>();

		for (final EducationalData ed : curricula.getEducationalData()) {
			final EducationalData res = new EducationalData();
			res.setDegree(ed.getDegree());
			res.setEndDate(ed.getEndDate());
			res.setStartDate(ed.getStartDate());
			res.setInstitution(ed.getInstitution());
			res.setMark(ed.getMark());
			educationalData.add(res);
		}

		miscData.setAttachment(curricula.getMiscData().getAttachment());
		miscData.setFreeText(curricula.getMiscData().getFreeText());

		personalData.setFullName(curricula.getPersonalData().getFullName());
		personalData.setGithubProfile(curricula.getPersonalData().getGithubProfile());
		personalData.setLinkedInProfile(curricula.getPersonalData().getLinkedInProfile());
		personalData.setPhoneNumber(curricula.getPersonalData().getPhoneNumber());
		personalData.setStatement(curricula.getPersonalData().getStatement());

		for (final PositionData pd : curricula.getPositionData()) {
			final PositionData res = new PositionData();
			res.setDescription(pd.getDescription());
			res.setEndDate(pd.getEndDate());
			res.setStartDate(pd.getStartDate());
			res.setTitle(pd.getTitle());
			positionData.add(res);
		}

		result = new Curricula();

		result.setEducationalData(educationalData);
		result.setMiscData(miscData);
		result.setPersonalData(personalData);
		result.setPositionData(positionData);
		result.setIsCopy(true);
		result = this.curriculaRepository.save(result);

		return result;
	}

	public Collection<Curricula> getCurriculaByHacker() {
		final Actor a = this.actorService.getActorLogged();
		final Hacker h = this.hackerService.findOne(a.getId());
		Assert.notNull(h);
		return this.curriculaRepository.getCurriculaByHacker(h);
	}

}
