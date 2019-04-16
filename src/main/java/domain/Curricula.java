
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Curricula extends DomainEntity {


	//Relationships ----------------------------------------------------------------------------------------------------
	private Collection<EducationalData>	educationalData;
	private MiscData					miscData;
	private PersonalData				personalData;
	private Collection<PositionData>	positionData;
	private boolean isCopy;

	public boolean isCopy() {
		return isCopy;
	}

	public void setCopy(boolean copy) {
		isCopy = copy;
	}

	@OneToMany
	public Collection<EducationalData> getEducationalData() {
		return this.educationalData;
	}

	public void setEducationalData(final Collection<EducationalData> educationalData) {
		this.educationalData = educationalData;
	}

	@OneToOne(optional = true)
	public MiscData getMiscData() {
		return this.miscData;
	}

	public void setMiscData(final MiscData miscData) {
		this.miscData = miscData;
	}

	@Valid
	@OneToOne(optional = false)
	public PersonalData getPersonalData() {
		return this.personalData;
	}

	public void setPersonalData(final PersonalData personalData) {
		this.personalData = personalData;
	}

	@OneToMany
	public Collection<PositionData> getPositionData() {
		return this.positionData;
	}

	public void setPositionData(final Collection<PositionData> positionData) {
		this.positionData = positionData;
	}

}
