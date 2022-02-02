package com.intershipjava.intershipproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerDto {
    private String email;
    private String name;
    private String password;
    private String role;
    private boolean enabled;

}
