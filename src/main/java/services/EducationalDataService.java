package services;

import domain.Actor;
import domain.Curricula;
import domain.EducationalData;
import domain.Hacker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import repositories.EducationalDataRepository;
import security.UserAccount;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
public class EducationalDataService {

    //Managed repository
    @Autowired
    private EducationalDataRepository educationalDataRepository;
    //Services
    @Autowired
    private ActorService actorService;
    @Autowired
    private HackerService hackerService;
    @Autowired
    private CurriculaService curriculaService;

    public EducationalData create(){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("HACKER"));

        EducationalData result = new EducationalData();

        return result;
    }

    public Collection<EducationalData> findAll(){
        return this.educationalDataRepository.findAll();
    }

    public EducationalData findOne(int id){
        return this.educationalDataRepository.findOne(id);
    }

    public EducationalData save(EducationalData educationalData,int curriculaId){
        Assert.notNull(educationalData);
        Actor a = this.actorService.getActorLogged();
        Hacker h = this.hackerService.findOne(a.getId());
        Curricula curricula = this.curriculaService.findOne(curriculaId);
        Assert.notNull(h);
        Assert.isTrue(h.getCurricula().contains(curricula));

        if(educationalData.getEndDate()!=null){
            Assert.isTrue(educationalData.getStartDate().before(educationalData.getEndDate()));
        }

        if(educationalData.getId() == 0){
            educationalData = this.educationalDataRepository.save(educationalData);
            curricula.getEducationalData().add(educationalData);
        }else{
            educationalData = this.educationalDataRepository.save(educationalData);
        }
        return educationalData;
    }

    public void delete(EducationalData educationalData){
        Assert.notNull(educationalData);
        Actor a = this.actorService.getActorLogged();
        Hacker h = this.hackerService.findOne(a.getId());
        Assert.notNull(h);

        for(Curricula c : h.getCurricula()){
            if(c.getEducationalData().contains(educationalData)){
                c.getEducationalData().remove(educationalData);
                break;
            }
        }
        this.educationalDataRepository.delete(educationalData);
    }
}
