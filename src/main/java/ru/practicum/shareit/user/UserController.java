package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validator.Marker;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;


    @GetMapping
    public ResponseEntity<Collection<UserDto>> getAll() {
        log.info("Вызов метода GET всех пользователей");
        return ResponseEntity.ok().body(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable long id) {
        log.info("Вызов GET пользователя с id={}", id);
        return ResponseEntity.ok().body(userService.getById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Validated(Marker.Create.class) UserDto user) {
        log.info("Вызов метода POST пользователя {}", user.getName());
        UserDto createdUser = userService.create(user);
        log.info("Пользователь {} с id={} создан", createdUser.getName(), createdUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> update(@PathVariable long userId,
                                          @RequestBody @Validated(Marker.Update.class) UserDto newUser) {
        log.info("Вызов метода PATCH пользователя: {}", newUser.getName());
        UserDto updatedUser = userService.update(userId, newUser);
        log.info("Пользователь {} с id={} обновлен", newUser.getName(), newUser.getId());
        return ResponseEntity.ok().body(updatedUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        log.info("Вызов метода DELETE пользователя с id={}", id);
        userService.delete(id);
    }


}