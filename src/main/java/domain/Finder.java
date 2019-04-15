
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Finder extends DomainEntity {

	private String						keyWord;
	private Date						deadline;
	private Date						maxDeadline;
	private int							minSalary;
	private Date						lastUpdate;

	//Relationships

	private Collection<PositionData>	positions;


	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getKeyWord() {
		return this.keyWord;
	}

	public void setKeyWord(final String keyWord) {
		if (keyWord == null)
			this.keyWord = keyWord;
		else
			this.keyWord = keyWord.trim();
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getDeadline() {
		return this.deadline;
	}

	public void setDeadline(final Date deadline) {
		this.deadline = deadline;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMaxDeadline() {
		return this.maxDeadline;
	}

	public void setMaxDeadline(final Date maxDeadline) {
		this.maxDeadline = maxDeadline;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public int getMinSalary() {
		return this.minSalary;
	}

	public void setMinSalary(final int minSalary) {
		this.minSalary = minSalary;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yy HH:mm")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(final Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	//Relationships

	@Valid
	@ManyToMany
	public Collection<PositionData> getPositions() {
		return this.positions;
	}

	public void setPositions(final Collection<PositionData> positions) {
		this.positions = positions;
	}

}
