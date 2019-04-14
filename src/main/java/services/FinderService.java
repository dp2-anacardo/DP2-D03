package services;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FinderRepository;
import domain.Configuration;
import domain.Finder;

@Service
@Transactional
public class FinderService {

    //Managed repository
    @Autowired
    private FinderRepository		finderRepository;

    //Services
    @Autowired
    private ConfigurationService	configurationService;

    //Validator
    @Autowired
    private Validator				validator;


    //Simple CRUD Methods
    public Finder create() {
        Finder result;
        result = new Finder();
        final Collection<Position> positions = new ArrayList<Position>();
        result.setLastUpdate(new Date());
        result.setPositions(positions);
        return result;
    }
    public Collection<Finder> findAll() {
        return this.finderRepository.findAll();
    }

    public Finder findOne(final Integer id) {
        return this.finderRepository.findOne(id);
    }

    public Finder save(Finder finder) {

        Assert.notNull(finder);
        Collection<Position> result = Collections.emptyList();
        List<Position> pro1 = null;
        List<Position> pro2 = null;
        List<Position> pro3 = null;
        List<Position> pro4 = null;
        if (!(finder.getKeyWord() == null || finder.getKeyWord() == ""))
            pro1 = (List<Position>) this.finderRepository.getPositionsByKeyWord(finder.getKeyWord());
        if (finder.getDeadline() != null)
            pro2 = (List<Position>) this.finderRepository.getPositionsByDeadline(finder.getDeadline());
        if (finder.getMaxDeadline() != null)
            pro3 = (List<Position>) this.finderRepository.getPositionsUntilDeadline(new Date(), finder.getMaxDeadline());
        if (finder.getMinSalary() != 0 )
            pro4 = (List<Position>) this.finderRepository.getPositionsByMinSalary(finder.getMinSalary());
        if (!(pro1 == null && pro2 == null && pro3 == null && pro4 == null)) {
            if (pro1 == null)
                pro1 = (List<Position>) this.finderRepository.findAllFinal();
            if (pro2 == null)
                pro2 = (List<Position>) this.finderRepository.findAllFinal();
            if (pro3 == null)
                pro3 = (List<Position>) this.finderRepository.findAllFinal();
            if (pro4 == null)
                pro4 = (List<Position>) this.finderRepository.findAllFinal();
            pro1.retainAll(pro2);
            pro1.retainAll(pro3);
            pro1.retainAll(pro4);

            result = pro1;
        }

        Configuration conf;
        conf = this.configurationService.getConfiguration();

        if (result.size() > conf.getMaxResults()) {

            final List<Position> copy = (List<Position>) result;

            final List<Position> paradesLim = new ArrayList<Position>();
            for (int i = 0; i < conf.getMaxResults(); i++)
                paradesLim.add(copy.get(i));
            result = paradesLim;

        }

        finder.setPositions(result);
        final Date moment = new Date();
        finder.setLastUpdate(moment);

        finder = this.finderRepository.save(finder);
        return finder;
    }

    //Reconstruct
    public Finder reconstruct(final Finder finder, final BindingResult binding) {
        Finder result;

        result = this.finderRepository.findOne(finder.getId());

        finder.setVersion(result.getVersion());

        result = finder;
        this.validator.validate(finder, binding);

        return result;
    }

    //Another methods
    public Collection<Position> findAllFinal() {
        return this.finderRepository.findAllFinal();
    }

    public void delete(final Finder f) {
        Assert.notNull(f);
        this.finderRepository.delete(f);
    }
}
