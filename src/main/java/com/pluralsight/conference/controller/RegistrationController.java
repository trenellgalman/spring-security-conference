package com.pluralsight.conference.controller;

import com.pluralsight.conference.model.Registration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@Controller
public class RegistrationController {

  @GetMapping("registration")
  public String getRegistration(@ModelAttribute("registration") Registration registration) {
    return "registration";
  }

  @PostMapping("registration")
  @Secured("ROLE_USER")
  public String addRegistration(@Valid @ModelAttribute("registration") Registration registration,
      BindingResult result,
      Authentication auth) {
    log.info("Auth: {}", auth.getPrincipal());

    if (result.hasErrors()) {
      log.info("There were errors");
      return "registration";
    }

    log.info("Registration: {}", registration.getName());

    return "redirect:registration";
  }
}
