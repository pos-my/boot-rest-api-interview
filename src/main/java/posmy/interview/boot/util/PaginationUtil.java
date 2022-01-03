package posmy.interview.boot.util;

import posmy.interview.boot.exception.InvalidPaginationException;
import posmy.interview.boot.model.common.Pagination;

public class PaginationUtil {
    public static Pagination createPagination(int count, int pageSize, int pageNumber){
        Pagination pagination = new Pagination();
        int pageCount = (count + pageSize - 1) / pageSize;
        pagination.setPageCount(pageCount);
        pagination.setPageSize(pageSize);
        pagination.setTotalCount(count);
        pagination.setPageNumber(pageNumber+1);
        return pagination;
    }

    public static void validatePagination(Integer pageNumber, Integer pageSize) throws InvalidPaginationException {
        if (pageNumber == null || pageSize == null || pageNumber <= 0 || pageSize <= 0) {
            throw new InvalidPaginationException();
        }
    }
}
