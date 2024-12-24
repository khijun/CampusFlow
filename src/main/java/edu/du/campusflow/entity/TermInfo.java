package edu.du.campusflow.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.Year;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "term_info")
public class TermInfo {

    @Id
    @Column(name = "tui_year")
    private Year tuiYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester")
    private CommonCode semester;
}