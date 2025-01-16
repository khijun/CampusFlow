package edu.du.campusflow.dto;

import edu.du.campusflow.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class SubjectDTO {
   private Long subjectId;
   private String subjectName;
   private String subjectDesc;
   private Integer subjectCredits;

   public static SubjectDTO fromEntity(Subject subject) {
      return SubjectDTO.builder()
          .subjectId(subject.getSubjectId())
          .subjectName(subject.getSubjectName())
          .subjectDesc(subject.getSubjectDesc())
          .subjectCredits(subject.getSubjectCredits())
          .build();
   }

   public static List<SubjectDTO> fromEntityList(List<Subject> subjects) {
      return subjects.stream().map(SubjectDTO::fromEntity).collect(Collectors.toList());
   }
}
