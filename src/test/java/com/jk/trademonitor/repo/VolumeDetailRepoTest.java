package com.jk.trademonitor.repo;

import com.jk.trademonitor.entity.VolumeDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * created by jacky. 2019/11/22 6:15 PM
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class VolumeDetailRepoTest {

    @Autowired
    private VolumeDetailRepo repo;

    @Test
    public void save() {
        VolumeDetail s = new VolumeDetail();
        s.setId(0L);
        s.setSymbol("btc_usdt");
        s.setVolume(new BigDecimal("0"));
        s.setAskTakerVolume(new BigDecimal("0"));
        s.setBidTakerVolume(new BigDecimal("0"));
        s.setResolution("m1");
        repo.save(s);
    }


    @Test
    public void findOne() {
        Optional<VolumeDetail> one = repo.findById(1574650440000L);
        assert one.isPresent();
    }

    @Test
    public void findPageable() {
        Iterable<VolumeDetail> page = repo.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id")));
        assert page != null;
    }
}
