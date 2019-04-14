
package domain;

import com.google.gson.annotations.Expose;
import datatype.Url;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Access(AccessType.PROPERTY)
public class Problem extends DomainEntity {

    // Attributes -------------------------------------------------------------
    @Expose
    private String title;
    @Expose
    private String statement;
    @Expose
    private String hint;
    @Expose
    private Collection<Url> attachment;
    @Expose
    private Boolean isFinal;

    @NotBlank
    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotBlank
    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getStatement() {
        return this.statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }


    @SafeHtml(whitelistType = WhiteListType.NONE)
    public String getHint() {
        return this.hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @Valid
    public Collection<Url> getAttachment() {
        return this.attachment;
    }

    public void setAttachment(Collection<Url> attachment) {
        this.attachment = attachment;
    }

    @NotNull
    public Boolean getFinal() {
        return isFinal;
    }

    public void setFinal(Boolean aFinal) {
        isFinal = aFinal;
    }


    // Relationships

    private Company company;

    @Valid
    @ManyToOne(optional = false)
    public Company getCompany(){
        return this.company;
    }

    public void setCompany(final Company company){
        this.company = company;
    }

}
