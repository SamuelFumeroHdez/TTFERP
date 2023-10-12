package com.ttf.tallertornofumeroerp.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class ErrorResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Error> errors;
}
