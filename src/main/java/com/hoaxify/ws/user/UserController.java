package com.hoaxify.ws.user;

import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.ws.configuration.CurrentUser;
import com.hoaxify.ws.shared.GenericMessage;
import com.hoaxify.ws.shared.Messages;
import com.hoaxify.ws.user.dto.UserCreate;
import com.hoaxify.ws.user.dto.UserDTO;
import com.hoaxify.ws.user.dto.UserUpdate;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;




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

    @PatchMapping("/api/v1/users/{token}/active")
    GenericMessage activateUser(@PathVariable String token)
    {
        userService.activateUser(token);
        String message = Messages.getMessageForLocale("hoaxify.activate.user.success.message", LocaleContextHolder.getLocale());
        return new GenericMessage(message);
    }

    @GetMapping("/api/v1/users")
    public Page<UserDTO> getUsers(Pageable pageable, @AuthenticationPrincipal CurrentUser currentUser) 
    {
        return userService.getAllUsers(pageable,currentUser).map(UserDTO::new);
    }

    @GetMapping("/api/v1/users/{userid}")
    public UserDTO getUser(@PathVariable long userid) {
        return new UserDTO(userService.getUser(userid));
    }
    
    @PutMapping("/api/v1/users/{id}")
    @PreAuthorize("#id == principal.id")
    public UserDTO updateUser(@PathVariable long id, @Valid @RequestBody UserUpdate userUpdate)
    {
        return new UserDTO(userService.updateUser(id,userUpdate));
    }
}
