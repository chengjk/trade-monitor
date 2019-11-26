package com.jk.trademonitor.service;

import com.huobi.api.client.constant.HuobiConfig;
import com.huobi.api.client.domain.enums.OrderSide;
import com.huobi.api.client.domain.event.TradeData;
import com.huobi.api.client.domain.event.TradeDetailResp;
import com.huobi.api.client.domain.resp.ApiCallback;
import com.huobi.api.client.impl.HuobiApiWebSocketClientImpl;
import com.jk.trademonitor.entity.VolumeDetail;
import com.jk.trademonitor.repo.VolumeDetailRepo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/**
 * created by jacky. 2019/11/22 6:06 PM
 */
@Slf4j
@Service
public class VolumeDetailServiceImpl implements VolumeDetailService {


    private Closeable stream;
    @Value("${apiKey}")
    private String apiKey = "a";
    @Value("${apiSecret}")
    private String apiSecret = "s";
    private HuobiApiWebSocketClientImpl ws;


    @Resource
    private VolumeDetailRepo repo;

    private HashMap<Long, VolumeDetail> vol = new HashMap<>();


    @PostConstruct
    public void init() throws IOException {
        HuobiConfig.ReconnectOnFailure = true;
        ws = new HuobiApiWebSocketClientImpl(apiKey, apiSecret);
        subTick();
        saveDetailSchedule();
    }


    public void subTick() {
        stream = ws.onTradeDetailTick("btcusdt", new ApiCallback<TradeDetailResp>() {
            @Override
            public void onResponse(WebSocket webSocket, TradeDetailResp response) {
                Set<TradeData> data = response.getTick().getData();
                for (TradeData t : data) {
                    refreshDetail(t);
                }
            }

            @Override
            public void onExpired(WebSocket webSocket, int code, String reason) {
                log.info("ws expired callback");
            }

            @Override
            public void onConnect(WebSocket ws, Closeable closeable) {
                stream = closeable;
                log.info("onConnect :" + closeable.hashCode());
            }
        });
    }

    public void refreshDetail(TradeData t) {
        Date date = new Date(t.getTs());
        long millis = new DateTime(date).withSecondOfMinute(0).withMillisOfSecond(0).getMillis();
        VolumeDetail current = vol.get(millis);
        if (current == null) {
            current = new VolumeDetail();
            current.setId(millis);
        }
        if (t.getDirection().equals(OrderSide.buy)) {
            current.setBidTakerVolume(current.getBidTakerVolume().add(t.getAmount()));
        } else {
            current.setAskTakerVolume(current.getAskTakerVolume().add(t.getAmount()));
        }
        current.setVolume(current.getAskTakerVolume().add(current.getBidTakerVolume()));
        current.setPrice(t.getPrice());
        vol.put(millis, current);
    }


    @Scheduled(cron = "1/5 * * * * ?")
    public void saveDetailSchedule() {
        long millis = DateTime.now().withSecondOfMinute(0).withMillisOfSecond(0).getMillis();
        VolumeDetail vd = vol.get(millis);
        if (vd != null) {
            repo.save(vd);
            log.info("save succeed!" + vd.toString());
        }
    }


    @Override
    public Page<VolumeDetail> findPage(int pageNo,int pageSize){
        return repo.findAll(PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id")));
    }
}
