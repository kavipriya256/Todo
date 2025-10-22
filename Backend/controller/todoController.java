package kavi.example.hello.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import kavi.example.hello.models.User;
import kavi.example.hello.repository.UserRepository;
import kavi.example.hello.service.TodoService;
import kavi.example.hello.models.Todo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/todo") //localhost:8080/todo = common url todo

public class todoController {

    @Autowired
    private TodoService todoService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    // returning Todo type
    ResponseEntity<Todo> createUser(@Valid @RequestBody Todo body,Authentication auth)
    {
        try
        {
            String email = auth.getName();
            Optional<User> user= Optional.ofNullable(userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found")));
            Todo newTodo=todoService.createTodo(body,user);
            return new ResponseEntity<>(newTodo, HttpStatus.CREATED);
        }
        catch (RuntimeException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "Todo retrived sucessfully"),
            @ApiResponse(responseCode = "404", description = "Todo was not found")
    })
    @GetMapping("/get/{id}")
    ResponseEntity<Todo> getUser (@PathVariable long id)
    {
        try {
            Todo newTodo = todoService.getTodoById(id);
            return new ResponseEntity<>(newTodo, HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            log.info("error");
            log.warn("");
            log.error("",e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getall")
    ResponseEntity<List<Todo>> getTodos(Authentication auth)
    {
        Optional<User> user=userRepository.findByEmail(auth.getName());
        return new ResponseEntity<>(todoService.getallTodos(user), HttpStatus.OK);
    }

    @PutMapping("/update")
    ResponseEntity<Todo> update(@RequestBody Todo todo, Authentication auth)
    {
        try {
            String email = auth.getName();
            Optional<User> user = userRepository.findByEmail(email);
            Todo newTodo = todoService.updateTodo(todo,user);
            return new ResponseEntity<>(newTodo, HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteUser(@PathVariable long id)
    {
        try
        {
            String s=todoService.delById(id);
            return new ResponseEntity<>(s, HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            return new ResponseEntity<>("Unable to del", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getall/page")
    ResponseEntity<Page<Todo>> getallbypage(@RequestParam int pgno, @RequestParam int size)
    {
        return new ResponseEntity<>(todoService.getallPage(pgno,size), HttpStatus.OK);
    }








// localhost:8080/todo/get - url ku Todo will be executed
   /* @GetMapping("/get")
    String getTodosController()
    {
        todoService.printTodo();
        return "Todos from controller";
    }

    // localhost:8080/todo/2 - url ku Todo with ID will be executed
    // now variable id value = 2
    @GetMapping("/{id}") // id=variable
    String getTodoById(@PathVariable long id)
    { return "Todo with id "+id; }

    //request parameter in url ? id=2
    //url = todo?todoID=45
    //@GetMapping = if no endpoint given, without () also works
    @GetMapping("")
    String getById(@RequestParam("todoID") long id)
    { return "Todo with id "+id; }

    //not secured = pass is visible in url
    @GetMapping("/createUser")
    String createUsers(@RequestParam String userId, @RequestParam String pass)
    { return "Todo with username " + userId + " pass "+pass; }

    @PutMapping("/update/{id}")
    String updateParam(@PathVariable long id)
    { return "Update Todo with id "+id; }

    @DeleteMapping("/delete/{id}")
    String delParam(@PathVariable long id)
    { return "Deleted Todo with id "+id; }
    */
}

//browser (url with parameter) = usually get request
// put, post , delete = postman
