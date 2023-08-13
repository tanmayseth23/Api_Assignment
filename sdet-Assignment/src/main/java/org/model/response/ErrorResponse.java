package org.model.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ErrorResponse {

    public String error;
}
