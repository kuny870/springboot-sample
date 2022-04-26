package com.wizvera.templet.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginationListResult<T> extends CommonResult {
	private List<T>	resultList;
	private String	totalPageNum;
	private String	totalElementsNum;
	private String	elementsNum;
	private String	pageNum;
}