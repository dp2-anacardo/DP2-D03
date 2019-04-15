
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Future;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Position extends DomainEntity {

	//Properties -----------------------------------------------------------------------------------
	private String				title;
	private String				description;
	private Date				deadline;
	private String				profile;
	private Collection<String>	skill;
	private Collection<String>	technology;
	private int					salary;
	private String				ticker;
	private boolean				isFinal;
	private boolean				isCancelled;


	//Getters and setters ---------------------------------------------------------------------------

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Future
	public Date getDeadline() {
		return this.deadline;
	}

	public void setDeadline(final Date deadline) {
		this.deadline = deadline;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getProfile() {
		return this.profile;
	}

	public void setProfile(final String profile) {
		this.profile = profile;
	}

	@ElementCollection
	public Collection<String> getSkill() {
		return this.skill;
	}

	public void setSkill(final Collection<String> skill) {
		this.skill = skill;
	}

	@ElementCollection
	public Collection<String> getTechnology() {
		return this.technology;
	}

	public void setTechnology(final Collection<String> technology) {
		this.technology = technology;
	}

	public int getSalary() {
		return this.salary;
	}

	public void setSalary(final int salary) {
		this.salary = salary;
	}

	//TODO: Revisar para pattern
	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	public boolean getIsFinal() {
		return this.isFinal;
	}

	public void setIsFinal(final boolean isFinal) {
		this.isFinal = isFinal;
	}

	public boolean getIsCancelled() {
		return this.isCancelled;
	}

	public void setIsCancelled(final boolean isCancelled) {
		this.isCancelled = isCancelled;
	}


	// Relationships ----------------------------------------------------------
	private Company					company;
	private Collection<Problem>		problems;
	private Collection<Application>	applications;


	@ManyToOne(optional = false)
	public Company getCompany() {
		return this.company;
	}

	public void setCompany(final Company company) {
		this.company = company;
	}

	@ManyToMany
	public Collection<Problem> getProblems() {
		return this.problems;
	}

	public void setProblems(final Collection<Problem> problems) {
		this.problems = problems;
	}

	@ManyToMany
	public Collection<Application> getApplications() {
		return this.applications;
	}

	public void setApplications(final Collection<Application> applications) {
		this.applications = applications;
	}
}
