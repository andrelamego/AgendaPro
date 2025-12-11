package com.lamego.agendapro.security;

import com.lamego.agendapro.domain.model.User;
import com.lamego.agendapro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado com e-mail: " + email));

        // Mapeia role do enum para ROLE_*
        String roleName = "ROLE_" + user.getRole().name(); // CLIENTE -> ROLE_CLIENTE

        GrantedAuthority authority = new SimpleGrantedAuthority(roleName);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getSenhaHash())
                .authorities(List.of(authority))
                .accountLocked(!user.getAtivo())
                .disabled(!user.getAtivo())
                .build();
    }
}
