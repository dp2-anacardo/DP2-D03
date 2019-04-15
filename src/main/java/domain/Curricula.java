package domain;


import javax.persistence.*;
import javax.validation.Valid;
import java.util.Collection;

@Entity
@Access(AccessType.PROPERTY)
public class Curricula extends DomainEntity {

    //Relationships ----------------------------------------------------------------------------------------------------
    private Collection<EducationalData> educationalData;
    private MiscData miscData;
    private PersonalData personalData;
    private Collection<PositionData> positionData;

    @OneToMany
    public Collection<EducationalData> getEducationalData() {
        return educationalData;
    }

    public void setEducationalData(Collection<EducationalData> educationalData) {
        this.educationalData = educationalData;
    }
    @Valid
    @OneToOne(optional = true)
    public MiscData getMiscData() {
        return miscData;
    }

    public void setMiscData(MiscData miscData) {
        this.miscData = miscData;
    }

    @Valid
    @OneToOne(optional = false)
    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }

    @OneToMany
    public Collection<PositionData> getPositionData() {
        return positionData;
    }

    public void setPositionData(Collection<PositionData> positionData) {
        this.positionData = positionData;
    }
}
