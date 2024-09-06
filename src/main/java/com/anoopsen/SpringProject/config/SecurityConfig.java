package com.anoopsen.SpringProject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.anoopsen.SpringProject.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	@Autowired
    CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
				.csrf(csrf ->
					csrf.disable()
				)
				.authorizeHttpRequests(httpRequest -> 
					httpRequest
								.requestMatchers("/resources/**", "/static/**", "/images/**", "/productImages/**", "/css/**", "/js/**").permitAll()
								.requestMatchers(new AntPathRequestMatcher("/VITproject")).permitAll()
								.requestMatchers(new AntPathRequestMatcher("/VITproject/shop/**")).permitAll()
								.requestMatchers(new AntPathRequestMatcher("/VITproject/forgotPassword")).permitAll()
								.requestMatchers(new AntPathRequestMatcher("/VITproject/register")).permitAll()
								.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
								.requestMatchers(new AntPathRequestMatcher("/VITproject/admin/")).hasRole("ADMIN")
								.anyRequest().authenticated()
				)
				.formLogin(form ->
					form
							.loginPage("/VITproject/login")
							.permitAll()
							.failureUrl("/login?error=true")
							.defaultSuccessUrl("/VITproject/home")
							.usernameParameter("email")
							.passwordParameter("password")
				)
				.oauth2Login(oauthLogin -> 
					oauthLogin
								.loginPage("/VITproject/login")
								.successHandler(googleOAuth2SuccessHandler)
				)
				.logout(logout -> 
					logout
							.logoutRequestMatcher(new AntPathRequestMatcher("/VITproject/logout"))
							.logoutSuccessUrl("/VITproject/login")
							.invalidateHttpSession(true)
							.deleteCookies("JSESSIONID")
				)
				.exceptionHandling(exception ->
					exception.accessDeniedPage("/VITproject/login")
				)
				.headers(header ->
					header.frameOptions(frameOption -> frameOption.disable())
				);
		
		DefaultSecurityFilterChain filterChain = http.build();
		
		return filterChain;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	@Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
        	.userDetailsService(customUserDetailsService)
        	.passwordEncoder(passwordEncoder());
    }
}
