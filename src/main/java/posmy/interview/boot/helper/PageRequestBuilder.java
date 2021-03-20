package posmy.interview.boot.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class PageRequestBuilder {

    /**
     * Default page number. 0 means first page.
     */
    public static int DEFAULT_PAGE_NUMBER = 0;

    /**
     * Number of elements in a page.
     */
    public static int DEFAULT_PAGE_SIZE = 50;
    public static int MAX_PAGE_SIZE = 100;

    public static PageRequest build(Integer pageSize, Integer pageNumber, String sortingCriteria) {

        Set<String> sortingFileds = new LinkedHashSet<>(Arrays.asList(StringUtils.split(StringUtils.defaultIfEmpty(sortingCriteria, ""), ",")));
        List<Sort.Order> sortingOrders = sortingFileds.stream().map(PageRequestBuilder::getOrder).collect(Collectors.toList());
        Sort sort = sortingOrders.isEmpty() ? Sort.unsorted() : Sort.by(sortingOrders);

        pageNumber = defaultIfNull(pageNumber, DEFAULT_PAGE_NUMBER);
        pageNumber = pageNumber <= 0 ? DEFAULT_PAGE_NUMBER : pageNumber;

        pageSize = defaultIfNull(pageSize, DEFAULT_PAGE_SIZE);
        pageSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
        pageSize = pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : pageSize;

        return PageRequest.of(
                pageNumber,
                pageSize,
                sort);
    }

    private static Sort.Order getOrder(String value) {
        if (StringUtils.startsWith(value, "-")) {
            return new Sort.Order(Sort.Direction.DESC, StringUtils.substringAfter(value, "-"));
        } else if (StringUtils.startsWith(value, "+")) {
            return new Sort.Order(Sort.Direction.ASC, StringUtils.substringAfter(value, "+"));
        } else {
            return new Sort.Order(Sort.Direction.ASC, StringUtils.trim(value));
        }
    }
}
