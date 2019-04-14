
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Actor;
import domain.Message;
import repositories.MessageRepository;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class MessageService {

	// Manage Repository
	@Autowired
	private MessageRepository		messageRepository;

	//Supporting devices

	@Autowired
	private ActorService			actorService;

	@Autowired
	private Validator				validator;


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

			final String acceptedEnrolment = "Enrolment accepted \n Inscripci�n aceptada";
			final String dropoutBrotherhood = "Drop out brotherhood \n Salida de fraternidad";
			final String acceptedRequest = "A request changed its status \n Una petici�n ha cambiado su estatus";
			final String sponsorshipFee = "A sponsorship has been shown \n Se ha mostrado un anuncio";
			final Integer actors = this.actorService.findAll().size();

			if (message.getSubject().equals(sponsorshipFee) || message.getSubject().equals(acceptedRequest) || message.getSubject().equals(acceptedEnrolment) || message.getSubject().equals(dropoutBrotherhood)
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

		//final Boolean actorRole;
		//if (message.getSender() == null)
		//	actorRole = true;
		//		else if (message.getSender().equals(actor))
		//			actorRole = true;
		//else
		//	actorRole = false;

		if(message.getTags().contains("DELETED"))
			//if (!actorRole)
			//	message.getRecipients().remove(actor);
			//			else
			//				message.setSender(null);

			if (message.getRecipients().size() == 0) //&& message.getSender() == null message.getRecipients().size() == 0 &&
				this.messageRepository.delete(message);
			else
				this.messageRepository.save(message);


	}

	public void deleteAll(final Message m) {

		this.messageRepository.delete(m);

	}

	// Other methods

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

	public Double findSpamRatioByActor(final int actorID) {
		Double result = this.messageRepository.findSpamRatioByActor(actorID);
		if (result == null)
			result = 0.0;

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
		result.setPriority(message.getPriority());
		result.setRecipients(message.getRecipients());

		this.validator.validate(result, binding);

		return result;

	}
}
