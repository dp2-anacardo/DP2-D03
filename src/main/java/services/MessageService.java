package services;

import domain.Actor;
import domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.MessageRepository;
import security.LoginService;
import security.UserAccount;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

@Service
@Transactional
public class MessageService {

    // Manage Repository
    @Autowired
    private MessageRepository messageRepository;

    //Supporting devices

    @Autowired
    private ActorService actorService;

    @Autowired
    private Validator validator;


    // CRUD methods
    public Message create() {
        final Message result = new Message();
        final Calendar calendar = new GregorianCalendar();
        final Collection<String> tags = new ArrayList<String>();

        result.setTags(tags);
        result.setSendingMoment(calendar.getTime());

        return result;
    }

    public Message findOne(final int messageID) {
        final Message result = this.messageRepository.findOne(messageID);
        Assert.notNull(result);

        return result;
    }

    public Message save(final Message message) {
        Assert.notNull(message);
        final Message result;

        Actor sender = null;

        if (message.getId() == 0) {
            Assert.notNull(message);

            final String notification1 = "Enrolment accepted \n Inscripci�n aceptada";
            final String notification2 = "Drop out brotherhood \n Salida de fraternidad";
            final Integer actors = this.actorService.findAll().size();

            if (message.getSubject().equals(notification1) || message.getSubject().equals(notification2)
                    || (message.getRecipients().size() == actors)) {

                final Collection<Actor> recipients = message.getRecipients();
                Assert.notNull(recipients);
                Assert.notEmpty(recipients);

                result = this.messageRepository.save(message);

                for (final Actor recipient : recipients)
                    recipient.getMessages().add(result);

            } else {
                final UserAccount userAccount = LoginService.getPrincipal();

                sender = this.actorService.findByUserAccount(userAccount);
                message.setSender(sender);

                final Collection<Actor> recipients = message.getRecipients();
                Assert.notNull(recipients);
                Assert.notEmpty(recipients);

                result = this.messageRepository.save(message);

                if (sender != null)
                    sender.getMessages().add(result);

                for (final Actor recipient : recipients)
                    recipient.getMessages().add(result);
            }

        } else
            result = this.messageRepository.save(message);
        return result;
    }

    public void delete(final Message message) {
        Assert.notNull(message);
        final UserAccount userAccount = LoginService.getPrincipal();
        Assert.notNull(userAccount);
        final Actor actor = this.actorService.findByUsername(userAccount.getUsername());

        Assert.isTrue(message.getRecipients().contains(actor) || message.getSender().equals(actor));

        if (!message.getTags().contains("DELETED")) {
            message.getTags().add("DELETED");
            this.messageRepository.save(message);
        } else {
            actor.getMessages().remove(message);
            final Integer msgInPool = this.findMessageInPool(message.getId());
            if (msgInPool > 0)
                actor.getMessages().remove(message);
            else
                this.messageRepository.delete(message.getId());

        }

    }

    public void deleteAll(final Message m) {

        this.messageRepository.delete(m);

    }

    // Other methods

    public Integer findMessageInPool(final int messageID) {
        final Integer result = this.messageRepository.findMessageInPool(messageID);
        Assert.notNull(result);

        return result;
    }

    public Collection<Message> findAllByActor(final int actorID) {
        final Collection<Message> result = this.messageRepository.findAllByActor(actorID);
        Assert.notNull(result);

        return result;
    }

    public Collection<Message> findAllReceivedByActor(final int actorID) {
        final Collection<Message> result = this.messageRepository.findAllReceivedByActor(actorID);
        Assert.notNull(result);

        return result;
    }
    
    public Message reconstruct(final Message message, final BindingResult binding) {
        final Message result;

        result = this.create();

        final Calendar calendar = new GregorianCalendar();
        final long time = calendar.getTimeInMillis() - 500;
        calendar.setTimeInMillis(time);

        result.setSendingMoment(calendar.getTime());
        result.setSubject(message.getSubject());
        result.setBody(message.getBody());
        result.setTags(message.getTags());
        result.setRecipients(message.getRecipients());

        this.validator.validate(result, binding);

        return result;

    }
}
