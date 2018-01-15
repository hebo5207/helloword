package com.study.permission.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

@Getter
@Setter
public class TestVo {

    @NotBlank
    private String name;




}
