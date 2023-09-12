package ra.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.security.model.domain.Users;
import ra.security.repository.IUserRepository;
import ra.security.service.IUserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<Users> findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Users save(Users users) {
        if (userRepository.existsByUsername(users.getUsername())){
            throw new RuntimeException("User is exists");
        }
        return userRepository.save(users);
    }


}
