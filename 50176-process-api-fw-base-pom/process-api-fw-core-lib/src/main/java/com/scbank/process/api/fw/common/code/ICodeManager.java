package com.scbank.process.api.fw.common.code;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.i18n.LocaleContextHolder;

import com.scbank.process.api.fw.core.lifecycle.IReloadable;

/**
 * <pre>
 *     공통코드 매니저 인터페이스
 * packageName    : co.kr.framework.common.code
 * fileName       : ICommCodeManager
 * author         : gasigol
 * date           : 25. 4. 10.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 4. 10.        gasigol       최초 생성
 * </pre>
 */
public interface ICodeManager extends IReloadable, InitializingBean {

	default void afterPropertiesSet() throws Exception {
		init();
	}

	void init();

	/**
	 * 공통코드 아이템 값을 가져온다.
	 *
	 * @param codeKey     공통코드 키
	 * @param codeItemKey 공통코드 아이템 키
	 * @return 공통코드 아이템 값
	 */
	default String getCodeItem(String codeKey, String codeItemKey) {
		Locale defaultLocale = LocaleContextHolder.getLocale();
		String defaultLangCode = defaultLocale.getLanguage();
		return this.getCodeItem(codeKey, codeItemKey, defaultLangCode);
	}

	/**
	 * 공통코드 아이템 값을 가져온다.
	 *
	 * @param codeKey     공통코드 키
	 * @param codeItemKey 공통코드 아이템 키
	 * @param locale      언어 코드
	 * @return 공통코드 아이템 값
	 */
	String getCodeItem(String codeKey, String codeItemKey, String locale);

	/**
	 * 공통코드 하위 공통코드 아이템 목록을 가져온다.
	 *
	 * @param codeKey 공통코드 키
	 * @param locale  언어 코드
	 * @return 공통코드 하위 공통코드 아이템 목록
	 */
	List<ICodeItemInfo> getCodeItemList(String codeKey, String locale);

	/**
	 * 공통코드 하위 공통코드 아이템 목록을 가져온다.
	 *
	 * @param codeKey 공통코드 키
	 * @return 공통코드 하위 공통코드 아이템 목록
	 */
	default List<ICodeItemInfo> getCodeItemList(String codeKey) {
		Locale defaultLocale = LocaleContextHolder.getLocale();
		String defaultLangCode = defaultLocale.getLanguage();
		return getCodeItemList(codeKey, defaultLangCode);
	}

	/**
	 * 
	 * @return
	 */
	Map<String, Object> getAllCodes();
}
