
package services;

import domain.*;
import forms.AdministratorForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class AdministratorService {

    //Managed Repositories
    @Autowired
    private AdministratorRepository administratorRepository;
    //Supporting services
    @Autowired
    private ActorService actorService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private HackerService hackerService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private Validator validator;


    public Administrator create() {

        final Actor actor = this.actorService.getActorLogged();
        Assert.isTrue(actor.getUserAccount().getAuthorities().iterator().next().getAuthority().equals("ADMIN"));
        final Authority auth;
        final UserAccount userAccount;
        final Collection<Authority> authorities;
        final Collection<SocialProfile> profiles;
        final Collection<Message> sent;
        final Collection<Message> received;
        final Administrator a = new Administrator();
        userAccount = new UserAccount();
        auth = new Authority();
        authorities = new ArrayList<Authority>();
        profiles = new ArrayList<SocialProfile>();
        sent = new ArrayList<Message>();
        received = new ArrayList<Message>();

        auth.setAuthority(Authority.ADMIN);
        authorities.add(auth);
        userAccount.setAuthorities(authorities);
        a.setUserAccount(userAccount);
        a.setIsBanned(false);
        a.setIsSpammer(false);
        a.setSocialProfiles(profiles);
        a.setMessagesR(received);
        a.setMessagesS(sent);

        return a;
    }

    public Collection<Administrator> findAll() {
        Collection<Administrator> result;

        result = this.administratorRepository.findAll();

        return result;
    }

    public Administrator findOne(final int administratorId) {
        Assert.isTrue(administratorId != 0);

        Administrator result;

        result = this.administratorRepository.findOne(administratorId);

        return result;
    }

    public Administrator save(final Administrator administrator) {
        UserAccount userAccount;
        userAccount = LoginService.getPrincipal();
        Assert.isTrue(userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN"));
        Assert.notNull(administrator);
        Administrator result;
        final char[] c = administrator.getPhoneNumber().toCharArray();
        if ((!administrator.getPhoneNumber().equals(null) && !administrator.getPhoneNumber().equals("")))
            if (c[0] != '+') {
                final String i = this.configurationService.findAll().get(0).getCountryCode();
                administrator.setPhoneNumber("+" + i + " " + administrator.getPhoneNumber());
            }
        if (administrator.getId() == 0) {
            final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
            final String res = encoder.encodePassword(administrator.getUserAccount().getPassword(), null);
            administrator.getUserAccount().setPassword(res);
        }
        result = this.administratorRepository.save(administrator);
        return result;
    }

    public void delete(final Administrator administrator) {

        final Actor actor = this.actorService.getActorLogged();
        Assert.isTrue(actor.getUserAccount().getAuthorities().iterator().next().getAuthority().equals("ADMIN"));
        Assert.notNull(administrator);
        Assert.isTrue(actor.getId() != 0);

        this.administratorRepository.delete(administrator);
    }

    public Administrator reconstruct(final Administrator admin, final BindingResult binding) {

        Administrator result;
        if (admin.getId() == 0) {
            this.validator.validate(admin, binding);
            result = admin;
        } else {
            result = this.administratorRepository.findOne(admin.getId());

            result.setName(admin.getName());
            result.setPhoto(admin.getPhoto());
            result.setPhoneNumber(admin.getPhoneNumber());
            result.setEmail(admin.getEmail());
            result.setAddress(admin.getAddress());
            result.setVatNumber(admin.getVatNumber());
            result.setSurname(admin.getSurname());

            this.validator.validate(admin, binding);
        }
        return result;
    }

    //Validador de contraseñas
    public Boolean checkPass(final String pass, final String confirmPass) {
        Boolean res = false;
        if (pass.compareTo(confirmPass) == 0)
            res = true;
        return res;
    }

    //Objeto formulario
    public Administrator reconstruct(final AdministratorForm admin, final BindingResult binding) {

        final Administrator result = this.create();
        result.setAddress(admin.getAddress());
        result.setEmail(admin.getEmail());
        result.setId(admin.getId());
        result.setName(admin.getName());
        result.setPhoneNumber(admin.getPhoneNumber());
        result.setPhoto(admin.getPhoto());
        result.setSurname(admin.getSurname());
        result.getUserAccount().setPassword(admin.getPassword());
        result.getUserAccount().setUsername(admin.getUsername());
        result.setVersion(admin.getVersion());
        result.setVatNumber(admin.getVatNumber());

        this.validator.validate(result, binding);
        return result;
    }

    public void computeAllSpam() {

        Collection<Hacker> hackers;
        Collection<Company> companies;

        // Make sure that the principal is an Admin
        final Actor principal = this.actorService.getActorLogged();
        Assert.isInstanceOf(Administrator.class, principal);

        hackers = this.hackerService.findAll();
        for (final Hacker hacker : hackers) {
            Integer spam = 0;
            Double ratio = 0.0;
            Collection<Message> sentMessages = this.messageService.findAllSentByActor(hacker.getId());
            if (sentMessages.size() > 0) {
                for (Message m : sentMessages) {
                    if (checkSpam(m))
                        spam++;
                }
                ratio = 1.0 * (spam / sentMessages.size());
                if (ratio >= 0.1)
                    hacker.setIsSpammer(true);
                else
                    hacker.setIsSpammer(false);
            } else
                hacker.setIsSpammer(false);

            this.hackerService.save(hacker);
        }

        companies = this.companyService.findAll();
        for (final Company company : companies) {
            Integer spam = 0;
            Double ratio = 0.0;
            Collection<Message> sentMessages = this.messageService.findAllSentByActor(company.getId());
            if (sentMessages.size() > 0) {
                for (Message m : sentMessages) {
                    if (checkSpam(m))
                        spam++;
                }
                ratio = 1.0 * (spam / sentMessages.size());
                if (ratio >= 0.1)
                    company.setIsSpammer(true);
                else
                    company.setIsSpammer(false);
            } else
                company.setIsSpammer(false);

            this.companyService.save(company);
        }
    }

    private Boolean checkSpam(final Message message) {
        Boolean spam = false;

        final Configuration configuration = this.configurationService.getConfiguration();
        final Collection<String> spamWords = configuration.getSpamWords();
        for (final String word : spamWords)
            if (message.getSubject().contains(word)) {
                spam = true;
                break;
            }
        if (!spam)
            for (final String word : spamWords)
                if (message.getBody().contains(word)) {
                    spam = true;
                    break;
                }

        return spam;
    }

    public List<Double> getStatsPositionsPerCompany(){
        Assert.isTrue(LoginService.getPrincipal().getAuthorities().iterator().next().equals("ADMIN"));

        List<Double> result = new ArrayList<>();

        result.add(this.administratorRepository.getAvgNumberOfPositions());
        result.add(this.administratorRepository.getMinimumNumberOfPositions());
        result.add(this.administratorRepository.getMaximumNumberOfPositions());
        result.add(this.administratorRepository.getStddevNumberOfPositions());

        return result;
    }

    public List<Double> getStatsApplicationsPerHacker(){
        Assert.isTrue(LoginService.getPrincipal().getAuthorities().iterator().next().equals("ADMIN"));

        List<Double> result = new ArrayList<>();

        result.add(this.administratorRepository.getAvgNumberOfApplications());
        result.add(this.administratorRepository.getMinimumNumberOfApplications());
        result.add(this.administratorRepository.getMaximumNumberOfApplications());
        result.add(this.administratorRepository.getStddevNumberOfApplications());

        return result;
    }

    public List<Company> getCompaniesWithOfferedMorePositions(){
        Assert.isTrue(LoginService.getPrincipal().getAuthorities().iterator().next().equals("ADMIN"));

        List<Company> result = new ArrayList<>();

        result.addAll(this.administratorRepository.getCompaniesWithMorePositionsOffered());

        return result;
    }

    public List<Hacker> getHackersWithMoreApplications(){
        Assert.isTrue(LoginService.getPrincipal().getAuthorities().iterator().next().equals("ADMIN"));

        List<Hacker> result = new ArrayList<>();

        result.addAll(this.administratorRepository.getHackersWithMoreApplications());

        return result;
    }

    public List<Double> getStatsSalariesOffered(){
        Assert.isTrue(LoginService.getPrincipal().getAuthorities().iterator().next().equals("ADMIN"));

        List<Double> result = new ArrayList<>();

        result.add(this.administratorRepository.getAvgSalaryOffered());
        result.add(this.administratorRepository.getMinSalaryOffered());
        result.add(this.administratorRepository.getMaxSalaryOffered());
        result.add(this.administratorRepository.getStddevSalaryOffered());

        return result;
    }

    public Position getBestPositionSalaryOffered(){
        Assert.isTrue(LoginService.getPrincipal().getAuthorities().iterator().next().equals("ADMIN"));

        Position position;

        position = (Position) this.administratorRepository.getBestPositionSalaryOffered().get(0);

        return position;
    }

    public Position getWorstPositionSalaryOffered(){
        Assert.isTrue(LoginService.getPrincipal().getAuthorities().iterator().next().equals("ADMIN"));

        Position position;

        position = (Position) this.administratorRepository.getWorstPositionSalaryOffered().get(0);

        return position;
    }

    public List<Double> getStatsCurricula(){
        Assert.isTrue(LoginService.getPrincipal().getAuthorities().iterator().next().equals("ADMIN"));

        List<Double> result = new ArrayList<>();

        result.add(this.administratorRepository.getAvgNumberOfCurricula());
        result.add(this.administratorRepository.getMinimumNumberOfCurricula());
        result.add(this.administratorRepository.getMaximumNumberOfCurricula());
        result.add(this.administratorRepository.getStddevNumberOfCurricula());

        return result;
    }

    public List<Double> getStatsFinder(){
        Assert.isTrue(LoginService.getPrincipal().getAuthorities().iterator().next().equals("ADMIN"));

        List<Double> result = new ArrayList<>();

        result.add(this.administratorRepository.getAvgResultFinder());
        result.add(this.administratorRepository.getMinimumResultFinder());
        result.add(this.administratorRepository.getMaximumResultFinder());
        result.add(this.administratorRepository.getStddevResultFinder());

        return result;
    }

    public Double getRatioOfNotEmptyFinders(){
        Assert.isTrue(LoginService.getPrincipal().getAuthorities().iterator().next().equals("ADMIN"));

        Double result = this.administratorRepository.getRatioOfNotEmptyFinders();

        return result;
    }

    public Double getRatioOfEmptyFinders(){
        Assert.isTrue(LoginService.getPrincipal().getAuthorities().iterator().next().equals("ADMIN"));

        Double result = this.administratorRepository.getRatioOfEmptyFinders();

        return result;
    }

}
