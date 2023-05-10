package it.cgmconsulting.myblog.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ReportingResponse {

    private long commentId;
    private String reporter;//username di colui che segnala
    private String commentAuthor;//autore del commento
    private String reasonId;//motivazione
    private String status;//stato segnalazione
    private LocalDateTime updatedAt;
}
