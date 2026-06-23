package com.scbank.process.api.svc.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.svc.common.dao.dto.InsertPushJoinDataParameter;
import com.scbank.process.api.svc.common.dao.dto.PushJoinAlarmListResult;
import com.scbank.process.api.svc.common.dao.dto.PushJoinStatusResult;
import com.scbank.process.api.svc.common.dao.dto.UpdatePushJoinAlarmParameter;
import com.scbank.process.api.svc.common.dao.dto.UpdatePushJoinDataParameter;

@DaoComponent(id = "TmbObjUsrMgtDao", database = "smsdb", description = "PUSH 가입 여부 조회 ", author = "951301")
public interface TmbObjUsrMgtDao {

    @ComponentOperation(name = "PUSH 가입 여부 조회 ", description = "PUSH 가입 여부 조회 ")
    List<PushJoinStatusResult> selectPushJoinStatus(@Param("userId") String userId);

    @ComponentOperation(name = "입출금 알림 서비스 등록 계좌 목록 조회 ", description = "입출금 알림 서비스 등록 계좌 목록 조회 ")
    List<PushJoinAlarmListResult> selectPushJoinAlarmList(@Param("userId") String userId);

    @ComponentOperation(name = "입출금 알림 서비스 등록 정보 조회 ", description = "입출금 알림 서비스 등록 정보 조회 ")
    PushJoinStatusResult selectPushJoinAlarm(@Param("userId") String userId);

    @ComponentOperation(name = "Push 기가입 고객 재가입 처리 및 정보 업데이트", description = "Push 기가입 고객 재가입 처리 및 정보 업데이트")
    int updatePushJoinData(UpdatePushJoinDataParameter parameter);

    @ComponentOperation(name = "PUSH 고객일련번호 시퀀스 조회", description = "PUSH 고객일련번호 시퀀스 조회")
    int selectPushUsrSeq();

    @ComponentOperation(name = "PUSH 고객정보 등록", description = "PUSH 고객정보 등록")
    int insertPushJoinData(InsertPushJoinDataParameter parameter);

    @ComponentOperation(name = "Push 알림 서비스 등록 및 변경", description = "Push 알림 서비스 등록 및 변경")
    int updatePushJoinAlarm(UpdatePushJoinAlarmParameter parameter);

}
