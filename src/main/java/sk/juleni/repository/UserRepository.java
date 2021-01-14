package sk.juleni.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sk.juleni.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("from User where user_id=?1")
    public User findOneById(Long userId);

    @Query("from User where user_login=?1")
    public List<User> findByLogin(String login);

    @Query("from User where user_login=?1 and user_password=?2")
    public User findByUsernamePassword(String username, String password);
}
