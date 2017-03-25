package com.mirego.cschat.controller;

import com.mirego.cschat.models.request.LoginRequest;
import com.mirego.cschat.models.User;
import com.mirego.cschat.services.CSChatService;
import com.mirego.cschat.services.StorageService;

import io.reactivex.Flowable;

public class LoginController { //TODO edit this to not authenticate

    private final CSChatService chatService;
    private final StorageService storageService;

    public LoginController(CSChatService chatService, StorageService storageService) {
        this.chatService = chatService;
        this.storageService = storageService;
    }

    public Flowable<User> login(String username, String password) {
        return chatService.login(new LoginRequest(username, password));
    }

    public void logout() {
        storageService.clearUserId();
    }

    public void saveUserId(String userId) {
        storageService.storeUserId(userId);
    }

}
