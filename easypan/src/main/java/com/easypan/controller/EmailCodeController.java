package com.easypan.controller;

import java.io.Serializable;
import com.easypan.entity.vo.ResponseVO;

import com.easypan.entity.po.EmailCode;
import com.easypan.entity.query.EmailCodeQuery;
import com.easypan.service.EmailCodeService;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @Description:邮箱验证码Controller
 * @author: 洛洛
 * @date: 2024/06/06
 */

@RestController
@RequestMapping("/emailCode")
public class EmailCodeController extends ABaseController{

	@Resource
	private EmailCodeService emailCodeService;

	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(EmailCodeQuery query) {
		return getSuccessResponseVO(emailCodeService.findListByPage(query));
	}
	/**
	 * 新增
	 */

	@RequestMapping("add")
	public ResponseVO add(EmailCode bean) {
		this.emailCodeService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */

	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<EmailCode> listBean) {
		this.emailCodeService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增或修改
	 */

	@RequestMapping("addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<EmailCode> listBean) {
		this.emailCodeService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}


}