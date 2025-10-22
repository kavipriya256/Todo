package kavi.example.hello.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor     //create user when no argument is given
@AllArgsConstructor    //create when all arguments are given
@Builder
//in postgres, by default it has a table called user, so to avoid confict,we use this: for DB table name=UserTable, for java usage here, we can use User
@Table(name="UserTable")
public class User {
    @Id
    @GeneratedValue
    Long id;
    @Email
    String email;
    String password;
}
