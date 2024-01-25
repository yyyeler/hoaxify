package com.hoaxify.ws.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "hoaxify")
@Configuration
public class HoaxifyProperties {
    private Email email;

    private Client client;

    public Email getEmail(){
        return email;
    }

    public Client getClient(){
        return client;
    }

    public void setEmail(Email email)
    {
        this.email = email;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public static record Email
    (
        String username,
        String password,
        String host,
        String from,
        int port
    ){}

    public static record  Client
    (
        String host
    ) 
    {}
}
