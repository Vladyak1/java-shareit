package ru.practicum.shareit.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.practicum.shareit.validator.ValidateWhile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    private Long id;

    private String name;

    @NotBlank(groups = ValidateWhile.Create.class, message = "Email пользователя не может быть пустым")
    private String email;

}