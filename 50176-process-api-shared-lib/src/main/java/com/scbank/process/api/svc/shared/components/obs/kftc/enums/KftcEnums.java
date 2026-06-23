package com.scbank.process.api.svc.shared.components.obs.kftc.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class KftcEnums {

        /**
         * 오픈 API 응답 오류
         */
        @Getter
        @RequiredArgsConstructor
        @ToString
        public static enum KftcErrorCode {

                // 거래중지
                A0014("A0014", "탈퇴회원", KftcErrorGroup.ACCOUNT_SUSPENDED),
                A0019("A0019", "탈퇴 처리 중", KftcErrorGroup.ACCOUNT_SUSPENDED),
                A0312("A0312", "예금주명 불일치", KftcErrorGroup.ACCOUNT_SUSPENDED),
                A0313("A0313", "사용자 불일치", KftcErrorGroup.ACCOUNT_SUSPENDED),
                BANK_412("412", "해당계좌 없음", KftcErrorGroup.ACCOUNT_SUSPENDED),
                BANK_414("414", "이관계좌", KftcErrorGroup.ACCOUNT_SUSPENDED),
                BANK_415("415", "해약 계좌", KftcErrorGroup.ACCOUNT_SUSPENDED),
                BANK_416("416", "잡좌", KftcErrorGroup.ACCOUNT_SUSPENDED),
                BANK_417("417", "비실명 계좌", KftcErrorGroup.ACCOUNT_SUSPENDED),
                BANK_425("425", "법적등록계좌(가압류 등)", KftcErrorGroup.ACCOUNT_SUSPENDED),
                BANK_461("461", "의뢰인 성명 오류", KftcErrorGroup.ACCOUNT_SUSPENDED),
                BANK_467("467", "장기미사용계좌 ", KftcErrorGroup.ACCOUNT_SUSPENDED),
                BANK_473("473", "명의인 변경 계좌 또는 상속계좌", KftcErrorGroup.ACCOUNT_SUSPENDED),

                // 이체한도 초과
                A0101("A0101", "이용기관 입금이체 한도 초과(일 한도)", KftcErrorGroup.TRANSFER_LIMIT_EXCEEDED),
                A0102("A0102", "이용기관 입금이체 한도 초과(월 한도)", KftcErrorGroup.TRANSFER_LIMIT_EXCEEDED),
                A0103("A0103", "이용기관 출금이체 한도 초과(일 한도)", KftcErrorGroup.TRANSFER_LIMIT_EXCEEDED),
                A0104("A0104", "이용기관 출금이체 한도 초과(월 한도)", KftcErrorGroup.TRANSFER_LIMIT_EXCEEDED),
                A0105("A0105", "이용기관 출금이체 한도 초과(건당 한도)", KftcErrorGroup.TRANSFER_LIMIT_EXCEEDED),
                A0106("A0106", "이용기관 입금이체 한도 초과(건당 한도)", KftcErrorGroup.TRANSFER_LIMIT_EXCEEDED),
                A0111("A0111", "사용자 출금이체 한도 초과(건당 한도)", KftcErrorGroup.TRANSFER_LIMIT_EXCEEDED),
                A0112("A0112", "사용자 출금이체 한도 초과(일 한도)", KftcErrorGroup.TRANSFER_LIMIT_EXCEEDED),
                BANK_455("455", "건별 이체한도 초과", KftcErrorGroup.TRANSFER_LIMIT_EXCEEDED),
                BANK_456("456", "일일 이체한도 초과", KftcErrorGroup.TRANSFER_LIMIT_EXCEEDED),

                // 오픈뱅킹 동의 만료/미완료
                A0305("A0305", "제3자 정보제공동의 미완료", KftcErrorGroup.OPEN_BANKING_TERMS_EXPIRED),
                A0306("A0306", "출금동의 미완료", KftcErrorGroup.OPEN_BANKING_TERMS_EXPIRED),
                A0316("A0316", "제3자제공동의 만료", KftcErrorGroup.OPEN_BANKING_TERMS_EXPIRED),
                A0319("A0319", "출금동의만료", KftcErrorGroup.OPEN_BANKING_TERMS_EXPIRED),
                A0332("A0332", "제3자 정보제공동의 사용자인증 미완료", KftcErrorGroup.OPEN_BANKING_TERMS_EXPIRED),
                A0334("A0334", "제3자정보제공동의 만료", KftcErrorGroup.OPEN_BANKING_TERMS_EXPIRED),

                // 잔액 부족
                BANK_453("453", "예금잔액 부족", KftcErrorGroup.INSUFFICIENT_BALANCE),
                BANK_454("454", "출금가능잔액 부족", KftcErrorGroup.INSUFFICIENT_BALANCE),

                // 계좌 상태
                A0317("A0317", "미등록 수취계좌", KftcErrorGroup.ACCOUNT_STATE),
                A0323("A0323", "이용기관에 등록된 사용자 계좌 아님", KftcErrorGroup.ACCOUNT_STATE),
                A0335("A0335", "기 등록된 조회서비스용 법인계좌", KftcErrorGroup.ACCOUNT_STATE),
                BANK_413("413", "통장분실 재발행계좌", KftcErrorGroup.ACCOUNT_STATE),
                BANK_418("418", "보안계좌", KftcErrorGroup.ACCOUNT_STATE),
                BANK_419("419", "사고신고 계좌", KftcErrorGroup.ACCOUNT_STATE),
                BANK_420("420", "거래중지 계좌", KftcErrorGroup.ACCOUNT_STATE),
                BANK_422("422", "타행처리 불가계좌", KftcErrorGroup.ACCOUNT_STATE),
                BANK_424("424", "연체계좌", KftcErrorGroup.ACCOUNT_STATE),
                BANK_427("427", "기타 출금 불가 계좌", KftcErrorGroup.ACCOUNT_STATE),
                BANK_428("428", "동의서 미징구 계좌", KftcErrorGroup.ACCOUNT_STATE),
                BANK_431("431", "잔액 및 부채증명 발급 계좌", KftcErrorGroup.ACCOUNT_STATE),
                BANK_432("432", "통장정리후 거래(무통건수 초과)", KftcErrorGroup.ACCOUNT_STATE),
                BANK_433("433", "인감분실", KftcErrorGroup.ACCOUNT_STATE),
                BANK_452("452", "비밀번호 입력횟수 초과", KftcErrorGroup.ACCOUNT_STATE),
                BANK_458("458", "30분간 지연인출(이체) 대상", KftcErrorGroup.ACCOUNT_STATE),
                BANK_468("468", "제3자정보제공(재)동의 만료", KftcErrorGroup.ACCOUNT_STATE),
                BANK_471("471", "전자금융거래 제한계좌", KftcErrorGroup.ACCOUNT_STATE),
                BANK_472("472", "고객요청 제한계좌", KftcErrorGroup.ACCOUNT_STATE),
                BANK_474("474", "비밀번호 잠기계좌", KftcErrorGroup.ACCOUNT_STATE),
                BANK_475("475", "신규계좌 일시제한(해당 지점 연락 요망)", KftcErrorGroup.ACCOUNT_STATE),
                BANK_476("476", "파산관재인 계좌(해당 지점 연락 요망)", KftcErrorGroup.ACCOUNT_STATE),

                // 고객 상태
                A0001("A0001", "처리 중 (이체결과조회 요망, 이체 시)", KftcErrorGroup.USER_STATE_WRONG),
                A0002("A0002", "참가기관 에러 ", KftcErrorGroup.USER_STATE_WRONG),
                A0003("A0003", "내부 처리 에러 ", KftcErrorGroup.USER_STATE_WRONG),
                A0004("A0004", "요청전문 포맷에러 ", KftcErrorGroup.USER_STATE_WRONG),
                A0005("A0005", "등록 데이터 에러 ", KftcErrorGroup.USER_STATE_WRONG),
                A0006("A0006", "전문 변환 에러 ", KftcErrorGroup.USER_STATE_WRONG),
                A0007("A0007", "처리시간 초과 에러 ", KftcErrorGroup.USER_STATE_WRONG),
                A0008("A0008", "중복거래 에러 ", KftcErrorGroup.USER_STATE_WRONG),
                A0009("A0009", "API 세부업무 처리실패(리스트 건별 처리결과 확인) ", KftcErrorGroup.USER_STATE_WRONG),
                A0010("A0010", "이용기관 APP 정보 확인 실패 ", KftcErrorGroup.USER_STATE_WRONG),
                A0011("A0011", "이용기관 API 사용권한 없음 ", KftcErrorGroup.USER_STATE_WRONG),
                A0012("A0012", "API 정보확인 실패 ", KftcErrorGroup.USER_STATE_WRONG),
                A0013("A0013", "이용기관 서비스 사용 불가 ", KftcErrorGroup.USER_STATE_WRONG),
                A0015("A0015", "시뮬레이선 응답 전문 존재하지 않음 ", KftcErrorGroup.USER_STATE_WRONG),
                A0016("A0016", "내부 전문 송신 실패 ", KftcErrorGroup.USER_STATE_WRONG),
                A0017("A0017", "참가기관 응답전문 timeout ", KftcErrorGroup.USER_STATE_WRONG),
                A0018("A0018", "거래내역 없음 ", KftcErrorGroup.USER_STATE_WRONG),
                A0020("A0020", "조회 가능시간 아님 ", KftcErrorGroup.USER_STATE_WRONG),
                A0021("A0021", "오픈뱅킹센터 지정 에러미시지  > 사용자에게 고지 의무 ", KftcErrorGroup.USER_STATE_WRONG),
                A0301("A0301", "접근권한 없음 ", KftcErrorGroup.USER_STATE_WRONG),
                A0302("A0302", "참가기관 API 이용권한 없음 ", KftcErrorGroup.USER_STATE_WRONG),
                A0303("A0303", "등록된 이용기관 수수료 정책 없음 ", KftcErrorGroup.USER_STATE_WRONG),
                A0304("A0304", "핀테크 이용번호 정보 불일치  ", KftcErrorGroup.USER_STATE_WRONG),
                A0307("A0307", "이체암호문구 불일치 ", KftcErrorGroup.USER_STATE_WRONG),
                A0308("A0308", "처리대행비용 할인대상 여부 없음 ", KftcErrorGroup.USER_STATE_WRONG),
                A0310("A0310", "이체내역 없음 ", KftcErrorGroup.USER_STATE_WRONG),
                A0311("A0311", "등록된 처리대항 수수료 정보 없음 ", KftcErrorGroup.USER_STATE_WRONG),
                A0314("A0314", "(계좌이동 중)", KftcErrorGroup.USER_STATE_WRONG), // 중복
                A0318("A0318", "수취계좌 사업자등록번호 상이 ", KftcErrorGroup.USER_STATE_WRONG),
                A0320("A0320", "실명번호(전체) 조회 권한 없음 ", KftcErrorGroup.USER_STATE_WRONG),
                A0321("A0321", "예금주 실명번호 구분코드와 실명번호의 형식 불일치 ", KftcErrorGroup.USER_STATE_WRONG),
                A0322("A0322", "미등록된 이용기관 약정 계좌/계정 ", KftcErrorGroup.USER_STATE_WRONG),
                A0324("A0324", "기등록된 조회서비스용 사용자 서비스 ", KftcErrorGroup.USER_STATE_WRONG),
                A0325("A0325", "기 등록된 출금서비스용 사용자 서비스 ", KftcErrorGroup.USER_STATE_WRONG),
                A0326("A0326", "거래고유번호(참가기관) 중복 ", KftcErrorGroup.USER_STATE_WRONG),
                A0327("A0327", "출금한도 미협의 은행(이용기관 문의) ", KftcErrorGroup.USER_STATE_WRONG),
                A0331("A0331", "계좌통합관리시스템 처리 오류 ", KftcErrorGroup.USER_STATE_WRONG),
                A0336("A0336", "오프라인 요청 발생 불가 기관 ", KftcErrorGroup.USER_STATE_WRONG),
                A0337("A0337", "해당 사용자 없음 ", KftcErrorGroup.USER_STATE_WRONG),
                BANK_421("421", "법인계좌 이체불가 ", KftcErrorGroup.USER_STATE_WRONG), // 중복
                BANK_426("426", "압류금지 전용 계좌로 입금 불가 ", KftcErrorGroup.USER_STATE_WRONG),
                BANK_462("462", "고객 앞 통지 반송계좌 ", KftcErrorGroup.USER_STATE_WRONG), // 중복
                BANK_463("463", "실명번호 상위 ", KftcErrorGroup.USER_STATE_WRONG), // 중복
                BANK_464("464", "사용자 등록정보 이상(기해지 등)", KftcErrorGroup.USER_STATE_WRONG),
                BANK_470("470", "개인정보 노출자", KftcErrorGroup.USER_STATE_WRONG),
                BANK_477("477", "사용자 단말기 정보 불일치(해당 지점 연락 요망) ", KftcErrorGroup.USER_STATE_WRONG),
                BANK_490("490", "오픈뱅킹 안심차단 중인 사용자", KftcErrorGroup.USER_STATE_WRONG),
                BANK_499("499", "기타 처리불가", KftcErrorGroup.USER_STATE_WRONG),
                BANK_551("551", "기 해지 사용자", KftcErrorGroup.USER_STATE_WRONG),
                BANK_555("555", "해당 사용자 없음", KftcErrorGroup.USER_STATE_WRONG),
                BANK_556("556", "사용자 미등록", KftcErrorGroup.USER_STATE_WRONG),

                // 알림함 메시지
                ALARM_TRANSFER_IMPOSSIBLE("ALARM_TRANSFER_IMPOSSIBLE", "출금 이체 불가능 상태", KftcErrorGroup.ALARM_MESSAGE),
                ALARM_TRANSFER_LIMIT_EXCEEDED("ALARM_TRANSFER_LIMIT_EXCEEDED", "오픈뱅킹 이체한도 초과",
                                KftcErrorGroup.ALARM_MESSAGE),
                ALARM_OPEN_BANKING_TERMS_CONFIRM("ALARM_OPEN_BANKING_TERMS_CONFIRM", "오픈뱅킹 계좌 동의 상태 확인 필요",
                                KftcErrorGroup.ALARM_MESSAGE),
                ALARM_INSUFFICIENT_BALANCE("ALARM_INSUFFICIENT_BALANCE", "출금계좌 잔액 부족", KftcErrorGroup.ALARM_MESSAGE),
                ALARM_ACCOUNT_STATE_CONFIRM("ALARM_ACCOUNT_STATE_CONFIRM", "등록 계좌상태 확인 필요",
                                KftcErrorGroup.ALARM_MESSAGE);

                private final String code;
                private final String detail;
                private final KftcErrorGroup group;

                public String getSmsMsg(String accountNumber) {
                        return group.getSmsMsg(accountNumber);
                }

                public static KftcErrorCode fromCode(String code) {
                        return Arrays.stream(KftcErrorCode.values()).filter(e -> e.getCode().equals(code)).findFirst()
                                        .orElse(null);
                }

                public static Optional<KftcErrorCode> of(String code) {
                        return Arrays.stream(values()).filter(e -> e.getCode().equals(code)).findFirst();
                }
        }

        @Getter
        @RequiredArgsConstructor
        @ToString
        public static enum KftcErrorGroup {
                ACCOUNT_SUSPENDED(
                                "거래 중지",
                                """
                                                예금계좌(%s)로의 타행출금자동이체 또는 충전 등록 건이 중지 되었습니다.

                                                다른 금융기관 출금 이체가 불가능한 상태이오니, 자세한 사항은 자동이체 관리 메뉴에서 확인 바랍니다.

                                                서비스 이용을 원하실 경우에는 기존에 등록하신 건을 해지 후, 정상계좌로 다시 등록하시기 바랍니다.
                                                """, true, true, true),
                TRANSFER_LIMIT_EXCEEDED(
                                "이체한도 초과",
                                """
                                                예금계좌(%s)로의 타행출금자동이체 또는 충전이 처리되지 않았습니다.

                                                오픈뱅킹 이체한도 초과 여부를 확인해 주세요.

                                                자세한 사유는 아래의 오픈뱅킹 이체결과 조회에서 확인 부탁드립니다.
                                                """, false, false, true),
                OPEN_BANKING_TERMS_EXPIRED(
                                "오픈뱅킹 동의 만료/미완료",
                                """
                                                예금계좌(%s)로의 타행출금자동이체 또는 충전이 처리되지 않았습니다.

                                                다른 금융기관 출금을 위해 등록된 오픈뱅킹 계좌의 동의 상태를 확인해 주세요.

                                                자세한 사유는 아래의 오픈뱅킹 이체결과 조회에서 확인 부탁드립니다.
                                                """, false, false, true),
                INSUFFICIENT_BALANCE(
                                "잔액 부족",
                                """
                                                예금계좌(%s)로의 타행출금자동이체 또는 충전이 처리되지 않았습니다.

                                                신청한 출금계좌의 잔액을 확인해 주세요.

                                                자세한 사유는 아래의 오픈뱅킹 이체결과 조회에서 확인 부탁드립니다.
                                                """, false, false, true),
                ACCOUNT_STATE(
                                "계좌 상태",
                                """
                                                예금계좌(%s)로의 타행출금자동이체 또는 충전이 처리되지 않았습니다.

                                                서비스에 등록한 계좌 상태를 확인해 주세요.

                                                자세한 사유는 아래의 오픈뱅킹 이체결과 조회에서 확인 부탁드립니다.
                                                """, false, false, true),
                USER_STATE_WRONG(
                                "고객 상태",
                                """
                                                예금계좌(%s)로의 타행출금자동이체 또는 충전이 처리되지 않았습니다.

                                                다른 금융기관 출금 이체가 불가능한 상태이오니, 자세한 사항은 오픈뱅킹 이체결과 조회에서 확인 부탁드립니다.
                                                """, false, false, true),
                ALARM_MESSAGE(
                                "알림함 메시지",
                                """
                                                %s 계좌로 자동이체 또는 충전이 되지 않았습니다.
                                                """, true, true, false);

                private final String groupName;
                private final String smsMsg;
                private final boolean transferFailNoti; // 이체 거래 실패 시 : 자동이체(충전) 배치에서 제외할 경우 "O"표시 (상태 값 중지)
                private final boolean withdrawFailAlarmTalk; // 출금 실패 시 알림톡 발송여부
                private final boolean withdrawFailPush; // 출금 실패 시 푸시 발송여부

                public String getSmsMsg(String accountNumber) {
                        return String.format(smsMsg, accountNumber);
                }
        }
}
