package com.barshelf.service;

import com.barshelf.entities.BarshelfUser;
import com.barshelf.repository.BarshelfUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BarshelfUserService implements UserDetailsService {
    private final BarshelfUserRepository barshelfUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BarshelfUser user = barshelfUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User details not found for the user: " + username));
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
    public String addIngredientToUserBarshelf(String email, String ingredientName) {
        try {
            // Fetch the BarshelfUser entity directly from the repository
            BarshelfUser user = barshelfUserRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (user.getIngredients().contains(ingredientName)) {
                return "Ingredient already exists in the barshelf";
            }

            // Add the ingredient to the user's barshelf
            user.getIngredients().add(ingredientName);
            barshelfUserRepository.save(user);
            return "Ingredient added successfully";
        } catch (UsernameNotFoundException ex) {
            return "User not found";
        }
    }
    public Set<String> getUserBarshelf(String email) {
        BarshelfUser user = barshelfUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getIngredients();
    }
}
