package kavi.example.hello.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Entity
@Data
@Slf4j
public class Todo {
    @Id
    @GeneratedValue
    Long id;
   @NotBlank
   @NotNull
   @Schema(name="title",example="Complete spring boot")
    String title;
    Boolean isCompleted;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    //@Size(min=1,max=10) //min,max value
    //String description;
    //@Email
    //String email;
    //to validate phone numbers, regular expression
    //@Pattern(regexp="^[0-9]{10}$")
    //@Min(1) - min value of a field eg: num of doors
    //@Max(10) - max value
}
