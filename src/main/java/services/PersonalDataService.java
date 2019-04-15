package services;

import domain.Actor;
import domain.Curricula;
import domain.Hacker;
import domain.PersonalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import repositories.PersonalDataRepository;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
public class PersonalDataService {

    //Managed repository
    @Autowired
    private PersonalDataRepository personalDataRepository;
    //Services
    @Autowired
    private ActorService actorService;
    @Autowired
    private HackerService hackerService;
    @Autowired
    private CurriculaService curriculaService;

    public PersonalData create(){
        Actor a = this.actorService.getActorLogged();
        Hacker h = this.hackerService.findOne(a.getId());
        Assert.notNull(h);

        PersonalData result = new PersonalData();
        return result;
    }

    public Collection<PersonalData> findAll(){
        return this.personalDataRepository.findAll();
    }

    public PersonalData findOne(int id){
        return this.personalDataRepository.findOne(id);
    }

    public PersonalData save(PersonalData p){
        Assert.notNull(p);
        Actor a = this.actorService.getActorLogged();
        Hacker h = this.hackerService.findOne(a.getId());
        Assert.notNull(h);

        p = this.personalDataRepository.save(p);
        return p;

    }

    public void delete(PersonalData p ){
        Assert.notNull(p);
        Actor a = this.actorService.getActorLogged();
        Hacker h = this.hackerService.findOne(a.getId());
        Assert.notNull(h);
        this.personalDataRepository.delete(p);
    }

}
