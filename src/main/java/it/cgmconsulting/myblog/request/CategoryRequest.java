package it.cgmconsulting.myblog.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CategoryRequest {

    @NotBlank @Size(max = 50)
    private String categoryName;
}
