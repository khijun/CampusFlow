package edu.du.campusflow.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("PROFESSOR")
public class Professor extends Member {

    @ManyToOne
    private Dept dept;

    private LocalDate hireDate;

    private LocalDate retirementDate;

}