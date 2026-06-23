package com.scbank.process.api.svc.common.mapper;

import org.mapstruct.Mapper;

import com.scbank.process.api.svc.common.dao.dto.TermsInfoResult;
import com.scbank.process.api.svc.common.service.functions.dto.terms.TermsInfo;

@Mapper(componentModel = "spring")
public interface FunctionsTermsMapper {

        // @Mapping(target="prvsnLongExpln", expression=
        // "java(getPrvsnLongExplnString(termsInfoResult.getPrvsnLongExpln()))")
        public TermsInfo toTermsInfo(TermsInfoResult termsInfoResult);

        // public List<TermsInfo> toTermsInfoList(List<TermsInfoResult>
        // termsInfoResultList);

        /**
         * Clob 데이타 String 으로 읽기
         * 
         * @param Clob
         * @return String
         */
        /*
         * // TODO : DB2 드라이버 업데이트 후 불필요 시 제거
         * default String getPrvsnLongExplnString(Clob prvsnLongExplnClob) {
         * 
         * // Clob pClob = termsInfoResult.getPrvsnLongExpln();
         * 
         * if(prvsnLongExplnClob == null) {
         * return "";
         * }
         * 
         * StringBuffer loutStr = new StringBuffer();
         * try(Reader lreader = prvsnLongExplnClob.getCharacterStream()) {
         * char[] lbuff = new char[8192];
         * int lcnt = 0;
         * if (lreader != null) {
         * while ((lcnt = lreader.read(lbuff)) > 0) {
         * loutStr.append(lbuff, 0, lcnt);
         * }
         * }
         * } catch (IOException|SQLException e) {
         * return loutStr.toString();
         * }
         * 
         * return loutStr.toString();
         * }
         */

}
