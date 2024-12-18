package com.studleague.studleague.dto.security;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIdentityInfo(scope = UserMainInfoDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserMainInfoDTO {

    private Long id;

    private String username;

    private String fullname;

}
