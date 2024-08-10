package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.validator.ValidateWhile;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {
    @NotNull(groups = ValidateWhile.Create.class, message = "Отсутствует Id предмета для бронирования")
    private Long itemId;
    @NotNull(groups = ValidateWhile.Create.class, message = "Отсутствует время начала бронирования")
    private LocalDateTime start;
    @NotNull(groups = ValidateWhile.Create.class, message = "Отсутствует время окончания бронирования")
    private LocalDateTime end;
}
