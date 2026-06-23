package com.scbank.process.api.fw.dao.paging;

public class PagingRequestContext {

	private static ThreadLocal<PagingRequest> pagingRequestHolder = new ThreadLocal<>();

	public static void set(PagingRequest pagingRequest) {
		pagingRequestHolder.set(pagingRequest);
	}

	public static void set(int offset, int limit) {
		PagingRequest request = new PagingRequest();
		request.setOffset(offset);
		request.setLimit(limit);

		pagingRequestHolder.set(request);
	}

	public static PagingRequest get() {
		return pagingRequestHolder.get();
	}

	public static void clear() {
		pagingRequestHolder.remove();
	}
}
