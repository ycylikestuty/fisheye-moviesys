package com.gdesign.fisheyemoviesys.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author ycy
 */
public class CaptchaException extends AuthenticationException {
    public CaptchaException(String msg) {
        super(msg);
    }
}
