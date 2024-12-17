package com.studleague.studleague.dto.controversial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.studleague.studleague.dto.deserializers.LocalDateDeserializer;
import com.studleague.studleague.dto.result.ResultMainInfoDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for {@link com.studleague.studleague.entities.Controversial}
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ControversialDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("questionNumber")
    private int questionNumber;

    private String answer;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonProperty("issuedAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime issuedAt;

    private String status;

    private String comment;

    @JsonProperty("resolvedAt")
    private String resolvedAt;

    @JsonProperty("appealJuryComment")
    private String appealJuryComment;

    private ResultMainInfoDTO fullResult;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ControversialDTO entity = (ControversialDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.questionNumber, entity.questionNumber) &&
                Objects.equals(this.answer, entity.answer) &&
                Objects.equals(this.issuedAt, entity.issuedAt) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.comment, entity.comment) &&
                Objects.equals(this.resolvedAt, entity.resolvedAt) &&
                Objects.equals(this.appealJuryComment, entity.appealJuryComment) &&
                Objects.equals(this.fullResult, entity.fullResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, questionNumber, answer, issuedAt, status, comment, resolvedAt, appealJuryComment, fullResult);
    }


}