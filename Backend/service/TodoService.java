package kavi.example.hello.service;

import kavi.example.hello.models.Todo;
import kavi.example.hello.models.User;
import kavi.example.hello.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

   public Todo createTodo(Todo data, Optional<User> useropt)
   {
       //save = used to save , create and update
       //if that data exists already, updation happens else it inserts
       User user = useropt.orElseThrow(() -> new RuntimeException("User not found"));
       data.setUser(user);
        return todoRepository.save(data);
   }

   public Todo getTodoById(Long id)
   {
       return todoRepository.findById(id).orElseThrow(()->new RuntimeException("Todo not found"));
   }

   public String delById(Long id)
   {
       todoRepository.deleteById(id);
       return "Deleted";
       // 1st get todo from id, then if its found = delete then
   }

   public List<Todo> getallTodos(Optional<User> user)
   {
       return todoRepository.findByUser(user);
   }

   public Todo updateTodo(Todo todo,Optional<User> userOpt)
   {
       //get actual user from Optinonal
       User user = userOpt.orElseThrow(() -> new RuntimeException("User not found"));
       Todo existingTodo = todoRepository.findById(todo.getId())
               .orElseThrow(() -> new RuntimeException("Todo not found"));
       if (!existingTodo.getUser().getId().equals(user.getId()))
       {
           throw new RuntimeException("You cannot update someone else's todo");
       }
       existingTodo.setTitle(todo.getTitle());
       existingTodo.setIsCompleted(todo.getIsCompleted());
       return todoRepository.save(existingTodo);
   }

                            // page num, num of todos in a page
   public Page<Todo> getallPage(int page, int size)
   {
       Pageable pageable = PageRequest.of(page, size);
       return todoRepository.findAll(pageable);
   }
}
