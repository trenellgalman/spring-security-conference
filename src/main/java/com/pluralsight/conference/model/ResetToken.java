package com.pluralsight.conference.model;

import java.util.Calendar;
import java.util.Date;
import lombok.Data;

@Data
public class ResetToken {

  public static final int EXPIRATION = 60 * 24;

  private String token;
  private String email;
  private Date expiryDate;
  private String username;

  public Date calculateExpiryDate(int expiryTimeInMinutes) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MINUTE, expiryTimeInMinutes);
    return cal.getTime();
  }
}
