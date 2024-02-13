package com.hoaxify.ws.user.exception;

import org.springframework.context.i18n.LocaleContextHolder;

import com.hoaxify.ws.shared.Messages;


public class AuthorizationException extends RuntimeException {
    public AuthorizationException()
    {
        super(Messages.getMessageForLocale("hoaxify.authorization.user.failure",LocaleContextHolder.getLocale()));
    }
}
