package com.kula.kula_project_backend.config;

import com.kula.kula_project_backend.security.JwtAuthenticationFilter;
import com.kula.kula_project_backend.security.JwtAuthorizationFilter;
import com.kula.kula_project_backend.security.JwtTokenProvider;
import com.kula.kula_project_backend.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * SecurityConfig is a configuration class that sets up security settings for the application.
 * It extends WebSecurityConfigurerAdapter which provides default security configurations
 * and allows custom security configurations on the application security context.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider tokenProvider;
    /**
     * Constructor for SecurityConfig.
     * @param userDetailsService The service that provides user details.
     * @param tokenProvider The provider that manages JSON Web Tokens.
     */
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtTokenProvider tokenProvider) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
    }
    /**
     * Configures the AuthenticationManagerBuilder with user details and password encoder.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
    /**
     * Configures the HttpSecurity by disabling CSRF protection and allowing all requests.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/api/public/**").permitAll()
               
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic()
//                .and()
//                .addFilter(jwtAuthenticationFilter())
//                .addFilter(jwtAuthorizationFilter());
                .csrf().disable() // 禁用CSRF保护
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/users/login","/users/save", "/users/sendEmail").permitAll()
                // .permitAll()
                //.antMatchers("/test/t1").hasAuthority("Admin")
                .anyRequest().authenticated() // 允许所有请求
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                //.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic();
    }
    /**
     * Creates a JwtAuthenticationFilter bean.
     * @return A JwtAuthenticationFilter instance.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager(), tokenProvider);
    }
    // /**
    //  * Creates a JwtAuthorizationFilter bean.
    //  * @return A JwtAuthorizationFilter instance.
    //  */
    // @Bean
    // public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
    //     return new JwtAuthorizationFilter(authenticationManager(), tokenProvider);
    // }
    /**
     * Creates an AuthenticationManager bean.
     * @return An AuthenticationManager instance.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    /**
     * Creates a PasswordEncoder bean.
     * @return A PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
