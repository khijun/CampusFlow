package edu.du.campusflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "curriculumSubject")
public class CurriculumSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curriculum_subject_id")
    private Long curriculumSubjectId; // 기본키 이름

    @ManyToOne
    @JoinColumn(name = "curriculum_id")
    private Curriculum curriculum;  // Curriculum 엔티티 참조

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject; //SubjectInfo 엔티티 참조

    @ManyToOne
    @JoinColumn(name = "prereq_subject_id")
    private Subject prereqSubject; //선 수강 과목

    @ManyToOne
    @JoinColumn(name = "subject_type", referencedColumnName = "code_id")
    private CommonCode subjectType; //이수 구분

    // 성적 평가 (상대평가, 절대평가)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grading_method", referencedColumnName = "code_id")
    private CommonCode gradingMethod;

}
