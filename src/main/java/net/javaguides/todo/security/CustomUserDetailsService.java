package net.javaguides.todo.security;

import lombok.AllArgsConstructor;
import net.javaguides.todo.entity.User;
import net.javaguides.todo.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository repository;

    /**
     * Loads user details for the username value which may contain either a username or email address.
     * @param username May contain a username or a user email address.
     * @return A {@link UserDetails} object.
     * @throws UsernameNotFoundException Thrown when a {@link User user} could not be found matching the username or email address.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUserNameOrEmail(username, username).orElseThrow(() -> new UsernameNotFoundException(String.format("User with username/email address %s was not found", username)));
        Set<GrantedAuthority> authorities = user.getRoles().
                stream().
                map((role)-> new SimpleGrantedAuthority(role.getName())).
                collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), authorities);
    }
}
