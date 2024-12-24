package edu.du.campusflow.entity;

import edu.du.campusflow.enums.Semester;
import lombok.*;

import javax.persistence.*;
import java.time.Year;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tuition")
public class Tuition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tuition_id")
    private Long id;

    /**Depart**/
//    @ManyToOne(fetch = FetchType.LAZY)
//    @Column(name = "dept_id", nullable = false)
//    private Depart deptId;

    @Id
    @Column(name = "tui_year")
    private Year tuiYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester")
    private CommonCode semester;

    @Column(name = "amount")
    private Long amount;


} 