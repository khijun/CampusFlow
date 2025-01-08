package edu.du.campusflow.utils;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationHelper {
   public static <T> Specification<T> like(String field, String value) {
      return (root, query, cb) -> value == null || value.isEmpty() ? null : cb.like(root.get(field), "%" + value + "%");
   }

   public static <T> Specification<T> equal(String field, Object value) {
      return (root, query, cb) -> value == null ? null : cb.equal(root.get(field), value);
   }
}
