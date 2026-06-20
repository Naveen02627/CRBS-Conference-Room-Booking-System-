package com.TechnoCarts.CRBS.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
@Builder
@Data
public class pair implements Serializable {
    int booking,reqCnt;
    public pair(int booking, int reqCnt) {
        this.booking = booking;
        this.reqCnt = reqCnt;
    }
}
