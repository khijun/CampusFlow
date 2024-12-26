package edu.du.campusflow.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("STUDENT")
public class Student extends Member {

    @ManyToOne
    private Dept dept;

    private LocalDate admissionDate;

    private LocalDate graduationDate;

    private Integer grade;

   @ManyToOne
   @JoinColumn(name = "academic_status")
   private CommonCode academicStatus;
}