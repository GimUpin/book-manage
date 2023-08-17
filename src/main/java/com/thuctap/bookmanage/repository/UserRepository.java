package com.thuctap.bookmanage.repository;

import com.thuctap.bookmanage.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query(value = "SELECT * FROM User u WHERE u.email=?1 AND u.password=?2", nativeQuery = true)
    User findByUsernameAndPassword(String email, String password);
    @Query(value = "SELECT * FROM User u WHERE u.email = ?1", nativeQuery = true)
    User findByEmail(String email);
    @Query(value = "SELECT * FROM User u WHERE u.id_user=?1", nativeQuery = true)
    User findByID(Long id_user);
    @Query(value = "SELECT * FROM User u WHERE u.id_user=?1", nativeQuery = true)
    User findUserById(Long id);

    @Query(value = "SELECT * FROM User u", nativeQuery = true)
    List<User> showAllUser();

    @Query(value = "SELECT * FROM user u WHERE u.email = :email", nativeQuery = true)
    Optional<User> findByUserName(@Param("email") String email);

    @Transactional
    @Modifying
    @Query(value="DELETE FROM User c WHERE c.id_user = ?1", nativeQuery = true)
    void deleteUserByAdmin(Long id);
    public User findByToken(String token);
}
