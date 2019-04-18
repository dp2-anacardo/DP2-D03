/*
 * SampleTest.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;

import utilities.AbstractTest;
import datatype.CreditCard;
import domain.Hacker;
import forms.HackerForm;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RegisterAndEditHackerTest extends AbstractTest {

	@Autowired
	private HackerService	hackerService;
	@Autowired
	private ActorService	actorService;


	/*
	 * Testing functional requirement : requirement 7.1 (hacker register)
	 * Positive:An actor who is not authenticated can register as hacker
	 * Negative:An actor who is not authenticated can not register in the system
	 * Sentence coverage: 100%
	 * Data coverage: 30%
	 */

	@Test
	public void registerHackerDriver() {
		final Object testingData[][] = {
			{
				"prueba1", "123456", "123456", "prueba1", "prueba", "", "prueba@prueba.com", "600102030", "", 21, "prueba1", "MASTERCARD", "5473259551394900", "2026/10/20", 841, null
			}, {
				"prueba1", "123456", "123456", "prueba1", "prueba", "", "prueba@prueba.com", "600102030", "", 21, "prueba1", "MASTERCARD", "5473259551394900", "", 841, ParseException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateRegisterHacker((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (int) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (String) testingData[i][12], (String) testingData[i][13], (Integer) testingData[i][14],
				(Class<?>) testingData[i][15]);
	}

	/*
	 * Testing functional requirement : requirement 8.2 (hacker edit personal data)
	 * Positive:An actor who is authenticated as Hacker can edit your personal data
	 * Negative:An actor who is authenticated as Company can not edit edit personal data of any hacker
	 * Sentence coverage: 80%
	 * Data coverage: 18%
	 */

	@Test
	public void editHackerDriver() {
		final Object testingData[][] = {
			{
				"hacker1", "newName", null
			}, {
				"company1", "newName", NullPointerException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditHacker((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------

	public void templateRegisterHacker(final String username, final String password, final String comfirmPass, final String name, final String surname, final String photo, final String email, final String phoneNumber, final String address,
		final int vatNumber, final String holderName, final String brandName, final String creditCardNumber, final String expiration, final Integer cvvCode, final Class<?> expected) {

		Class<?> caught;
		caught = null;
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {

			final Date fecha = sdf.parse(expiration);
			final HackerForm aForm = new HackerForm();

			final DataBinder binding = new DataBinder(new Hacker());

			aForm.setName(name);
			aForm.setSurname(surname);
			aForm.setUsername(username);
			aForm.setPassword(password);
			aForm.setConfirmPass(comfirmPass);
			aForm.setEmail(email);
			aForm.setAddress(address);
			aForm.setPhoneNumber(phoneNumber);
			aForm.setPhoto(photo);
			aForm.setVatNumber(vatNumber);

			final CreditCard c = new CreditCard();
			c.setBrandName(brandName);
			c.setCvv(cvvCode);
			c.setExpirationYear(fecha);
			c.setHolder(holderName);
			c.setNumber(creditCardNumber);
			final Hacker a = this.hackerService.reconstruct(aForm, binding.getBindingResult());
			a.setCreditCard(c);

			this.hackerService.save(a);

		} catch (final Throwable e) {
			caught = e.getClass();
		}
		super.checkExceptions(expected, caught);

	}

	private void templateEditHacker(final String hacker, final String newName, final Class<?> class1) {
		Class<?> caught;
		caught = null;
		try {

			super.authenticate(hacker);
			final DataBinder binding = new DataBinder(new Hacker());
			final Hacker h = this.hackerService.findOne(this.actorService.getActorLogged().getId());
			h.setName(newName);
			final Hacker result = this.hackerService.reconstruct(h, binding.getBindingResult());
			this.hackerService.save(result);

		} catch (final Exception e) {
			caught = e.getClass();
		}
		super.checkExceptions(class1, caught);
	}
}
