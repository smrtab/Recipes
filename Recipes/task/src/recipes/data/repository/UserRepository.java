package recipes.data.repository;

import org.springframework.data.repository.CrudRepository;
import recipes.data.entity.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
