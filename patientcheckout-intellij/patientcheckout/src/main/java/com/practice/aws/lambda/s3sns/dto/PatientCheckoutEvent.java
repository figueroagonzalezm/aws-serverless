package com.practice.aws.lambda.s3sns.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientCheckoutEvent {
    private String firstName;
    private String middleName;
    private String lastName;
    public String ssn;
}
