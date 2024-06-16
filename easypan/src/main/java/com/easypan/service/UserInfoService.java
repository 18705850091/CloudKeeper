package com.easypan.service;

import java.io.Serializable;

import com.easypan.entity.dto.SessionWebUserDto;
import com.easypan.entity.po.UserInfo;
import com.easypan.entity.query.UserInfoQuery;
import java.util.List;
import com.easypan.entity.vo.PaginationResultVO;
import com.easypan.exception.BusinessException;

/**
 * @Description:用户信息Service
 * @author: 洛洛
 * @date: 2024/06/06
 */

public interface UserInfoService{

	/**
	 * 根据条件查询列表
	 */
	List<UserInfo> findListByParam(UserInfoQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(UserInfoQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<UserInfo> findListByPage(UserInfoQuery query);

	/**
	 * 新增
	 */
	Integer add(UserInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserInfo> listBean);

	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<UserInfo> listBean);

	/**
	 * 根据UserId查询
	 */
	UserInfo getUserInfoByUserId(String userId);

	/**
	 * 根据UserId更新
	 */
	Integer updateUserInfoByUserId( UserInfo t, String userId);

	/**
	 * 根据UserId删除
	 */
	Integer deleteUserInfoByUserId(String userId);

	/**
	 * 根据NickName查询
	 */
	UserInfo getUserInfoByNickName(String nickName);

	/**
	 * 根据NickName更新
	 */
	Integer updateUserInfoByNickName( UserInfo t, String nickName);

	/**
	 * 根据NickName删除
	 */
	Integer deleteUserInfoByNickName(String nickName);

	/**
	 * 根据Email查询
	 */
	UserInfo getUserInfoByEmail(String email);

	/**
	 * 根据Email更新
	 */
	Integer updateUserInfoByEmail( UserInfo t, String email);

	/**
	 * 根据Email删除
	 */
	Integer deleteUserInfoByEmail(String email);

	/**
	 * 根据QqOpenId查询
	 */
	UserInfo getUserInfoByQqOpenId(String qqOpenId);

	/**
	 * 根据QqOpenId更新
	 */
	Integer updateUserInfoByQqOpenId( UserInfo t, String qqOpenId);

	/**
	 * 根据QqOpenId删除
	 */
	Integer deleteUserInfoByQqOpenId(String qqOpenId);

	void register(String email,String nickName,String password,String emailCode) throws BusinessException;

	SessionWebUserDto login(String email,String password) throws BusinessException;

	void resetPwd(String email,String password,String emailCode) throws BusinessException;
}