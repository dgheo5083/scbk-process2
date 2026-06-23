package com.scbank.process.api.fw.dao.paging;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingRequest {

	private int offset;

	private int limit;
}
