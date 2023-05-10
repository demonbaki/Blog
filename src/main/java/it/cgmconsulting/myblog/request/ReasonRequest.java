package it.cgmconsulting.myblog.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReasonRequest {

    @NotBlank  @Size(max=30)
    private String reasonId;
    @Min(1)
    private int severity;
    @NotNull @FutureOrPresent
    private LocalDate startDate;
}
