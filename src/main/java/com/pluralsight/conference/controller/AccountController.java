package com.pluralsight.conference.controller;

import com.pluralsight.conference.model.Account;
import com.pluralsight.conference.service.AccountService;
import com.pluralsight.conference.util.OnCreateAccountEvent;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

  private final AccountService accountService;
  private final PasswordEncoder encoder;
  private final ApplicationEventPublisher eventPublisher;

  @Autowired
  public AccountController(AccountService accountService, PasswordEncoder encoder,
      ApplicationEventPublisher eventPublisher) {
    this.accountService = accountService;
    this.encoder = encoder;
    this.eventPublisher = eventPublisher;
  }

  @GetMapping("account")
  public String getRegistration(@ModelAttribute("account") Account account) {
    return "account";
  }

  @PostMapping("account")
  public String addRegistration(@Valid @ModelAttribute("account") Account account) {

    //check for errors
    //should verify that the account and the user don't already exist
    //should verify valid email address

    //encrypt password
    account.setPassword(encoder.encode(account.getPassword()));

    //create the account
    account = accountService.create(account);

    //fire off an event on creation
    eventPublisher.publishEvent(new OnCreateAccountEvent(account, "conference_war"));
    return "redirect:account";
  }

  @GetMapping("accountConfirm")
  public String confirmAccount(@RequestParam("token") String token) {
    accountService.confirmAccount(token);

    return "accountConfirmed";
  }
}
