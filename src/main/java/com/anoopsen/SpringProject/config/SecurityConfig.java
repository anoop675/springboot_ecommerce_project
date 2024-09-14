package com.anoopsen.SpringProject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.anoopsen.SpringProject.service.CustomUserDetailsService;

@Configuration
@PropertySource("classpath:application.properties")  //this will allow spring security to locate the private key mentioned in application.properties for the below @Value
@EnableWebSecurity
public class SecurityConfig{
	
	@Value("${rememberMe.privateKey}")
	private String rememberMePrivateKey; //Use @Value to load the Remember-Me private key from a configuration file instead of hard coding it in the code
	
	@Autowired
	@Lazy
    CustomUserDetailsService customUserDetailsService;
	
	//@Autowired
	//GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;
	@Bean
    public AuthenticationSuccessHandler oAuth2SuccessHandler() {
        return new GoogleOAuth2SuccessHandler();
    }
	@Autowired
	PasswordEncoderConfig pwdEncoder;
	
	@Autowired
	CustomAuthenticationSuccessHandler authHandler;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
				.csrf(csrf ->
					csrf.disable()
				)
				.authorizeHttpRequests(httpRequest -> 
					httpRequest
								.requestMatchers("/resources/**", "/static/**", "/images/**", "/productImages/**", "/css/**", "/js/**",
										"/lottie.host/7401522f-2d8b-4049-ad18-eb0edb6af224/CE9lFrNlEH.json").permitAll()
								.requestMatchers(new AntPathRequestMatcher("/VITproject")).permitAll()
								//.requestMatchers(new AntPathRequestMatcher("/VITproject/shop/**")).permitAll()
								.requestMatchers(new AntPathRequestMatcher("/VITproject/forgotPassword")).permitAll()
								.requestMatchers(new AntPathRequestMatcher("/VITproject/register")).permitAll()
								.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
								.requestMatchers(new AntPathRequestMatcher("/oauth2/authorization/google")).permitAll()
								.requestMatchers(new AntPathRequestMatcher("/VITproject/admin/**")).hasRole("ADMIN")
								.anyRequest().authenticated()
				)
				.formLogin(form ->
					form
							.loginPage("/VITproject/login")
							.permitAll()
							.successHandler(authHandler) // custom authentication success handler is set
							//.defaultSuccessUrl("/VITproject/home")
							.failureUrl("/VITproject/login?error=true")
							.usernameParameter("email")
							.passwordParameter("password")
				)
				.oauth2Login(oauthLogin -> 
					oauthLogin
								.loginPage("/VITproject/login")
								.successHandler(this.oAuth2SuccessHandler())
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
				.rememberMe(
						(rememberMe) -> rememberMe
										.rememberMeServices(this.rememberMeServices())
										//.rememberMeServices(rememberMeServices)   //calls the in-built Spring Security Bean for rememberMeServices
				)
				.headers(header ->
					header.frameOptions(frameOption -> frameOption.disable())
				);
		
		DefaultSecurityFilterChain filterChain = http.build();
		
		return filterChain;
	}
	
	/*@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}*/
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {    //This bean is used to notify the Spring Security session registry, if the session is created or destroyed
	    return new HttpSessionEventPublisher();                       //This is important for creation of multiple concurrent sessions
	}
	
	@Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
        	.userDetailsService(customUserDetailsService)
        	.passwordEncoder(pwdEncoder.passwordEncoder());
    }
	
final String rememberMeCookieName = "remember-me";
	
	@Bean
	public RememberMeAuthenticationFilter rememberMeFilter(AuthenticationManager authenticationManager) throws Exception {
	    RememberMeAuthenticationFilter rememberMeFilter = new RememberMeAuthenticationFilter(
	            authenticationManager,
	            this.rememberMeServices()
	    );
	    
	    return rememberMeFilter;
	}
	
	
	@Bean
	public RememberMeServices rememberMeServices() {
		
		RememberMeTokenAlgorithm encodingAlgorithm = RememberMeTokenAlgorithm.SHA256;
		
		
		TokenBasedRememberMeServices rememberMe = //Ctrl + SHift + t, and search for TokenBasedRememberMeServices
				new TokenBasedRememberMeServices(
						rememberMePrivateKey,
						customUserDetailsService, 
						encodingAlgorithm
					);
		
		rememberMe.setMatchingAlgorithm(RememberMeTokenAlgorithm.MD5);
		rememberMe.setParameter("remember-me");  //if the checkbox "remember-me" from loginPage.jsp is checked then, the remember-me functionality is used
		rememberMe.setAlwaysRemember(false);    //we don't want user sessions to be remembered, even if the remember-me checkbox is unchecked in loginPage.jsp
		rememberMe.setCookieName(rememberMeCookieName);
		rememberMe.setCookieDomain("localhost");
	    rememberMe.setTokenValiditySeconds(60 * 1);  //1 minute
	    
		return rememberMe;
	}
	
	@Bean
	public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
	    RememberMeAuthenticationProvider rememberMeAuthenticationProvider 
	    					= new RememberMeAuthenticationProvider(rememberMePrivateKey);
	    
	    return rememberMeAuthenticationProvider;
	}
}
