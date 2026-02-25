package com.example.egestion.security;

import com.example.egestion.dto.LoginResponse;
import com.example.egestion.models.Admin;
import com.example.egestion.models.Employee;
import com.example.egestion.models.Employer;
import com.example.egestion.models.Person;
import com.example.egestion.repositories.EmployerRepository;
import com.example.egestion.repositories.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
public class SecurityConfig {

    private CorsConfigurationSource corsConfig(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedMethods(List.of("POST","GET","PATCH","DELETE"));
        corsConfiguration.setAllowedHeaders(List.of("content-type"));
        corsConfiguration.setAllowedOrigins(List.of("*"));
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**",corsConfiguration);
        return configSource;
    }
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter, LoginResponse loginResponse){
        return http
                .cors(cors ->cors.configurationSource(this.corsConfigurationSourceource()))
                .csrf(AbstractHttpConfigurer::disable)
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

                                    Map<String,String> response = loginResponse.response(
                                            "" + HttpServletResponse.SC_OK,
                                            userType,
                                            "LOGIN SUCCESSFULLY",
                                            username
                                    );
                                    ObjectMapper maper = new ObjectMapper();
                                    res.getWriter().write(maper.writeValueAsString(response));
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
                                }""");
                    }));
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public UserDetailsService userDetailsService(PersonRepository pRepo){
        return username ->(UserDetails) pRepo.findByUsername(username);
    }
    private CorsConfigurationSource corsConfigurationSourceource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("POST","GET","DELETE","PATCH"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization","content-type"));
        UrlBasedCorsConfigurationSource urlConfig = new UrlBasedCorsConfigurationSource();
        urlConfig.registerCorsConfiguration("/**",corsConfiguration);
        return urlConfig;

    }




}





