package ra.security.service;

import ra.security.model.domain.Users;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<Users> findAll();
    Optional<Users> findByUserName(String username);
    Users save(Users users);

}
