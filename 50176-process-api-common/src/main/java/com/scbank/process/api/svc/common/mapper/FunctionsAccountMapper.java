package com.scbank.process.api.svc.common.mapper;

import java.util.List;
import java.util.Objects;

import org.mapstruct.Mapper;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.svc.shared.components.account.dto.AllAccountInfo;

@Mapper(componentModel = "spring")
public interface FunctionsAccountMapper {

    public AllAccountInfo toAllAccountInfo(AllAccountInfo info);

    default List<AllAccountInfo> toDepositAccountList(
            List<AllAccountInfo> list) {

        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toAllAccountInfo).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .filter(item -> {
                    String drawAcctNum = item.getDrawAcctNum();
                    String acctType = item.getAcctType();
                    Integer subCode = Integer.parseInt(drawAcctNum.substring(3, 5));

                    if ("1".equals(acctType) && subCode != 73) {
                        if (!(subCode >= 51 && subCode <= 59) && ((subCode == 46 || subCode == 48 || subCode == 49
                                || subCode == 70 || subCode == 80 || subCode == 90) || subCode < 60)) {
                            return true;
                        }
                    }

                    return false;
                })
                .toList();
    }

    default List<AllAccountInfo> toLoanAccountList(
            List<AllAccountInfo> list) {

        if (list == null) {
            return List.of();
        }

        return list.stream().map(this::toAllAccountInfo).filter(Objects::nonNull)
                .filter(item -> StringUtils.isNotBlank(item.getDrawAcctNum()))
                .filter(item -> {
                    String drawAcctNum = item.getDrawAcctNum();
                    String acctType = item.getAcctType();
                    Integer subCode = Integer.parseInt(drawAcctNum.substring(3, 5));

                    if ("1".equals(acctType) && subCode != 73) {
                        if (!(subCode >= 51 && subCode <= 59) && ((subCode == 46 || subCode == 48 || subCode == 49
                                || subCode == 70 || subCode == 80 || subCode == 90) || subCode < 60)) {
                            return false;
                        }
                    }

                    if ("3".equals(acctType) || subCode != 73) {
                        return true;
                    }

                    return false;
                })
                .toList();
    }
}
