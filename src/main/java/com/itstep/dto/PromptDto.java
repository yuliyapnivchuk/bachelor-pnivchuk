package com.itstep.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PromptDto {
    private Integer eventId;
    private String user;
    private String prompt;
}
