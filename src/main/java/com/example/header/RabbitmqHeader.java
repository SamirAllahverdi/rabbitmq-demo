package com.example.header;


import lombok.Data;

import java.util.*;


@Data
public class RabbitmqHeader {

    private static final String KEYWORD_QUEUE_WAIT = "wait";
    private List<RabbitmqHeaderXDeath> xDeaths = new ArrayList<>(2);
    private String xFirstDeathExchange = "";
    private String xFirstDeathQueue = "";
    private String xFirstDeathReason = "";

    @SuppressWarnings("unchecked")
    public RabbitmqHeader(Map<String, Object> headers) {
        if (headers != null) {
            Optional.ofNullable(headers.get("x-first-death-exchange")).ifPresent(s -> this.setXFirstDeathExchange(s.toString()));
            Optional.ofNullable(headers.get("x-first-death-queue")).ifPresent(s -> this.setXFirstDeathQueue(s.toString()));
            Optional.ofNullable(headers.get("x-first-death-reason")).ifPresent(s -> this.setXFirstDeathReason(s.toString()));

            var xDeathHeaders = (List<Map<String, Object>>) headers.get("x-death");
            if (xDeathHeaders != null) {
                for (Map<String, Object> x : xDeathHeaders) {
                    RabbitmqHeaderXDeath hdrDeath = new RabbitmqHeaderXDeath();

                    Optional.ofNullable(x.get("reason")).ifPresent(s -> hdrDeath.setReason(s.toString()));
                    Optional.ofNullable(x.get("count")).ifPresent(s -> hdrDeath.setCount(Integer.parseInt(s.toString())));
                    Optional.ofNullable(x.get("exchange")).ifPresent(s -> hdrDeath.setExchange(s.toString()));
                    Optional.ofNullable(x.get("queue")).ifPresent(s -> hdrDeath.setQueue(s.toString()));
                    Optional.ofNullable(x.get("time")).ifPresent(d -> hdrDeath.setTime((Date) d));

                    Optional.ofNullable(x.get("routing-keys")).ifPresent(r -> {
                        var listR = (List<String>) r;
                        hdrDeath.setRoutingKeys(listR);
                    });

                    xDeaths.add(hdrDeath);
                }
            }
        }
    }

    public int getFailedRetryCount() {
        return xDeaths.stream()
                .filter(xDeath ->
                        xDeath.getExchange().toLowerCase().endsWith(KEYWORD_QUEUE_WAIT)
                                && xDeath.getQueue().toLowerCase().endsWith(KEYWORD_QUEUE_WAIT))
                .map(RabbitmqHeaderXDeath::getCount)
                .findFirst()
                .orElse(0);
    }


}
