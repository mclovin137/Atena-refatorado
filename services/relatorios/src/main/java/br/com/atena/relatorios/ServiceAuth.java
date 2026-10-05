package br.com.atena.relatorios;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;
@Component
class ServiceAuth extends OncePerRequestFilter {
 final String token;
 ServiceAuth(@Value("${atena.service-token}")String token){this.token=token;if(token.length()<32)throw new IllegalArgumentException("SERVICE_TOKEN deve ter pelo menos 32 caracteres");}
 @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)throws ServletException,IOException {
  if(request.getRequestURI().equals("/actuator/health")){chain.doFilter(request,response);return;}
  String supplied=request.getHeader("X-Service-Token");if(supplied==null||!java.security.MessageDigest.isEqual(token.getBytes(java.nio.charset.StandardCharsets.UTF_8),supplied.getBytes(java.nio.charset.StandardCharsets.UTF_8))){response.setStatus(401);response.setContentType("application/json");response.getWriter().write("{\"message\":\"Credencial de serviço inválida\"}");return;}chain.doFilter(request,response);
 }
}
