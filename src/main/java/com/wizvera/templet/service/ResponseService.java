package com.wizvera.templet.service;

import com.wizvera.templet.model.response.CommonResult;
import com.wizvera.templet.model.response.ListResult;
import com.wizvera.templet.model.response.PaginationListResult;
import com.wizvera.templet.model.response.SingleResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

	// enum으로 api 요청 결과에 대한 resultCode, message를 정의합니다.
	public enum CommonResponse {
		SUCCESS("0", "");

		String	resultCode;
		String	errorMsg;

		CommonResponse(String resultCode, String errorMsg) {
			this.resultCode = resultCode;
			this.errorMsg = errorMsg;
		}

		public String getResultCode() {
			return resultCode;
		}

		public String getErrorMsg() {
			return errorMsg;
		}
	}

	// 단일건 결과를 처리하는 메소드
	public <T> SingleResult<T> getSingleResult(T data) {
		SingleResult<T> result = new SingleResult<>();
		result.setResult(data);
		setSuccessResult(result);
		return result;
	}

	// 다중건 결과를 처리하는 메소드
	public <T> ListResult<T> getListResult(List<T> list) {
		ListResult<T> result = new ListResult<>();
		result.setResultList(list);
		setSuccessResult(result);
		return result;
	}

	// 성공 결과만 처리하는 메소드
	public CommonResult getSuccessResult() {
		CommonResult result = new CommonResult();
		setSuccessResult(result);
		return result;
	}

	// 페이징 결과를 처리하는 메소드
	public <T> PaginationListResult<T> getPaginationListResult(	List<T> adminInfoList,
																int totalPageNum,
																long totalElementsNum,
																int elementsNum) {
		PaginationListResult<T> result = new PaginationListResult<>();
		result.setResultList(adminInfoList);
		result.setTotalPageNum(String.valueOf(totalPageNum));
		result.setTotalElementsNum(String.valueOf(totalElementsNum));
		result.setElementsNum(String.valueOf(elementsNum));
		setSuccessResult(result);
		return result;
	}

	// 페이징 결과를 처리하는 메소드
	public <T> PaginationListResult<T> getPaginationListResult(	List<T> adminInfoList,
																int totalPageNum,
																long totalElementsNum,
																int elementsNum,
																int pageNum) {
		PaginationListResult<T> result = new PaginationListResult<>();
		result.setResultList(adminInfoList);
		result.setTotalPageNum(String.valueOf(totalPageNum));
		result.setTotalElementsNum(String.valueOf(totalElementsNum));
		result.setElementsNum(String.valueOf(elementsNum));
		result.setPageNum(String.valueOf(pageNum));
		setSuccessResult(result);
		return result;
	}

	// 실패 결과만 처리하는 메소드
	public CommonResult getFailResult(	int code,
										String msg) {
		CommonResult result = new CommonResult();
		// result.setSuccess(false);
		result.setResultCode(String.valueOf(code));
		result.setErrorMsg(msg);
		return result;
	}

	// 실패 결과만 처리하는 메소드
	public <T> SingleResult<T> getFailSingleResult(	int code,
													String msg) {
		SingleResult<T> result = new SingleResult<T>();
		result.setResultCode(String.valueOf(code));
		result.setErrorMsg(msg);
		return result;
	}

	// 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
	private void setSuccessResult(CommonResult result) {
		// result.setSuccess(true);
		result.setResultCode(CommonResponse.SUCCESS.getResultCode());
		result.setErrorMsg(CommonResponse.SUCCESS.getErrorMsg());
	}
}
