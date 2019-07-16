package com.hxy.actuator;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ActuatorApplication
 * @date 2019年07月16日 16:20:05
 */
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class ActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class,args);
    }

//    @Configuration
//    public static class SecurityConfig extends WebSecurityConfigurerAdapter {
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            // Page with login form is served as /login.html and does a POST on /login
//            http.formLogin().loginPage("/login.html").loginProcessingUrl("/login").permitAll();
//            // The UI does a POST on /logout on logout
//            http.logout().logoutUrl("/logout");
//            // The ui currently doesn't support csrf
//            http.csrf().disable();
//
//            // Requests for the login page and the static assets are allowed
//            http.authorizeRequests()
//                    .antMatchers("/login.html", "/**/*.css", "/img/**", "/third-party/**")
//                    .permitAll();
//            // ... and any other request needs to be authorized
//            http.authorizeRequests().antMatchers("/**").authenticated();
//
//            // Enable so that the clients can authenticate via HTTP basic for registering
//            http.httpBasic();
//        }
//    }

}
