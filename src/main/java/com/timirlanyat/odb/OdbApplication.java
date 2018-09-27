package com.timirlanyat.odb;

import com.samskivert.mustache.Mustache;
import com.timirlanyat.odb.dal.repositories.UserRepository;
import com.timirlanyat.odb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mustache.MustacheEnvironmentCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableAsync
public class OdbApplication {

	public static void main(String[] args) {

		SpringApplication.run(OdbApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public Mustache.Compiler mustacheCompiler(Mustache.TemplateLoader mustacheTemplateLoader,
											  Environment environment) {

		MustacheEnvironmentCollector collector = new MustacheEnvironmentCollector();
		collector.setEnvironment(environment);

		Mustache.Compiler compiler = Mustache.compiler().defaultValue("")
				.withLoader(mustacheTemplateLoader)
				.withCollector(collector);
		return compiler;

	}


	@Autowired
	private UserDetailsService customUserDetailsService;

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(customUserDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}



}
