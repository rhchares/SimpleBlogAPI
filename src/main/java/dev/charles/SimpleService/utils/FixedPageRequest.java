package dev.charles.SimpleService.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Slf4j
public class FixedPageRequest extends PageRequest {
    public FixedPageRequest(Pageable pageable, long totalCount) {
        super(getPageNumber(pageable, totalCount), pageable.getPageSize(), pageable.getSort());
    }
    private static int getPageNumber(Pageable pageable, long totalCount) {
        int pageNo = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        long requestCount = pageNo * pageSize;

        if (totalCount > requestCount) {
            return pageNo;
        }
        log.info("totalCount {} pageNo {} pageSize {}", totalCount, pageNo,pageSize);
        return (int) Math.floor((double)totalCount/pageSize);
    }
}
