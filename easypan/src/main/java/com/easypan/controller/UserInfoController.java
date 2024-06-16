package com.easypan.controller;

import java.io.Serializable;
import com.easypan.entity.vo.ResponseVO;

import com.easypan.entity.po.UserInfo;
import com.easypan.entity.query.UserInfoQuery;
import com.easypan.service.UserInfoService;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @Description:用户信息Controller
 * @author: 洛洛
 * @date: 2024/06/06
 */

@RestController
@RequestMapping("/userInfo")
public class UserInfoController extends ABaseController{

	@Resource
	private UserInfoService userInfoService;

	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(UserInfoQuery query) {
		return getSuccessResponseVO(userInfoService.findListByPage(query));
	}
	/**
	 * 新增
	 */

	@RequestMapping("add")
	public ResponseVO add(UserInfo bean) {
		this.userInfoService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */

	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<UserInfo> listBean) {
		this.userInfoService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增或修改
	 */

	@RequestMapping("addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<UserInfo> listBean) {
		this.userInfoService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据UserId查询
	 */

	@RequestMapping("getUserInfoByUserId")
	public ResponseVO getUserInfoByUserId(String userId) {
		return getSuccessResponseVO(this.userInfoService.getUserInfoByUserId(userId));
	}

	/**
	 * 根据UserId更新
	 */

	@RequestMapping("updateUserInfoByUserId")
	public ResponseVO updateUserInfoByUserId( UserInfo  bean, String userId) {
		this.userInfoService.updateUserInfoByUserId(bean,userId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据UserId删除
	 */

	@RequestMapping("deleteUserInfoByUserId")
	public ResponseVO deleteUserInfoByUserId(String userId) {
		this.userInfoService.deleteUserInfoByUserId(userId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据NickName查询
	 */

	@RequestMapping("getUserInfoByNickName")
	public ResponseVO getUserInfoByNickName(String nickName) {
		return getSuccessResponseVO(this.userInfoService.getUserInfoByNickName(nickName));
	}

	/**
	 * 根据NickName更新
	 */

	@RequestMapping("updateUserInfoByNickName")
	public ResponseVO updateUserInfoByNickName( UserInfo  bean, String nickName) {
		this.userInfoService.updateUserInfoByNickName(bean,nickName);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据NickName删除
	 */

	@RequestMapping("deleteUserInfoByNickName")
	public ResponseVO deleteUserInfoByNickName(String nickName) {
		this.userInfoService.deleteUserInfoByNickName(nickName);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Email查询
	 */

	@RequestMapping("getUserInfoByEmail")
	public ResponseVO getUserInfoByEmail(String email) {
		return getSuccessResponseVO(this.userInfoService.getUserInfoByEmail(email));
	}

	/**
	 * 根据Email更新
	 */

	@RequestMapping("updateUserInfoByEmail")
	public ResponseVO updateUserInfoByEmail( UserInfo  bean, String email) {
		this.userInfoService.updateUserInfoByEmail(bean,email);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Email删除
	 */

	@RequestMapping("deleteUserInfoByEmail")
	public ResponseVO deleteUserInfoByEmail(String email) {
		this.userInfoService.deleteUserInfoByEmail(email);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据QqOpenId查询
	 */

	@RequestMapping("getUserInfoByQqOpenId")
	public ResponseVO getUserInfoByQqOpenId(String qqOpenId) {
		return getSuccessResponseVO(this.userInfoService.getUserInfoByQqOpenId(qqOpenId));
	}

	/**
	 * 根据QqOpenId更新
	 */

	@RequestMapping("updateUserInfoByQqOpenId")
	public ResponseVO updateUserInfoByQqOpenId( UserInfo  bean, String qqOpenId) {
		this.userInfoService.updateUserInfoByQqOpenId(bean,qqOpenId);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据QqOpenId删除
	 */

	@RequestMapping("deleteUserInfoByQqOpenId")
	public ResponseVO deleteUserInfoByQqOpenId(String qqOpenId) {
		this.userInfoService.deleteUserInfoByQqOpenId(qqOpenId);
		return getSuccessResponseVO(null);
	}


}