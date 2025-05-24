package com.saasapp.saasApp.services;

import com.saasapp.saasApp.dto.AuthRequest;

public interface LoginService {
    String register(AuthRequest request);
    String login(AuthRequest request);
}

