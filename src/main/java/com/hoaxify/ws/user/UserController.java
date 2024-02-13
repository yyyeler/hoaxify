package com.hoaxify.ws.user;

import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.ws.auth.token.TokenService;
import com.hoaxify.ws.shared.GenericMessage;
import com.hoaxify.ws.shared.Messages;
import com.hoaxify.ws.user.dto.UserCreate;
import com.hoaxify.ws.user.dto.UserDTO;
import com.hoaxify.ws.user.dto.UserUpdate;
import com.hoaxify.ws.user.exception.AuthorizationException;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;
     
    @PostMapping("/api/v1/users")
    public GenericMessage createUser(@Valid @RequestBody UserCreate user) 
    {
        userService.save(user.toUser());
        String message = Messages.getMessageForLocale("hoaxify.constraint.success.message",LocaleContextHolder.getLocale());
        return new GenericMessage(message);
    }

    @PatchMapping("/api/v1/users/{token}/active")
    GenericMessage activateUser(@PathVariable String token)
    {
        userService.activateUser(token);
        String message = Messages.getMessageForLocale("hoaxify.activate.user.success.message", LocaleContextHolder.getLocale());
        return new GenericMessage(message);
    }

    @GetMapping("/api/v1/userList")
    public Page<UserDTO> getUsers(Pageable pageable, @RequestHeader(name = "Authorization", required = false) String authorizationHeader) 
    {
        var loggedInUser = tokenService.verifyToken(authorizationHeader);
        return userService.getAllUsers(pageable,loggedInUser).map(UserDTO::new);
    }

    @GetMapping("/api/v1/users/{userid}")
    public UserDTO getUser(@PathVariable long userid) {
        return new UserDTO(userService.getUser(userid));
    }
    
    @PutMapping("/api/v1/users/{id}")
    public UserDTO updateUser(@PathVariable long id, @Valid @RequestBody UserUpdate userUpdate, @RequestHeader(name = "Authorization", required = false) String authorizationHeader)
    {
        var loggedInUser = tokenService.verifyToken(authorizationHeader); 
        if(loggedInUser == null || loggedInUser.getId() != id)
        {
            throw new AuthorizationException();
        }
        return new UserDTO(userService.updateUser(id,userUpdate));
    }
}
