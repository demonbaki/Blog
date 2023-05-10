package it.cgmconsulting.myblog.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDate;
@Getter
public class ReasonRemoveRequest {

    @NotBlank
    @Size(max=30)
    private String reasonId;
    @NotNull @FutureOrPresent
    private LocalDate endDate;
}
