package services;

import domain.Actor;
import domain.Curricula;
import domain.Hacker;
import domain.PositionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import repositories.PositionDataRepository;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Transactional
public class PositionDataService {

    //Managed Repository
    @Autowired
    private PositionDataRepository positionDataRepository;
    //Services
    @Autowired
    private ActorService actorService;
    @Autowired
    private HackerService hackerService;
    @Autowired
    private CurriculaService curriculaService;

    public PositionData create(){
        Actor a = this.actorService.getActorLogged();
        Hacker h = this.hackerService.findOne(a.getId());
        Assert.notNull(h);

        PositionData result = new PositionData();
        return result;
    }

    public Collection<PositionData> findAll(){
        return this.positionDataRepository.findAll();
    }

    public PositionData findOne(int id){
        return this.positionDataRepository.findOne(id);
    }

    public PositionData save(PositionData p, int curriculaId){
        Assert.notNull(p);
        Actor a = this.actorService.getActorLogged();
        Hacker h = this.hackerService.findOne(a.getId());
        Curricula curricula = this.curriculaService.findOne(curriculaId);
        Assert.notNull(h);

        if(p.getEndDate()!=null){
            Assert.isTrue(p.getStartDate().before(p.getEndDate()));
        }

        if(p.getId()==0){
            p = this.positionDataRepository.save(p);
            curricula.getPositionData().add(p);
        }else{
            p = this.positionDataRepository.save(p);
        }
        return p;
    }

    public PositionData save2(PositionData positionData){
        Assert.notNull(positionData);

        PositionData result = this.positionDataRepository.save(positionData);

        return result;
    }

    public void delete(PositionData p){
        Assert.notNull(p);
        Actor a = this.actorService.getActorLogged();
        Hacker h = this.hackerService.findOne(a.getId());
        Assert.notNull(h);

        for (Curricula c : h.getCurricula()){
            if(c.getPositionData().contains(p)) {
                c.getPositionData().remove(p);
                break;
            }
        }
        this.positionDataRepository.delete(p);
    }
}
