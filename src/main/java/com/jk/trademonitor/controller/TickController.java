package com.jk.trademonitor.controller;

import com.jk.trademonitor.entity.VolumeDetail;
import com.jk.trademonitor.service.VolumeDetailService;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * created by jacky. 2019/11/22 6:06 PM
 */
@RequestMapping("/vol")
@RestController
public class TickController {


    @Resource
    private VolumeDetailService volumeDetailService;

    @RequestMapping("/page")
    public Page<VolumeDetail> page(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "60") int pageSize) {
        return volumeDetailService.findPage(pageNo, pageSize);
    }

    @RequestMapping("/last")
    public List last(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "60") int pageSize) {
        Page<VolumeDetail> page = volumeDetailService.findPage(pageNo, pageSize);
        List result = new ArrayList();
        Iterator<VolumeDetail> iterator = page.iterator();
        while (iterator.hasNext()) {
            VolumeDetail detail = iterator.next();
            List item=new ArrayList<>();
            item.add(new DateTime(new Date(detail.getId())).toString());
            item.add(detail.getPrice().stripTrailingZeros().doubleValue());
            result.add(item);
        }
        return result;
    }

}
