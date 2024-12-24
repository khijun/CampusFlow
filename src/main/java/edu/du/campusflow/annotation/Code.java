package edu.du.campusflow.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Code {
    String name() default "";  // Enum 값에 대한 설명을 저장
    String description() default "";
}
