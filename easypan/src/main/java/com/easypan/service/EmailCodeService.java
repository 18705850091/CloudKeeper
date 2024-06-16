package com.easypan.service;

import java.io.Serializable;
import com.easypan.entity.po.EmailCode;
import com.easypan.entity.query.EmailCodeQuery;
import java.util.List;
import com.easypan.entity.vo.PaginationResultVO;
import com.easypan.exception.BusinessException;

/**
 * @Description:邮箱验证码Service
 * @author: 洛洛
 * @date: 2024/06/06
 */

public interface EmailCodeService{

	/**
	 * 根据条件查询列表
	 */
	List<EmailCode> findListByParam(EmailCodeQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(EmailCodeQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery query);

	/**
	 * 新增
	 */
	Integer add(EmailCode bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<EmailCode> listBean);

	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<EmailCode> listBean);

	void sendEmailCode(String email,Integer type) throws BusinessException;

	void checkCode(String email,String code) throws BusinessException;
}