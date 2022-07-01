package jonathan_coutinho.CodingChallenge.security;

import jonathan_coutinho.CodingChallenge.domain.UserRole;
import jonathan_coutinho.CodingChallenge.service.AccountCacheConfig;
import jonathan_coutinho.CodingChallenge.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Value("${sdt.security.jwt.secret}")
    private String secret;

    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final AccountCacheConfig accountCacheConfig;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(new JWTAuthorizationFilter(secret), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new JwtCredentialsAuthenticationFilter(authenticationManager(),accountCacheConfig))
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeRequests()
                .antMatchers("/login/**","/signup/**","/refresh_token/**").permitAll()
                .antMatchers(HttpMethod.GET,"/comment/**").permitAll()
                .antMatchers("/comment/**").hasAnyRole(UserRole.BASICO.name(),UserRole.AVANCADO.name(),UserRole.MODERADOR.name())
                .anyRequest().authenticated().and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler());

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/swagger-ui/**","/v3/api-docs/**");
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider =new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(authenticationService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }


}
