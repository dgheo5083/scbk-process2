package com.scbank.process.api.svc.shared.components.fds.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FdsLogDataTest {

    @Test
    void toStringTest() {

        FdsLogData data = FdsLogData.builder()
                .curDate("20250521")
                .curTime("123456")
                .channelType("M")
                .wasNum("W01")
                .txDscd("S")
                .wasTrnaNo(1)
                .egmsTranNo(2)
                .userId("USER1")
                .userIp("127.0.0.1")
                .imsTranCd("TR001")
                .inClassCd("IN01")
                .jobCd("JOB01")
                .svcCd("SVC01")
                .data("REQDATA")
                .deviceKey("DEVICEKEY")
                .appVersion("1.0")
                .build();

        String expected = String.format(
                "%s|%s|%s|%s|%s|%07d|%07d|%-10s|%-15s|%s|%s|%s|%s|%s|%-14s|%-5s|",
                "20250521",
                "123456",
                "M",
                "W01",
                "S",
                1,
                2,
                "USER1",
                "127.0.0.1",
                "TR001",
                "IN01",
                "JOB01",
                "SVC01",
                "REQDATA",
                "DEVICEKEY",
                "1.0");

        assertEquals(expected, data.toString());
    }
}