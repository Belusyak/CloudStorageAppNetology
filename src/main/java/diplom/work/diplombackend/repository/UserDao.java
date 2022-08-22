package diplom.work.diplombackend.repository;

import diplom.work.diplombackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserDao extends JpaRepository<Long, User> {

	Optional<User> findByLoginAndPassword(String login, String password);
}
