package com.thuctap.book.respository;

import com.thuctap.book.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT c FROM User c WHERE c.username=?1 AND c.password=?2")
    User findByUsernameAndPassword(String username, String password);
    @Query("SELECT c FROM User c WHERE c.email = ?1")
    User findByEmail(String email);
    @Query("SELECT c FROM User c WHERE c.id_user=?1")
    User findByID(Long id_user);
    User findByUsername(String username);
    @Query("SELECT c FROM User c WHERE c.id_user=?1")
    User findUserById(Long id);

    @Query("SELECT c FROM User c")
    List<User> showAllUser();

    @Transactional
    @Modifying
    @Query("DELETE FROM User c WHERE c.id_user = ?1")
    void deleteUserByAdmin(Long id);

    public User findByResetPassToken(String resetPasswordToken);
}
