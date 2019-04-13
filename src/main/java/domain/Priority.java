
package domain;

import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Priority extends DomainEntity {

	private Map<String, String>	name;


	@ElementCollection(targetClass = String.class)
	@NotNull
	public Map<String, String> getName() {
		return this.name;
	}

	public void setName(final Map<String, String> name) {
		this.name = name;
	}

	public boolean equals(final Priority obj) {
		boolean res = false;
		if (this.getName().get("ES").equals(obj.getName().get("ES")) && this.getName().get("EN").equals(obj.getName().get("EN")))
			res = true;
		return res;
	}
}
