package com.kula.kula_project_backend.config;

import com.kula.kula_project_backend.common.PermissionDefinition;
import com.kula.kula_project_backend.dao.RoleRepository;
//import com.kula.kula_project_backend.entity.JwtAuthorizationFilter;
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
import org.springframework.http.HttpMethod;

/**
 * SecurityConfig is a configuration class that sets up security settings for
 * the application.
 * It extends WebSecurityConfigurerAdapter which provides default security
 * configurations
 * and allows custom security configurations on the application security
 * context.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsService userDetailsService;
    @Autowired
    private final JwtTokenProvider tokenProvider;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Constructor for SecurityConfig.
     * 
     * @param userDetailsService The service that provides user details.
     * @param tokenProvider      The provider that manages JSON Web Tokens.
     * @param roleRepository
     */
    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtTokenProvider tokenProvider,
            RoleRepository roleRepository) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.roleRepository = roleRepository;
    }

    /**
     * Configures the AuthenticationManagerBuilder with user details and password
     * encoder.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * Configures the HttpSecurity by disabling CSRF protection and allowing all
     * requests.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()  // Use WebMvcConfig CORS
                .and()
                .csrf().disable() // 禁用CSRF保护
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()

                // .anyRequest().authenticated()
                // most GET requests can access without token

                .antMatchers(HttpMethod.GET,
                        "/bookmarker/getBookMarksByUserId/**",
                        "/bookmarker/getBookMarksById/**",
                        "/comments/getAllPostsComments/**",
                        "/followingGroups/**",
                        "/followingGroups/getFollowingsByOwnerId/**",
                        "/followingGroups/getFollowers/**",
                        "/likes/getLikesByPostId/**",
                        "/followingGroups/**",
                        "/followingGroups/getFollowingsByOwnerId/**",
                        "/followingGroups/getFollowers/**",
                        "/likes/getLikesByPostId/**",
                        "/dishes/names",
                        "/restaurants/names",
                        "/users/getBookmarks/**").authenticated()

                // .antMatchers(HttpMethod.GET,
                // "/api/v1/influencer/**",
                // ""

                // ).hasAuthority("INFLUENCER") // INFLUENCER and higher roles
                // .antMatchers(HttpMethod.GET,
                // "/api/v1/manager/**",
                // ""
                // ).hasAuthority("RESTAURANT_MANAGER") // RESTAURANT_MANAGER and higher roles
                .antMatchers(HttpMethod.GET,
                        "/dishes/all",
                        "/users/getAll",
                        "/restaurants/all"
                        // ).hasAnyAuthority(
                        // "KULAA_STUFF", "ADMIN")// KULAA_STUFF and higher roles
                        ).authenticated()

                .antMatchers(HttpMethod.GET,
                        "/files/getImageURIFromServer/**").authenticated()

                // .antMatchers(
                // HttpMethod.GET,
                // "/files/getImageURIFromServer/**",
                // "/bookmarker/getBookMarksByUserId/**",
                // "/bookmarker/getBookMarksById/**",
                // "/comments/getAllPostsComments/**",
                // "/dishes/all",
                // "/followingGroups/**",
                // "/followingGroups/getFollowingsByOwnerId/**",
                // "/followingGroups/getFollowers/**",
                // "/likes/getLikesByPostId/**",
                // "/users/getAll",
                // "/users/getBookmarks/**")
                // .authenticated()

                // most POST requests must access with token

                .antMatchers(
                        "/files/upload",
                        "/files/uploadMultiple",
                        "/bookmarker/save/**",
                        "/bookmarker/addPost",
                        "/bookmarker/deletePost",
                        "/comments/save",
                        "/followingGroups/save",
                        "/followingGroups/moreFollowings",
                        "/followingGroups/deleteFollowings",
                        "/likes/save",
                        "/posts/save",
                        "/posts/update",
                        "/profiles/save",
                        "/profiles/updateBio",
                        "/profiles/updateProfileImage",
                        "/profiles/updatePrivateProfile",
                        "/sharedPostLists/saveSharedPostLists",
                        "/tags/save",
                        "/tags/getNamesByIds",
                        "/tags/findOrCreateTag",
                        "/tags/getOrCreateTags",
                        "/users/update",
                        "/users/listByParams",
                        "/users/assignProfile"

                // ).hasAnyAuthority(
                //         "CUSTOMER", "INFLUENCER", "RESTAURANT_MANAGER", "KULAA_STUFF", "ADMIN") // CUSTOMER and higher
                //
                ).authenticated()                                                                      // roles

                // .antMatchers(HttpMethod.POST,
                // "",
                // ""
                // ).hasAuthority("INFLUENCER") // INFLUENCER and higher roles

                .antMatchers(HttpMethod.POST,
                        "/dishes/save",
                        "/dishes/uploadImages/**",
                        "/offers/uploadImage/**",
                        "/offers/save",
                        "/restaurants/save",
                        "/restaurants/uploadLogo/**",
                        "/restaurants/uploadImages/**").authenticated()

                .antMatchers(HttpMethod.POST,
                        "/profiles/updateProfileLevels",
                        "/profiles/updateProfilePoints",
                        "/profiles/gainProfilePoints").authenticated()

                .antMatchers(HttpMethod.POST,
                        "/areas/save",
                        "/files/updateImageToServer",
                        "/diets/save",
                        "/diets/findOrCreateDiet",
                        "/regions/save").authenticated()

                // .antMatchers(
                // HttpMethod.POST,
                // "/users/login",
                // "/users/save",
                // "/users/sendEmail",
                // "/users/sendSMSVerificationCode",
                // "/users/verifySMSCode")
                // .permitAll()

                // all PUT and DELETE need token

                .antMatchers(
                        "/comments/deleteComment/**",
                        "/posts/deletePost/**").authenticated()

                // .antMatchers("").hasAuthority("INFLUENCER") // INFLUENCER and higher roles
                .antMatchers(
                        "/dishes/update",
                        "/dishes/delete/**",
                        "/dishes/deleteImages/**",
                        "/offers/deleteImage/**",
                        "/offers/update",
                        "/offers/delete/**",
                        "/restaurants/update",
                        "/restaurants/deleteLogo/**",
                        "/restaurants/deleteImages/").authenticated()

                .antMatchers(
                        "/restaurants/delete/**",
                        "/tags/deleteTag/**"
                //         )
                // .hasAnyAuthority(
                //         "KULAA_STUFF", "ADMIN") // KULAA_STUFF and higher roles
                ).authenticated()


                .antMatchers(
                        "/areas/update",
                        "/areas/delete/**",
                        "/diets/deleteDiet/**",
                        "/regions/update",
                        "/regions/delete/**"
                //         )
                // .hasAnyAuthority(
                //         "ADMIN")// only ADMIN
                ).authenticated()

                .antMatchers(HttpMethod.PUT, "/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/**").authenticated()
                .antMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-ui",
                        "swagger-ui/index.html",
                        "/api-docs/**",
                        "/webjars/**")
                .permitAll()

                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/**").permitAll()
                .antMatchers("/users/login", "/users/save", "/users/sendEmail","/users/verifyEmailCode").permitAll()

                // .permitAll()
                // .antMatchers("/test/t1").hasAuthority("Admin")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic();
    }

    /**
     * Creates a JwtAuthenticationFilter bean.
     * 
     * @return A JwtAuthenticationFilter instance.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager(), tokenProvider);
    }

    // /**
    // * Creates a JwtAuthorizationFilter bean.
    // * @return A JwtAuthorizationFilter instance.
    // */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManager(), tokenProvider);
    }

    /**
     * Creates an AuthenticationManager bean.
     * 
     * @return An AuthenticationManager instance.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Creates a PasswordEncoder bean.
     * 
     * @return A PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
