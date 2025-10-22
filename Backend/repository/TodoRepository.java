package kavi.example.hello.repository;

import kavi.example.hello.models.Todo;
import kavi.example.hello.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


//CRUD = Create Read Update Delete
//                                            model name, id/key datatype
public interface TodoRepository extends JpaRepository<Todo, Long>
{
    List<Todo> findByUser(Optional<User> user);
}
