/*
 * WelcomeController.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.AdministratorService;
import services.CompanyService;
import services.HackerService;
import domain.Actor;
import domain.Administrator;
import domain.Company;
import domain.Hacker;

@Controller
@RequestMapping("/profile")
public class ProfileController extends AbstractController {

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private HackerService			hackerService;

	@Autowired
	private CompanyService			companyService;


	@RequestMapping(value = "/myInformation", method = RequestMethod.GET)
	public ModelAndView myInformation() {
		final ModelAndView result = new ModelAndView("profile/myInformation");
		final Actor user = this.actorService.getActorLogged();
		final UserAccount userAccount = LoginService.getPrincipal();

		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("HACKER")) {
			Hacker hacker;
			hacker = this.hackerService.findOne(user.getId());
			Assert.notNull(hacker);
			result.addObject("hacker", hacker);
		}

		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN")) {
			Administrator administrador1;
			administrador1 = this.administratorService.findOne(user.getId());
			Assert.notNull(administrador1);
			result.addObject("administrator", administrador1);
		}

		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("COMPANY")) {
			Company company;
			company = this.companyService.findOne(user.getId());
			Assert.notNull(company);
			result.addObject("company", company);
		}
		return result;
	}

	@RequestMapping(value = "/exportJSON", method = RequestMethod.GET)
	public ModelAndView exportJSON() {
		final ModelAndView result = new ModelAndView("profile/exportJSON");
		final Actor user = this.actorService.getActorLogged();
		final UserAccount userAccount = LoginService.getPrincipal();
		final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("ADMIN")) {
			Administrator administrador1;
			administrador1 = this.administratorService.findOne(user.getId());
			Assert.notNull(administrador1);
			final String json = gson.toJson(administrador1);
			result.addObject("json", json);
		}

		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("HACKER")) {
			Hacker hacker;
			hacker = this.hackerService.findOne(user.getId());
			Assert.notNull(hacker);
			final String json = gson.toJson(hacker);
			result.addObject("json", json);
		}

		if (userAccount.getAuthorities().iterator().next().getAuthority().equals("COMPANY")) {
			Company member;
			member = this.companyService.findOne(user.getId());
			Assert.notNull(member);
			final String json = gson.toJson(member);
			result.addObject("json", json);
		}
		return result;

	}
}
