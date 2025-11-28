package net.javaguides.todo.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.todo.dto.JwtAuthResponse;
import net.javaguides.todo.dto.LoginDTO;
import net.javaguides.todo.dto.RegisterDTO;
import net.javaguides.todo.entity.Role;
import net.javaguides.todo.entity.User;
import net.javaguides.todo.exception.TodoAPIException;
import net.javaguides.todo.repository.RoleRepository;
import net.javaguides.todo.repository.UserRepository;
import net.javaguides.todo.security.JwtTokenProvider;
import net.javaguides.todo.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;

    @Override
    public String register(RegisterDTO registerDTO) {
        if(userRepository.existsByUserName(registerDTO.getUserName())){
            throw new TodoAPIException(HttpStatus.BAD_REQUEST,
                    String.format("User with user name %s already exists", registerDTO.getUserName()));
        }
        if(userRepository.existsByEmail(registerDTO.getEmail())){
            throw new TodoAPIException(HttpStatus.BAD_REQUEST,
                    String.format("User with email %s already exists", registerDTO.getEmail()));
        }
        Role role = roleRepository.findByName("ROLE_USER").
                orElseThrow(()-> new TodoAPIException(
                        HttpStatus.BAD_REQUEST, String.format("Role name %s is undefined", "ROLE_USER")));
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = new User();
        user.setName(registerDTO.getName());
        user.setUserName(registerDTO.getUserName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRoles(roles);
        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public JwtAuthResponse login(LoginDTO loginDTO) {
        User user = userRepository.findByUserNameOrEmail(
                loginDTO.getUserName(), loginDTO.getUserName()).
                orElseThrow(()-> new TodoAPIException(HttpStatus.NOT_FOUND,
                String.format("User with username/email %s does not exist", loginDTO.getUserName())));
        Role role = user.getRoles().
                stream().findFirst().
                orElseThrow(
                        ()-> new TodoAPIException(HttpStatus.BAD_REQUEST,
                                String.format("Not roles exist for user %s", loginDTO.getUserName())));

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), loginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);
        return new JwtAuthResponse(token, role.getName());
    }
}
