package com.app.wcc.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @author Ananth Shanmugam
 * Class to define security configuration for spring boot
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	//in memory spring security authentication - simple to implement 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER")
                .and()
                .withUser("admin").password("{noop}password").roles("USER", "ADMIN");

    }

    
    @Override
    protected void configure(HttpSecurity http) throws Exception {  
    	/* configure the matchers for spring security */
        http
            .csrf()
            .disable()
         	.authorizeRequests()
         	.anyRequest().authenticated()
             .and().httpBasic().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  
    }

    @Override
    public void configure(WebSecurity web) throws Exception {  
    	/* configure the ignore folders for spring security */
        web.ignoring()
           .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/h2-console/**");
    }

}
