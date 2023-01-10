package com.example.iPrueba;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class IPruebaApplication  {

	
	
	@GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal, @CookieValue(name="JSESSIONID") String session) {
		if(principal!= null) {
			Map<String,Object> mapa;
			mapa = new HashMap<String, Object>();
			mapa.put("accountAuthentication", principal.getAttribute("id"));
			mapa.put("email", principal.getAttribute("email"));
			mapa.put("name", principal.getAttribute("name"));
			mapa.put("lastname", "");
			mapa.put("age", 0);
			mapa.put("avatar_url", principal.getAttribute("avatar_url"));
			mapa.put("string", principal.getAttributes().toString());
			

			return mapa;
		}
		return null;
    }
	
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.authorizeRequests(a -> a
			.antMatchers("/login.html", "/error", "/webjars/**", "/api/**", "/css/**", "/img/**").permitAll()
			.anyRequest().authenticated()
		)
		.exceptionHandling(e -> e
			.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
		)
		.csrf().disable().cors().and()
		.logout(l -> l
			.logoutSuccessUrl("/login.html")
		)
		.oauth2Login();
        return http.build();
    }
	
	public static void main(String[] args) {
		SpringApplication.run(IPruebaApplication.class, args);
	}
}
