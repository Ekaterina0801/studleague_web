package com.studleague.studleague.dto.security;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIdentityInfo(scope = RoleDTO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class RoleDTO {
    private Long id;
    private String name;
}
