package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validator.ValidateWhile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDto {
    @NotNull(groups = ValidateWhile.Create.class, message = "Отсутствует Id предмета для бронирования")
    private Long itemId;
    @NotNull(groups = ValidateWhile.Create.class, message = "Отсутствует время начала бронирования")
    private LocalDateTime start;
    @NotNull(groups = ValidateWhile.Create.class, message = "Отсутствует время окончания бронирования")
    private LocalDateTime end;
}
