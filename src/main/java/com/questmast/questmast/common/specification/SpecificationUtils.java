package com.questmast.questmast.common.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtils {

    private SpecificationUtils() {
        // Private constructor to prevent instantiation
    }

    public static String insertRightWildcard(String value) {
        return value == null ? null : value.concat("%");
    }

    public static String insertLeftWildcard(String value) {
        return value == null ? null : "%".concat(value);
    }

    public static String surroundByWildcard(String value) {
        return value == null ? null : "%".concat(value).concat("%");
    }

    public static <T extends Comparable<T>> Search<T> generateEqualsCriteria(String key, T value) {
        return new Search<>(key, ":", value);
    }

    public static Search<String> generateRightLikeCriteria(String key, String value) {
        return new Search<>(key, ":", insertRightWildcard(value));
    }

    public static Search<String> generateLeftLikeCriteria(String key, String value) {
        return new Search<>(key, ":", insertLeftWildcard(value));
    }

    public static Search<String> generateInnerLikeCriteria(String key, String value) {
        return new Search<>(key, ":", surroundByWildcard(value));
    }

    public static <T extends Comparable<T>> Search<T> generateGreaterThanCriteria(String key, T value) {
        return new Search<>(key, ">", value);
    }

    public static <T extends Comparable<T>> Search<T> generateLessThanCriteria(String key, T value) {
        return new Search<>(key, "<", value);
    }

    public static Predicate defaultLikeQuery(CriteriaBuilder criteriaBuilder, Expression<String> expression, String value) {
        if (value == null || criteriaBuilder == null || expression == null) {
            throw new IllegalArgumentException("CriteriaBuilder, Expression, and value must not be null");
        }
        return criteriaBuilder.like(
                criteriaBuilder.lower(expression), value.toLowerCase());
    }

    public static <T> Specification<T> generateTrueSpecification() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static <T> Specification<T> generateFalseSpecification() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.disjunction();
    }
}
