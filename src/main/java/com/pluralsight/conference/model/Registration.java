package com.pluralsight.conference.model;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Registration {

  @NotEmpty
  private String name;
}
