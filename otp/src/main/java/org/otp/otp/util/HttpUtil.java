package org.otp.otp.util;

import jakarta.servlet.http.HttpServletRequest;
import org.otp.otp.exception.JwtTokenNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * @author Javidan Alizada
 */
@Component
public class HttpUtil {

    private static final String AUTH_HEADER = "Authorization";
    private static final String JWT_TOKEN_HEADER = "Bearer ";
    private static final int TOKEN_START_INDEX = 7;

    public HttpServletRequest getCurrentRequest() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
    }

    public String getTokenFromHeader() {
        return Optional.ofNullable(this.getCurrentRequest())
                .map(httpServletRequest -> httpServletRequest.getHeader(AUTH_HEADER))
                .filter(header -> header.startsWith(JWT_TOKEN_HEADER))
                .map(header -> header.substring(TOKEN_START_INDEX))
                .orElseThrow(JwtTokenNotFoundException::new);
    }
}
