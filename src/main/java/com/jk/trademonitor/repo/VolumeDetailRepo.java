package com.jk.trademonitor.repo;

import com.jk.trademonitor.entity.VolumeDetail;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * created by jacky. 2019/11/22 6:05 PM
 */
@Repository
public interface VolumeDetailRepo extends PagingAndSortingRepository<VolumeDetail, Long> {

}
