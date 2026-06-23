package com.scbank.process.api.fw.base.gateway.prc.base.simulation;

import java.lang.reflect.Method;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 게이트웨이 시뮬레이션 키 Resolver 클래스
 * 
 * <pre>
 * 	프로세스API 요청 대응답 정보를 찾기 위한 키 생성 Resolver 클래스
 * </pre>
 */
public final class SimulationKeyResolver {

	/**
	 * 요청 메소드 문자열을 확정한다.
	 * 
	 * @param m Method
	 * @return
	 */
	public static String httpMethod(Method m) {
		if (m.isAnnotationPresent(GetMapping.class)) {
			return "GET";
		}

		if (m.isAnnotationPresent(PostMapping.class)) {
			return "POST";
		}

		if (m.isAnnotationPresent(PutMapping.class)) {
			return "PUT";
		}

		if (m.isAnnotationPresent(DeleteMapping.class)) {
			return "DELETE";
		}

		if (m.isAnnotationPresent(PatchMapping.class)) {
			return "PATCH";
		}

		RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
		if (requestMapping != null && requestMapping.method().length > 0) {
			return requestMapping.method()[0].name();
		}

		return "POST";
	}

	/**
	 * 요청 경로를 획득한다.
	 * 
	 * @param m Method
	 * @return
	 */
	public static String path(Method m) {
		if (m.isAnnotationPresent(GetMapping.class)) {
			return first(m.getAnnotation(GetMapping.class).value(), m.getAnnotation(GetMapping.class).path());
		}

		if (m.isAnnotationPresent(PostMapping.class)) {
			return first(m.getAnnotation(PostMapping.class).value(), m.getAnnotation(PostMapping.class).path());
		}

		if (m.isAnnotationPresent(PutMapping.class)) {
			return first(m.getAnnotation(PutMapping.class).value(), m.getAnnotation(PutMapping.class).path());
		}

		if (m.isAnnotationPresent(DeleteMapping.class)) {
			return first(m.getAnnotation(DeleteMapping.class).value(), m.getAnnotation(DeleteMapping.class).path());
		}

		if (m.isAnnotationPresent(PatchMapping.class)) {
			return first(m.getAnnotation(PatchMapping.class).value(), m.getAnnotation(PatchMapping.class).path());
		}
		return "/";
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static String toSafePathToken(String path) {
		String p = path == null ? "/" : path.trim();
		int q = p.indexOf("?");
		if (q > 0) {
			p = p.substring(0, q);
		}

		while (p.startsWith("/")) {
			p = p.substring(1);
		}

		while (p.endsWith("/")) {
			p = p.substring(0, p.length() - 1);
		}

		if (p.isBlank()) {
			return "_root_";
		}

		return p.replace("/", "_");
	}

	/**
	 * 파일명을 획득한다.
	 * 
	 * @param m 메소드 {@link Method}
	 * @return
	 */
	public static String fileName(Method m) {
		String method = httpMethod(m);
		String token = toSafePathToken(path(m));

		StringBuffer fileName = new StringBuffer();
		return fileName
				.append(method.toLowerCase())
				.append("_").append(token)
				.append(".json")
				.toString();
	}

	private static String first(String[] a, String[] b) {
		if (a != null && a.length > 0 && !a[0].isBlank()) {
			return a[0];
		}

		if (b != null && b.length > 0 && !b[0].isBlank()) {
			return b[0];
		}

		return "/";
	}
}
