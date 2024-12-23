package edu.du.academic_management_system.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "common_code")
public class CommonCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_id")
    private Long codeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private CommonCodeGroup codeGroup;

    @Column(name = "code_value", length = 100)
    private String codeValue;

    @Column(name = "code_name", length = 100)
    private String codeName;

    @Column(name = "code_description", length = 100)
    private String codeDescription;
} 