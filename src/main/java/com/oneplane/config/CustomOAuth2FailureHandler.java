package com.oneplane.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Component
@Slf4j
public class CustomOAuth2FailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        log.error("OAuth2 로그인 실패: {}", exception.getMessage(), exception);

        String errorMessage = determineErrorMessage(exception);
        String encodedMessage = URLEncoder.encode(errorMessage, "UTF-8");

        logFailureDetails(request, exception);

        response.sendRedirect("/login?error=" + encodedMessage);
    }


    // 예외 타입에 따른 에러 메시지 결정
    private String determineErrorMessage(AuthenticationException exception) {
        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException oauth2Exception = (OAuth2AuthenticationException) exception;
            String errorCode = oauth2Exception.getError().getErrorCode();
            String errorDescription = oauth2Exception.getError().getDescription();

            log.warn("OAuth2 인증 실패 - 코드: {}, 설명: {}", errorCode, errorDescription);

            switch (errorCode) {
                case "invalid_token":
                case "invalid_request":
                    return "카카오 로그인 토큰이 유효하지 않습니다. 다시 시도해주세요.";
                case "access_denied":
                    return "카카오 로그인 권한이 거부되었습니다. 필수 권한을 승인해주세요.";
                case "server_error":
                    return "카카오 서버에 일시적인 문제가 발생했습니다. 잠시 후 다시 시도해주세요.";
                case "temporarily_unavailable":
                    return "카카오 로그인 서비스가 일시적으로 이용할 수 없습니다.";
                default:

                    if (errorDescription != null && !errorDescription.isEmpty()) {
                        if (errorDescription.contains("email")) {
                            return "카카오 계정에서 이메일 정보를 가져올 수 없습니다. 카카오 계정 설정을 확인해주세요.";
                        }
                        if (errorDescription.contains("시퀀스")) {
                            return "시스템 설정 오류가 발생했습니다. 관리자에게 문의해주세요.";
                        }
                    }
                    return "카카오 로그인 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
            }
        }

        String message = exception.getMessage();
        if (message != null) {
            if (message.contains("시퀀스가 존재하지 않습니다")) {
                return "시스템 설정 오류가 발생했습니다. 관리자에게 문의해주세요.";
            }
            if (message.contains("이메일")) {
                return "카카오 계정에서 이메일 정보를 가져올 수 없습니다.";
            }
            if (message.contains("지원하지 않는")) {
                return "지원하지 않는 로그인 방식입니다.";
            }
        }

        return "로그인에 실패했습니다. 다시 시도해 주세요.";
    }

    private void logFailureDetails(HttpServletRequest request, AuthenticationException exception) {
        String clientIP = getClientIP(request);
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("Referer");

        log.error("OAuth2 로그인 실패 상세 정보:");
        log.error("- 클라이언트 IP: {}", clientIP);
        log.error("- User-Agent: {}", userAgent);
        log.error("- Referer: {}", referer);
        log.error("- 요청 URI: {}", request.getRequestURI());
        log.error("- 예외 타입: {}", exception.getClass().getSimpleName());
        log.error("- 예외 메시지: {}", exception.getMessage());

        if (exception.getCause() != null) {
            log.error("- 근본 원인: {}", exception.getCause().getMessage());
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }

        return request.getRemoteAddr();
    }
}