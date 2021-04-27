package com.pluralsight.conference;

import javax.sql.DataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true
)
public class ConferenceSecurityConfig extends WebSecurityConfigurerAdapter implements
    ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext context) {
    this.applicationContext = context;
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    DataSource dataSource = applicationContext.getBean(DataSource.class);

    http
        .authorizeRequests()
        //.antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/anonymous*").anonymous()
        .antMatchers("/login*").permitAll()
        .antMatchers("/account*").permitAll()
        .antMatchers("/password*").permitAll()
        .antMatchers("/assets/css/**", "assets/js/**", "/images/**").permitAll()
        .antMatchers("/index*").permitAll()
        .anyRequest().authenticated()

        .and()
        .formLogin()
        .loginPage("/login")
        .loginProcessingUrl("/perform_login")
        .failureUrl("/login?error=true")
        .permitAll()
        .defaultSuccessUrl("/", true)

        .and()
        .rememberMe()
        .key("superSecretKey")
        .tokenRepository(tokenRepository(dataSource))

        .and()
        .logout()
        .logoutSuccessUrl("/login?logout=true")
        .logoutRequestMatcher(new AntPathRequestMatcher("/perform_logout", "GET"))
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .permitAll();

  }

  @Bean
  public PersistentTokenRepository tokenRepository(DataSource dataSource) {
    JdbcTokenRepositoryImpl token = new JdbcTokenRepositoryImpl();
    token.setDataSource(dataSource);
    return token;
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    DataSource dataSource = applicationContext.getBean(DataSource.class);

    // auth.inMemoryAuthentication()
    //        .withUser("bryan").password(passwordEncoder().encode("pass")).roles("USER");

    auth.jdbcAuthentication()
        .dataSource(dataSource)
        .passwordEncoder(passwordEncoder());

        /*
        auth.ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource()
                .url("ldap://localhost:8389/dc=pluralsight,dc=com")
                .and()
                .passwordCompare()
                .passwordEncoder(passwordEncoder())
                .passwordAttribute("userPassword")
                .and()
                .userDetailsContextMapper(ctxMapper);
                */

  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
