package net.javaguides.todo.service;

import net.javaguides.todo.dto.JwtAuthResponse;
import net.javaguides.todo.dto.LoginDTO;
import net.javaguides.todo.dto.RegisterDTO;

public interface AuthService {

    String register(RegisterDTO registerDTO);

    JwtAuthResponse login(LoginDTO loginDTO);
}
