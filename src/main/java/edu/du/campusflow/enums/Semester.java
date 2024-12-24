package edu.du.campusflow.enums;

import edu.du.campusflow.annotation.Code;

@Code(name="학기", description = "학기를 나타내는 코드")
public enum Semester {
    @Code(name="1학기", description = "1학기")
    FIRST_SEMESTER,
    
    @Code(name="2학기", description = "2학기") 
    SECOND_SEMESTER,
    
    @Code(name="여름학기", description = "여름계절학기")
    SUMMER,
    
    @Code(name="겨울학기", description = "겨울계절학기")
    WINTER;
} 