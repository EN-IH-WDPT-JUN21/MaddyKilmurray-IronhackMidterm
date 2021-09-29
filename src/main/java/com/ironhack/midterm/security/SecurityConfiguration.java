package com.ironhack.midterm.security;

import com.ironhack.midterm.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin1").password(passwordEncoder().encode("admin1Pass"))
                .authorities("ROLE_ADMIN").and()
                .withUser("accholder1").password(passwordEncoder().encode("accHolder1Pass"))
                .authorities("ROLE_ACCOUNTHOLDER").and()
                .withUser("thirdparty1").password(passwordEncoder().encode("thirdparty1Pass"))
                .authorities("ROLE_THIRDPARTY");

    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.csrf().disable();
        http.authorizeRequests().mvcMatchers(HttpMethod.GET, "/users").authenticated()

                .mvcMatchers(HttpMethod.GET,"/users").authenticated()
                .mvcMatchers(HttpMethod.GET, "/users").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.GET,"/users/**").hasAnyRole("ADMIN","ACCOUNTHOLDER","THIRDPARTY")
                .mvcMatchers(HttpMethod.GET,"/users/byid/**").hasAnyRole("ADMIN","ACCOUNTHOLDER","THIRDPARTY")
                .mvcMatchers(HttpMethod.POST,"/users/new/admin").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.POST,"/users/new/accountholder").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.POST,"/users/new/thirdparty").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.PATCH,"/users/update/logindetails/**").hasAnyRole("ADMIN","ACCOUNTHOLDER","THIRDPARTY")
                .mvcMatchers(HttpMethod.PATCH,"/users/update/username/**").hasAnyRole("ADMIN","ACCOUNTHOLDER","THIRDPARTY")
                .mvcMatchers(HttpMethod.PATCH,"/users/update/password/**").hasAnyRole("ADMIN","ACCOUNTHOLDER","THIRDPARTY")
                .mvcMatchers(HttpMethod.PATCH,"/users/update/accountholder/**").hasAnyRole("ADMIN","ACCOUNTHOLDER")
                .mvcMatchers(HttpMethod.PATCH,"/users/update/thirdparty/**").hasAnyRole("ADMIN","THIRDPARTY")
                .mvcMatchers(HttpMethod.PATCH,"/users/update/admin/**").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.DELETE,"/users/remove/**").hasAnyRole("ADMIN")

                .mvcMatchers(HttpMethod.GET,"/accounts").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.GET,"/accounts/byid/**").hasAnyRole("ADMIN","ACCOUNTHOLDER","THIRDPARTY")
                .mvcMatchers(HttpMethod.POST,"/accounts/new/checking").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.POST,"/accounts/new/savings").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.POST,"/accounts/new/creditcard").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.POST,"/accounts/new/thirdparty").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.PATCH,"/accounts/update/status/**").hasAnyRole("ADMIN")

                .mvcMatchers(HttpMethod.GET,"/accounts/getbalance/checking/**").hasAnyRole("ADMIN","ACCOUNTHOLDER")
                .mvcMatchers(HttpMethod.GET,"/accounts/getbalance/studentchecking/**").hasAnyRole("ADMIN","ACCOUNTHOLDER")
                .mvcMatchers(HttpMethod.GET,"/accounts/getbalance/savings/**").hasAnyRole("ADMIN","ACCOUNTHOLDER")
                .mvcMatchers(HttpMethod.GET,"/accounts/getbalance/creditcard/**").hasAnyRole("ADMIN","ACCOUNTHOLDER")
                .mvcMatchers(HttpMethod.GET,"/accounts/getbalance/thirdparty/**").hasAnyRole("ADMIN","THIRDPARTY")
                .mvcMatchers(HttpMethod.PATCH,"/accounts/admin/transferfunds/").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.PATCH,"/accounts/accountholder/transferfunds/**").hasAnyRole("ADMIN","ACCOUNTHOLDER")
                .mvcMatchers(HttpMethod.PATCH,"/accounts/thirdparty/transferfunds/**").hasAnyRole("ADMIN","THIRDPARTY")

                .anyRequest().permitAll()
                .and().formLogin()
                .loginPage("/login").permitAll();
    }
}
