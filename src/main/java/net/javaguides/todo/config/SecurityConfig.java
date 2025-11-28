package net.javaguides.todo.config;

import lombok.AllArgsConstructor;
import net.javaguides.todo.security.JwtAuthenticationEntryPoint;
import net.javaguides.todo.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private UserDetailsService service;
    private JwtAuthenticationEntryPoint authEntryPoint;
    private JwtAuthenticationFilter authenticationFilter;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        // Enables basic HTTP authenticaltion where there is no CSRF and any request is authorised.
        httpSecurity.csrf((csrf) -> csrf.disable()).
                        authorizeHttpRequests(
                                (authorise) -> {
                                    // Ensures 'preflight' request requirements are met
                                    authorise.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                                    // Give anyone access to the 'auth' APIs such as user registration or login
                                    authorise.requestMatchers("/api/auth/**").permitAll();
                                    authorise.anyRequest().authenticated();
                                }).
                        httpBasic(Customizer.withDefaults());
        // Ensures the exceptions will be handled by the JwtAuthenticationEntryPoint object.
        httpSecurity.exceptionHandling(
                exception -> exception.authenticationEntryPoint(authEntryPoint));
        // Ensures that the JwtAuthenticationFilter is executed before the UsernamePasswordAuthenticationFilter.
        httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
