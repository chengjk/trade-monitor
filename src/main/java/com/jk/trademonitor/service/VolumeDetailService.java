package com.jk.trademonitor.service;

import com.jk.trademonitor.entity.VolumeDetail;
import org.springframework.data.domain.Page;

/**
 * created by jacky. 2019/11/22 6:05 PM
 */
public interface VolumeDetailService {
    Page<VolumeDetail> findPage(int pageNo, int pageSize);
}
