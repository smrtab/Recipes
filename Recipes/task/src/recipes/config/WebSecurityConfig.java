package recipes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import recipes.data.entity.User;
import recipes.data.repository.UserRepository;
import recipes.data.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.swing.text.html.Option;
import java.util.Optional;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private DataSource dataSource;
    private UserRepository userRepository;
    private UserService userService;

    @Autowired
    public WebSecurityConfig(
        DataSource dataSource,
        UserRepository userRepository,
        UserService userService
    ) {
        this.dataSource = dataSource;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Set permissions on endpoints
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/register").permitAll()
            .antMatchers(HttpMethod.POST, "/actuator/shutdown").permitAll()
            .anyRequest().authenticated()
            .and().httpBasic();

        http.csrf().disable();
    }


    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Expose authentication manager bean
    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}