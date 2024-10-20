package com.example.jvtmicroservicejava.servise;


import com.example.jvtmicroservicejava.entity.User;
import com.example.jvtmicroservicejava.exeption.UserAlreadyExistsException;
import com.example.jvtmicroservicejava.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void registerNewUser(String username, String password) {
    if (userRepository.findByUsername(username).isPresent()) {
      throw new UserAlreadyExistsException("User already exists with username: " + username);
    }

    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setEnabled(true);
    userRepository.save(user);
  }
}
