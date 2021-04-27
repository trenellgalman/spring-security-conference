package com.pluralsight.conference.service;

import com.pluralsight.conference.model.ConferenceUserDetails;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Service;

@Service
public class ConferenceUserDetailsContextMapper implements UserDetailsContextMapper {

  private static final String LOAD_USER_BY_USERNAME_QUERY = "select username, password, " +
      "enabled, nickname from users where username = ?";

  private final JdbcTemplate template;

  @Autowired
  public ConferenceUserDetailsContextMapper(JdbcTemplate template) {
    this.template = template;
  }

  @Override
  public UserDetails mapUserFromContext(DirContextOperations dirContextOperations, String s,
      Collection<? extends GrantedAuthority> collection) {
    final ConferenceUserDetails userDetails =
        new ConferenceUserDetails(dirContextOperations.getStringAttribute("uid"),
        "fake",
        Collections.emptyList());

    template.queryForObject(LOAD_USER_BY_USERNAME_QUERY, (resultSet, i) -> {
      userDetails.setNickname(resultSet.getString("nickname"));
      return userDetails;
    }, dirContextOperations.getStringAttribute("uid"));

    return userDetails;
  }

  @Override
  public void mapUserToContext(UserDetails userDetails, DirContextAdapter dirContextAdapter) {
  }
}
