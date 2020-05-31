package com.ang.reptile.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "login-checker.down-stream")
public class DownStreamLoginChecker {
    private Login login;

    private KeyWorlds keyWorlds;

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public KeyWorlds getKeyWorlds() {
        return keyWorlds;
    }

    public void setKeyWorlds(KeyWorlds keyWorlds) {
        this.keyWorlds = keyWorlds;
    }
}
