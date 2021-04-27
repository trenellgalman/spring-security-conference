package com.pluralsight.conference.repository;

import com.pluralsight.conference.model.Password;
import com.pluralsight.conference.model.ResetToken;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcPasswordRepository implements PasswordRepository {

  private final JdbcTemplate template;

  public JdbcPasswordRepository(JdbcTemplate template) {
    this.template = template;
  }

  @Override
  public void saveToken(ResetToken resetToken) {
    template.update("INSERT INTO reset_tokens (email, username, token, expiry_date) VALUES " +
            "(?,?,?,?)", resetToken.getEmail(),
        resetToken.getUsername(),
        resetToken.getToken(),
        resetToken.calculateExpiryDate(resetToken.EXPIRATION));
  }

  @Override
  public ResetToken findByToken(String token) {
    return template.queryForObject(
        "select email, username, token, expiry_date from reset_tokens where token = ?",
        (resultSet, i) -> {
          ResetToken rsToken = new ResetToken();
          rsToken.setEmail(resultSet.getString("email"));
          rsToken.setUsername(resultSet.getString("username"));
          rsToken.setToken(resultSet.getString("token"));
          rsToken.setExpiryDate(resultSet.getTimestamp("expiry_date"));
          return rsToken;
        },
        token);
  }

  @Override
  public void update(Password password, String username) {
    template.update("UPDATE users SET password = ? WHERE username = ?", password.getPassword(),
        username);
  }
}
