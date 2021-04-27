package com.pluralsight.conference.service;

import com.pluralsight.conference.model.Password;
import com.pluralsight.conference.model.ResetToken;
import com.pluralsight.conference.repository.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

  private final PasswordRepository passwordRepository;

  @Autowired
  public PasswordServiceImpl(PasswordRepository passwordRepository) {
    this.passwordRepository = passwordRepository;
  }

  @Override
  public void createResetToken(Password password, String token) {
    ResetToken resetToken = new ResetToken();
    resetToken.setToken(token);
    resetToken.setEmail(password.getEmail());
    resetToken.setUsername(password.getUsername());

    passwordRepository.saveToken(resetToken);
  }

  @Override
  public boolean confirmResetToken(ResetToken token) {
    return false;
  }

  @Override
  public void update(Password password, String username) {
    passwordRepository.update(password, username);
  }
}
