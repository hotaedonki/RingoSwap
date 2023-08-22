package net.ringo.ringoSwap.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security 설정
 */
@Configuration
public class WebSecurityConfig {
    @Autowired
    private DataSource dataSource;

    //설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
        .authorizeRequests()
        .antMatchers(
        		"/",
        		"/taeho/**",
        		"/bootstrap/**",
        		"/member/emailConfirm",
        		"/member/join",
        		"/member/idcheck",
<<<<<<< HEAD
        		"/board/main",
        		"/board/read",
        		"/chat/chatMain",
        		"/feed/feedMain",
        		"/game/gameMain",
                "/img/**",
=======
        		"/chat/**",
        		"/feed/**",
        		"/game/**",
                "/live/**",
                "/note/**",
                "/image/**",
>>>>>>> 0970a4147bc987b2fdf6c2514efb966dab194da1
                "/css/**",
                "/js/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()					
        .loginPage("/member/login")	
        .loginProcessingUrl("/member/login").permitAll()
        .usernameParameter("memberid")
        .passwordParameter("memberpw")
        .and()
        .logout()
        .logoutUrl("/member/logout")
        .logoutSuccessUrl("/").permitAll()
        .and()
        .cors()
        .and()
        .httpBasic();

        return http.build();
    }

    //인증을 위한 쿼리
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
        .dataSource(dataSource)
        // 인증 (로그인)
        .usersByUsernameQuery(
        		"select memberid username, memberpw password, enabled " +
                "from market_member " +
                "where memberid = ?")
        // 권한
        .authoritiesByUsernameQuery(
        		"select memberid username, rolename role_name " +
                "from market_member " +
                "where memberid = ?");
    }

    // 단방향 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
