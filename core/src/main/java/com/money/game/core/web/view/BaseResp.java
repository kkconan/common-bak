package com.money.game.core.web.view;

import com.money.game.core.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
	public class BaseResp {

	protected String errorCode;
	protected String errorMessage;

	public void error(Throwable error) {
		if (error instanceof BaseException) {
			BaseException e = (BaseException) error;
			this.errorCode =  e.getCode();
			this.errorMessage = e.getMessage();
		} else {
			this.errorCode = "-1";
			this.errorMessage = error.getMessage();
			log.error("error={}",error.getMessage(),error);
		}
	}

}
