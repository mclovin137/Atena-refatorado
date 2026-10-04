package br.com.atena.escolar.infra;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.springframework.context.annotation.*;
import org.springframework.web.filter.OncePerRequestFilter;
@Configuration
public class Observability {
 @Bean OncePerRequestFilter requestLogs() {return new OncePerRequestFilter(){
  final org.slf4j.Logger log=org.slf4j.LoggerFactory.getLogger("atena.http");
  @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)throws ServletException,IOException{try{chain.doFilter(request,response);}finally{Object route=request.getAttribute(org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);log.info("HTTP {} {} status={}",request.getMethod(),route==null?"sem-rota":route,response.getStatus());}}
 };}
}
