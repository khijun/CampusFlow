package edu.du.academic_management_system.config;


import edu.du.academic_management_system.entity.Member;
import edu.du.academic_management_system.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableWebSecurity(debug = true)
@Log4j2
@RequiredArgsConstructor
public class SecurityConfig{

    private final MemberService memberService;

    @Bean
    public UserDetailsService userDetailsService() {
        return username ->{
            log.info("사용자: " + username);
            return toUserDetails(memberService.findByMemberId(Long.parseLong(username)));
        };
    }

    public UserDetails toUserDetails(Member member){
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRole()));
        System.out.println("현재 유저의 권한: " + member.getRole());
        return User.builder()
                .username(member.getId().toString())
                .password(member.getPassword())
                .authorities(authorities)
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/js/**", "/css/**").permitAll()
                .antMatchers("/login").permitAll()
//                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();
        http.csrf().disable();
        return http.build();
    }
}
