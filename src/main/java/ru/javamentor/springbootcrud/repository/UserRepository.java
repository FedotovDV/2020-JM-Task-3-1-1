package ru.javamentor.springbootcrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javamentor.springbootcrud.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
