package com.pluralsight.conference.model;

import java.util.Calendar;
import java.util.Date;
import lombok.Data;

@Data
public class VerificationToken {

  public static final int EXPIRATION = 60 * 24;

  private String token;
  private String username;
  private Date expiryDate;

  public Date calculateExpiryDate(int expiryTimeInMinutes) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MINUTE, expiryTimeInMinutes);
    return cal.getTime();
  }
}
