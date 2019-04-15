package services;

import domain.Actor;
import domain.Curricula;
import domain.Hacker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import repositories.CurriculaRepository;
import security.UserAccount;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
public class CurriculaService {

    //Managed repository
    @Autowired
    private CurriculaRepository curriculaRepository;
    //Services
    @Autowired
    private ActorService actorService;
    @Autowired
    private HackerService hackerService;

    public Curricula create(){
        UserAccount userAccount;
        userAccount = this.actorService.getActorLogged().getUserAccount();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("HACKER"));

        Curricula result = new Curricula();
        return result;
    }

    public Collection<Curricula> findAll(){
        return this.curriculaRepository.findAll();
    }

    public Curricula findOne(int id){
        return this.curriculaRepository.findOne(id);
    }

    public Curricula save(Curricula curricula){
        Assert.notNull(curricula);
        Actor a = this.actorService.getActorLogged();
        Hacker h = this.hackerService.findOne(a.getId());
        Assert.notNull(h);

        if(curricula.getId()== 0 ){
            curricula = this.curriculaRepository.save(curricula);
            h.getCurricula().add(curricula);
        }else{
            curricula = this.curriculaRepository.save(curricula);
        }

        return curricula;
    }

    public void delete(Curricula curricula){
        Assert.notNull(curricula);
        Actor a = this.actorService.getActorLogged();
        Hacker h = this.hackerService.findOne(a.getId());
        Assert.notNull(h);
        Assert.isTrue(h.getCurricula().contains(curricula));
        h.getCurricula().remove(curricula);
        this.curriculaRepository.delete(curricula);
    }

}
