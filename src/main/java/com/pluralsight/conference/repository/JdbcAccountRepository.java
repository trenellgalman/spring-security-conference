package com.pluralsight.conference.repository;

import com.pluralsight.conference.model.Account;
import com.pluralsight.conference.model.ConferenceUserDetails;
import com.pluralsight.conference.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcAccountRepository implements AccountRepository {

  private final JdbcTemplate template;

  @Autowired
  public JdbcAccountRepository(JdbcTemplate template) {
    this.template = template;
  }

  @Override
  public Account create(Account account) {
    template
        .update("INSERT INTO accounts (username, password, email, firstname, lastname) VALUES " +
                "(?,?,?,?,?)", account.getUsername(),
            account.getPassword(),
            account.getEmail(),
            account.getFirstName(),
            account.getLastName());

    return account;
  }

  @Override
  public void saveToken(VerificationToken verificationToken) {
    template.update("INSERT INTO verification_tokens (username , token, expiry_date) VALUES " +
            "(?,?,?)", verificationToken.getUsername(),
        verificationToken.getToken(),
        verificationToken.calculateExpiryDate(verificationToken.EXPIRATION));

  }

  @Override
  public VerificationToken findByToken(String token) {
    return template.queryForObject(
        "select username, token, expiry_date from verification_tokens where token = ?",
        (resultSet, i) -> {
          VerificationToken rsToken = new VerificationToken();
          rsToken.setUsername(resultSet.getString("username"));
          rsToken.setToken(resultSet.getString("token"));
          rsToken.setExpiryDate(resultSet.getTimestamp("expiry_date"));
          return rsToken;
        },
        token);
  }

  @Override
  public Account findByUsername(String username) {
    return template.queryForObject("select username, firstname, lastname, " +
            "password from accounts where username = ?",
        (resultSet, i) -> {
          Account account1 = new Account();
          account1.setUsername(resultSet.getString("username"));
          account1.setFirstName(resultSet.getString("firstname"));
          account1.setLastName(resultSet.getString("lastname"));
          account1.setPassword(resultSet.getString("password"));
          return account1;
        },
        username);
  }

  @Override
  public void createUserDetails(ConferenceUserDetails userDetails) {
    template.update("INSERT INTO users (username , password, enabled) VALUES " +
            "(?,?,?)", userDetails.getUsername(),
        userDetails.getPassword(),
        1);

  }

  @Override
  public void createAuthorities(ConferenceUserDetails userDetails) {
    userDetails.getAuthorities().forEach(grantedAuthority -> template
        .update("INSERT INTO authorities(username, authority) VALUES (?, ?)",
            userDetails.getUsername(),
            grantedAuthority.getAuthority()));
  }

  @Override
  public void delete(Account account) {
    template.update("DELETE FROM accounts where username = ?", account.getUsername());
  }

  @Override
  public void deleteToken(String token) {
    template.update("DELETE FROM verification_tokens where token = ?", token);
  }
}
