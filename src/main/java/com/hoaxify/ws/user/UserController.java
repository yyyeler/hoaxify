package com.hoaxify.ws.user;

import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.hoaxify.ws.error.ApiError;
import com.hoaxify.ws.shared.GenericMessage;
import com.hoaxify.ws.shared.Messages;
import com.hoaxify.ws.user.dto.UserCreate;
import com.hoaxify.ws.user.exception.ActivationNotificationException;
import com.hoaxify.ws.user.exception.NotUniqueEmailException;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class UserController {
    @Autowired
    UserService userService;
     
    @PostMapping("/api/v1/users")
    public GenericMessage createUser(@Valid @RequestBody UserCreate user) 
    {
        userService.save(user.toUser());
        String message = Messages.getMessageForLocale("hoaxify.constraint.success.message",LocaleContextHolder.getLocale());
        return new GenericMessage(message);
    }

    @GetMapping("/api/v1/getUsers")
    public String getUsers() 
    {
        // Create Gson instance
        Gson gson = new Gson();
        
        return gson.toJson(userService.getAllUsers());
    }
    

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleMethodArgNotValidEx(MethodArgumentNotValidException exception)
    {
        ApiError apiError = new ApiError();
        apiError.setPath("/api/v1/users");
        
        String message = Messages.getMessageForLocale("hoaxify.error.validation",LocaleContextHolder.getLocale());
        apiError.setMessage(message);
        apiError.setStatus(400);
        Map<String,String> validationErrors = new HashMap<>();

        for(var fieldError :
        exception.getBindingResult().getFieldErrors())
        {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        apiError.setValidationErrors(validationErrors);

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(NotUniqueEmailException.class)
    ResponseEntity<ApiError> handleNotUniqueEmailException(NotUniqueEmailException exception)
    {
        ApiError apiError = new ApiError();
        apiError.setPath("/api/v1/users");
        apiError.setMessage(exception.getMessage());
        apiError.setStatus(400);
        
        apiError.setValidationErrors(exception.getValidationErrors());

        return ResponseEntity.status(400).body(apiError);
    }

    @ExceptionHandler(ActivationNotificationException.class)
    ResponseEntity<ApiError> handleActivationNotificationException(ActivationNotificationException exception)
    {
        ApiError apiError = new ApiError();
        apiError.setPath("/api/v1/users");
        apiError.setMessage(exception.getMessage());
        apiError.setStatus(502);

        return ResponseEntity.status(502).body(apiError);
    }

    
}
