package sk.juleni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.juleni.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("from User where user_id=?1")
    User findOneById(Long userId);

    @Query("from User where user_login=?1")
    List<User> findByLogin(String login);

    @Query("from User where user_login=?1 and user_password=?2")
    User findByUsernamePassword(String username, String password);
}
