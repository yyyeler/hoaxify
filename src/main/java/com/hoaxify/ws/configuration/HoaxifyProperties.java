package com.hoaxify.ws.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "hoaxify")
@Configuration
public class HoaxifyProperties {
    private Email email;

    private Client client;

    private Storage storage = new Storage();

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
    
    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
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

    public static class Storage
    {
        String root = "uploads";
        String profile = "profile";

        public String getRoot() {
            return root;
        }
        public void setRoot(String root) {
            this.root = root;
        }
        public String getProfile() {
            return profile;
        }
        public void setProfile(String profile) {
            this.profile = profile;
        }
    }
}
