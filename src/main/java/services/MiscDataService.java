package services;

import domain.Actor;
import domain.Curricula;
import domain.Hacker;
import domain.MiscData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import repositories.MiscDataRepository;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
public class MiscDataService {

    //Managed Repository
    @Autowired
    private MiscDataRepository miscDataRepository;
    //Services
    @Autowired
    private ActorService actorService;
    @Autowired
    private HackerService hackerService;
    @Autowired
    private CurriculaService curriculaService;

    public MiscData create(){
        Actor a = this.actorService.getActorLogged();
        Hacker h = this.hackerService.findOne(a.getId());
        Assert.notNull(h);

        MiscData result = new MiscData();
        return result;
    }

    public Collection<MiscData> findAll(){
        return this.miscDataRepository.findAll();
    }

    public MiscData findOne(int id){
        return this.miscDataRepository.findOne(id);
    }

    public MiscData save(MiscData m,int curriculaId){
        Assert.notNull(m);
        Actor a = this.actorService.getActorLogged();
        Hacker h = this.hackerService.findOne(a.getId());
        Curricula curricula = this.curriculaService.findOne(curriculaId);
        Assert.notNull(h);
        Assert.isTrue(h.getCurricula().contains(curricula));

        if(m.getId()==0){
            Assert.isTrue(curricula.getMiscData()==null);
            m = this.miscDataRepository.save(m);
            curricula.setMiscData(m);
        }else{
            m = this.miscDataRepository.save(m);
        }
        return m;
    }

    public void delete(MiscData m){
        Assert.notNull(m);
        Actor a = this.actorService.getActorLogged();
        Hacker h = this.hackerService.findOne(a.getId());
        Assert.notNull(h);

        this.miscDataRepository.delete(m);
    }
}
