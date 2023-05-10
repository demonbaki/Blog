package it.cgmconsulting.myblog.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReportingRequest {

    @Min(1)
    private long commentId;
    @NotBlank @Size(max=30)
    private String reasonId;

}
