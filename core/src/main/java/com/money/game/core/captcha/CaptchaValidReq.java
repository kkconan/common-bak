package com.money.game.core.captcha;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@lombok.Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CaptchaValidReq {
	
	String sessionId;
	
	@NotBlank(message = "图形验证码不能为空！")
	String imgvc;
}
