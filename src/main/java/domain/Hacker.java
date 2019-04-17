
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
public class Hacker extends Actor {

	//Relationships

	private Finder					finder;
	private Collection<Curricula>	curricula;


	@Valid
	@OneToOne(optional = false)
	public Finder getFinder() {
		return this.finder;
	}

	public void setFinder(final Finder finder) {
		this.finder = finder;
	}

	@Valid
	@OneToMany
	public Collection<Curricula> getCurricula() {
		return this.curricula;
	}

	public void setCurricula(final Collection<Curricula> curricula) {
		this.curricula = curricula;
	}

}
