package com.example.demo.configuration;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  /// define the url path to authenticate or not.
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // permit / and /home
    http.csrf().disable();
    http.authorizeRequests().antMatchers("/signuproute/**", "/signup", "/logout", "/css/**").permitAll()
        /// authorize everyother request and display the login page
        .anyRequest().authenticated()
        // formLogin returns an object you can use to configure the login page
        .and().formLogin()
        // specify the login route to go to if the login is required
        .loginPage("/login").permitAll(true)
        // configure the logout
        .and().logout().deleteCookies("remove").invalidateHttpSession(false).logoutUrl("/logout").permitAll();
  }

  @Bean
  @Override
  public UserDetailsService userDetailsService() {
    // UserDetails user =
    // User..username("spankie").password("password").roles("USER").build();

    UserDetailsService userDetails = new UserDetailsService() {

      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        com.example.demo.models.User user = com.example.demo.models.User.users.get(username);
        if (user == null) {
          throw new UsernameNotFoundException("No user");
        }
        return new UserDetails() {

          @Override
          public boolean isEnabled() {
            // TODO Auto-generated method stub
            return true;
          }

          @Override
          public boolean isCredentialsNonExpired() {
            // TODO Auto-generated method stub
            return true;
          }

          @Override
          public boolean isAccountNonLocked() {
            // TODO Auto-generated method stub
            return true;
          }

          @Override
          public boolean isAccountNonExpired() {
            // TODO Auto-generated method stub
            return true;
          }

          @Override
          public String getUsername() {
            String username = user.getUsername();
            System.out.println(username);
            return username;
          }

          @Override
          public String getPassword() {
            String password = user.getPassword();
            System.out.println(password);
            return password;
          }

          @Override
          public Collection<? extends GrantedAuthority> getAuthorities() {
            // TODO Auto-generated method stub
            return Arrays.asList();
          }
        };
      }
    };
    return userDetails;
    // InMemoryUserDetailsManager(user);
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}