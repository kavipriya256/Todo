package kavi.example.hello.repository;

import kavi.example.hello.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    //if email is entered, we can find the user by email also (not only by id)
    Optional<User> findByEmail(String email);
}
