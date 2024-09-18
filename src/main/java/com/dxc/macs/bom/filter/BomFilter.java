package com.dxc.macs.bom.filter;

import com.dxc.macs.bom.constants.BomConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Component
@Order(1)
public class BomFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(BomFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String authHeader = request.getHeader(BomConstants.AUTHORIZATION);
        final String authType = request.getHeader(BomConstants.AUTH_TYPE);
        // Allow access to Actuator health check without token
        if (request.getRequestURI().contains("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (BomConstants.OPTIONS.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
        } else {
            if(authHeader == null || !authHeader.startsWith(BomConstants.BEARER)){
                throw new ServletException("An exception occurred");
            }
        }
        if (authHeader != null) {
            final String token = authHeader.substring(7);
            Claims claims;
            if (authType == null) {
                claims = Jwts.parser().setSigningKey(BomConstants.SECRET).parseClaimsJws(token).getBody();
            } else {
                claims = new DefaultClaims(getClaims(token));
            }
            request.setAttribute(BomConstants.CLAIMS, claims);
            request.setAttribute(BomConstants.BLOG, servletRequest.getParameter(BomConstants.ID));
        } else {
            logger.info("Authorization header is null for this request");
        }
        response.setHeader(BomConstants.ACCESS_CONTROL_ALLOW_ORIGIN, BomConstants.PERMIT_ALL);
        response.setHeader(BomConstants.ACCESS_CONTROL_EXPOSE_HEADERS, BomConstants.CONTENT_DISPOSITION);
        response.setHeader(BomConstants.ACCESS_CONTROL_ALLOW_METHODS, BomConstants.ALL_REQUEST_METHODS);
        response.setHeader(BomConstants.ACCESS_CONTROL_ALLOW_HEADERS, BomConstants.PERMIT_ALL);
        response.setHeader(BomConstants.ACCESS_CONTROL_MAX_AGE, BomConstants.MAX_AGE_VALUE);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public String extractUsername(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(BomConstants.SECRET).parseClaimsJws(token).getBody();
            return claims.getSubject();
        }catch (Exception exception) {
            return getClaims(token).get("name").toString();
        }
    }

    private Map<String, Object> getClaims(String token) {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(
                new String(decoder.decode(chunks[1])), Map.class);
        } catch (JsonProcessingException e) {
            logger.info("Error in header");
        }
        return null;
    }
}
