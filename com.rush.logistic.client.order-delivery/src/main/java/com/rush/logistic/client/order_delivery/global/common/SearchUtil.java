package com.rush.logistic.client.order_delivery.global.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class SearchUtil {
    public static Pageable checkAndGetPageRequest(Pageable pageable) {
        int page = pageable.getPageNumber();
        page = Math.max(page, 0);

        int size = pageable.getPageSize();
        List<Integer> sizeAvailable = Arrays.asList(10, 30, 50);
        if (!sizeAvailable.contains(page)) {
            page = 10;
        }

        Sort sort = pageable.getSort();
        Iterator<Sort.Order> it = sort.stream().iterator();
        Sort.Order order = (Sort.Order) it.next(); // 첫번째 값만 사용
        log.info("checkAndGetPageRequest sort order : {}", order.getProperty());

        // 유효한 정렬 기준 값인지 확인 (createdAt, updatedAt)
        String sortFieldName = order.getProperty();
        boolean isValid = Arrays.stream(SearchSortBy.values())
                .anyMatch(s -> s.getFieldName().equals(sortFieldName));

        String sortBy = sortFieldName;
        if (!isValid) {
            log.info("checkAndGetPageRequest sort : set CREATED_AT");
            sortBy = SearchSortBy.CREATED_AT.getFieldName();
        }

        return PageRequest.of(page, size, Sort.by(sortBy).descending());
    }
}
