package com.example.egestion.security;

import com.example.egestion.models.Admin;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Person;
import com.example.egestion.repositories.EmployerRepository;
import com.example.egestion.repositories.PersonRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        return http.csrf(csrf ->csrf.disable())
                .authorizeHttpRequests(
                        req ->req
                                .requestMatchers("/api/login")
                                .permitAll()
                                .anyRequest().authenticated())
                .formLogin(form ->
                        form.loginProcessingUrl("/api/login")
                                .successHandler((req,res,auth)->{
                                    String userType ;
                                    String username;
                                    res.setStatus(HttpServletResponse.SC_OK);
                                    res.setContentType("application/json");
                                    Person user = (Person) auth.getPrincipal();
                                    username = user.getUsername();
                                    if(user instanceof Employee){
                                        userType = "employee";
                                    }else if(user instanceof Admin){
                                        userType = "Admin";
                                    }
                                    else userType = "employer";
                                    Map<String,String> response = new HashMap<>();
                                    response.put("status","success");
                                    response.put("message","login successful");
                                    response.put("username",username);
                                    response.put("userType",userType);
                                    res.getWriter().write(response.toString());
                                }).failureHandler((req,res,auth)->{
                                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    res.setContentType("application/json");
                                    res.getWriter().write("""
                                            {
                                                "status": "login failed",
                                                "messsage": "username or password failed"
                                            """);

                                }).permitAll()

                ).exceptionHandling(ex ->{
                    ex.authenticationEntryPoint(((request, response, authException) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.getWriter().write("""
                                {
                                   "status": "Unauthorized",
                                   "message": "login required"
                                   """);
                    }));
                }).build();
    }
    @Bean
    public UserDetailsService userDetailsService(PersonRepository pRepo){
        return username ->(UserDetails) pRepo.findByUsername(username);
    }








}
