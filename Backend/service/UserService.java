package kavi.example.hello.service;

import kavi.example.hello.models.Todo;
import kavi.example.hello.models.User;
import kavi.example.hello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

   public User createUser(User data)
   {
        return userRepository.save(data);
   }

   public User getUserById(Long id)
   {
       return userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
   }

}
