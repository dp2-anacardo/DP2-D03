package domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.util.Collection;
import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
public class PositionEntity extends DomainEntity{

    //Properties -----------------------------------------------------------------------------------
    private String title;
    private String description;
    private Date deadline;
    private String profile;
    private Collection<String> skill;
    private Collection<String> technology;
    private int salary;
    private String ticker;
    private boolean isFinal;
    private boolean isCancelled;

    //Getters and setters ---------------------------------------------------------------------------

    @NotBlank
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotBlank
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Future
    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    @NotBlank
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Collection<String> getSkill() {
        return skill;
    }

    public void setSkill(Collection<String> skill) {
        this.skill = skill;
    }

    public Collection<String> getTechnology() {
        return technology;
    }

    public void setTechnology(Collection<String> technology) {
        this.technology = technology;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    //TODO: Revisar para pattern
    @NotBlank
    @SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    // Relationships ----------------------------------------------------------
    private Company company;
    private Collection<Problem> problems;
    private Collection<Application> applications;

    @ManyToOne(optional = false)
    public Company getCompany(){
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @ManyToMany
    public Collection<Problem> getProblems(){
        return this.problems;
    }

    public void setProblems(Collection<Problem> problems) {
        this.problems = problems;
    }

    @ManyToMany
    public Collection<Application> getApplications(){
        return this.applications;
    }

    public void setApplications(Collection<Application> applications) {
        this.applications = applications;
    }
}
