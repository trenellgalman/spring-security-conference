package com.pluralsight.conference.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Password {

  @NotNull
  @NotEmpty
  //TODO: Figure out where this is being used and rename it
  private String password;
  private String matchingPassword;

  @NotNull
  @NotEmpty
  private String email;

  @NotNull
  @NotEmpty
  private String username;
  private String token;
}
