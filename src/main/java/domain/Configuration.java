package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

    private int					maxResults;
    private int					maxTime;
    private String				systemName;
    private String				banner;
    private String				welcomeMessageEn;
    private String				welcomeMessageEs;
    private Collection<String>	spamWords;
    private String				defaultCC;


    @Range(min = 10, max = 100)
    public int getMaxResults() {
        return this.maxResults;
    }

    public void setMaxResults(final int maxResults) {
        this.maxResults = maxResults;
    }

    @Range(min = 1, max = 24)
    public int getMaxTime() {
        return this.maxTime;
    }

    public void setMaxTime(final int maxTime) {
        this.maxTime = maxTime;
    }

    @NotBlank
    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getSystemName() {
        return this.systemName;
    }

    public void setSystemName(final String systemName) {
        this.systemName = systemName;
    }

    @NotBlank
    @URL
    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getBanner() {
        return this.banner;
    }

    public void setBanner(final String banner) {
        this.banner = banner;
    }

    @NotBlank
    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getWelcomeMessageEn() {
        return this.welcomeMessageEn;
    }

    public void setWelcomeMessageEn(final String welcomeMessageEn) {
        this.welcomeMessageEn = welcomeMessageEn;
    }

    @NotBlank
    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getWelcomeMessageEs() {
        return this.welcomeMessageEs;
    }

    public void setWelcomeMessageEs(final String welcomeMessageEs) {
        this.welcomeMessageEs = welcomeMessageEs;
    }

    @ElementCollection
    public Collection<String> getSpamWords() {
        return this.spamWords;
    }

    public void setSpamWords(final Collection<String> spamWords) {
        this.spamWords = spamWords;
    }

    @NotBlank
    public String getDefaultCC() {
        return this.defaultCC;
    }

    @SafeHtml(whitelistType = WhiteListType.NONE)
    public void setDefaultCC(final String defaultCC) {
        this.defaultCC = defaultCC;
    }
}