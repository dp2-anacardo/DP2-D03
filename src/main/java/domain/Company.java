<<<<<<< HEAD

=======
>>>>>>> a26ef29a2e6434cf055352a9bbbcc6328bac7f21
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

<<<<<<< HEAD
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Company extends Actor {

	private String	commercialName;


	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getCommercialName() {
		return this.commercialName;
	}

	public void setCommercialName(final String commercialName) {
		this.commercialName = commercialName;
	}

=======
@Entity
@Access(AccessType.PROPERTY)
public class Company extends Actor{
>>>>>>> a26ef29a2e6434cf055352a9bbbcc6328bac7f21
}
