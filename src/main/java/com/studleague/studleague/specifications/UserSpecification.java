package com.studleague.studleague.specifications;

import com.studleague.studleague.entities.security.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;


public class UserSpecification {


    public static Specification<User> usernameContains(String username) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<User> searchUsers(String username, Sort sort) {
        Specification<User> spec = Specification.where(null);

        if (username != null && !username.isEmpty()) {
            spec = spec.and(usernameContains(username));
        }

        Specification<User> finalSpec = spec;
        return (root, query, criteriaBuilder) -> {
            if (sort != null) {
                query.orderBy(sort.stream()
                        .map(order -> order.isAscending()
                                ? criteriaBuilder.asc(root.get(order.getProperty()))
                                : criteriaBuilder.desc(root.get(order.getProperty())))
                        .toList());
            }
            return finalSpec.toPredicate(root, query, criteriaBuilder);
        };
    }
}


