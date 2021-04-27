package com.pluralsight.conference.controller;

import com.pluralsight.conference.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

  @GetMapping("/user")
  public User getUser(@RequestParam(value = "firstname", defaultValue = "Bryan") String firstname,
      @RequestParam(value = "lastname", defaultValue = "Hansen") String lastname,
      @RequestParam(value = "age", defaultValue = "43") int age) {
    User user = new User();

    user.setFirstname(firstname);
    user.setLastname(lastname);
    user.setAge(age);

    return user;
  }

  @PostMapping("/user")
  public User postUser(User user) {
    log.info("User firstname: {}", user.getFirstname());

    return user;
  }
}
