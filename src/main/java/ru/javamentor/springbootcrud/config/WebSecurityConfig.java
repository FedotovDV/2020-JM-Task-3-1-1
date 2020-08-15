package ru.javamentor.springbootcrud.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.javamentor.springbootcrud.config.handler.LoginSuccessHandler;
import ru.javamentor.springbootcrud.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {



    private final UserService userService; // сервис, с помощью которого тащим пользователя
    private final LoginSuccessHandler loginSuccessHandler; // класс, в котором описана логика перенаправления пользователей по ролям
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(UserService userService, LoginSuccessHandler loginSuccessHandler, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.loginSuccessHandler = loginSuccessHandler;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/registration", "/login").anonymous()
                .antMatchers("/user","/user/*").authenticated()
                .antMatchers("/admin", "/admin/*").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()
                .and().csrf().disable()
                .formLogin()
                .loginPage("/login")
//                .successHandler(new LoginSuccessHandler())
                .loginProcessingUrl("/login/process")
                .usernameParameter("email")
                .failureUrl("/login?error=true")
                .successHandler(loginSuccessHandler)
                .and()
                .exceptionHandling()
                .accessDeniedPage("/user")
                .and()
                .logout()
                .permitAll()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }




}