package br.com.atena.escolar.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfig {
 @Bean PasswordEncoder passwords() { return new BCryptPasswordEncoder(); }
 @Bean UserDetailsService users(PessoaRepository people) {
  return email -> { Pessoa p=people.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Credenciais inválidas"));
   return User.withUsername(p.email).password(p.senhaHash).roles(p.perfil).disabled(!p.ativo).build(); };
 }
 @Bean AuthenticationManager manager(UserDetailsService users, PasswordEncoder encoder) {
  var provider=new DaoAuthenticationProvider(users); provider.setPasswordEncoder(encoder); return new ProviderManager(provider);
 }
 @Bean SecurityFilterChain security(HttpSecurity http,PessoaRepository people) throws Exception {
  http.csrf(c->c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()).ignoringRequestMatchers("/api/internal/**"))
   .securityContext(c->c.securityContextRepository(new HttpSessionSecurityContextRepository()))
   .authorizeHttpRequests(a->a.requestMatchers("/","/index.html","/assets/**","/favicon.ico","/api/session","/api/session/login","/api/internal/**","/actuator/health").permitAll().anyRequest().authenticated())
   .exceptionHandling(e->e.authenticationEntryPoint((req,res,ex)->{res.setStatus(401);res.setContentType("application/json");res.getWriter().write("{\"message\":\"Autenticação necessária\"}");}).accessDeniedHandler((req,res,ex)->{res.setStatus(403);res.setContentType("application/json");res.getWriter().write("{\"message\":\"Acesso negado\"}");}))
   .headers(h->h.contentSecurityPolicy(c->c.policyDirectives("default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self' data:; frame-ancestors 'none'")))
   .logout(l->l.disable());
  http.addFilterBefore(new org.springframework.web.filter.OncePerRequestFilter(){
   @Override protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,jakarta.servlet.http.HttpServletResponse response,jakarta.servlet.FilterChain chain)throws jakarta.servlet.ServletException,java.io.IOException{
    var auth=org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
    if(auth!=null && auth.isAuthenticated() && !(auth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) && people.findByEmail(auth.getName()).filter(p->p.ativo).isEmpty()){if(request.getSession(false)!=null)request.getSession(false).invalidate();org.springframework.security.core.context.SecurityContextHolder.clearContext();response.setStatus(401);response.setContentType("application/json");response.getWriter().write("{\"message\":\"Conta inativa\"}");return;}chain.doFilter(request,response);
   }
  },org.springframework.security.web.access.intercept.AuthorizationFilter.class);
  return http.build();
 }
}
