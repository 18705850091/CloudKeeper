package com.easypan.controller;

import com.easypan.annotation.GlobalInterceptor;
import com.easypan.annotation.VerifyParam;
import com.easypan.entity.dto.SessionWebUserDto;
import com.easypan.entity.dto.UploadResultDto;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.vo.FileInfoVO;
import com.easypan.entity.vo.PaginationResultVO;
import com.easypan.entity.vo.ResponseVO;

import com.easypan.entity.query.FileInfoQuery;
import com.easypan.entity.enums.FileCategoryEnums;
import com.easypan.entity.enums.FileDelFlagEnums;
import com.easypan.exception.BusinessException;
import com.easypan.service.FileInfoService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @Description:文件信息 Controller
 * @author: 洛洛
 * @date: 2024/06/10
 */

@RestController
@RequestMapping("/file")
public class FileInfoController extends CommonFileController{

	@Resource
	private FileInfoService fileInfoService;

	@RequestMapping("loadDataList")
	@GlobalInterceptor
	public ResponseVO loadDataList(HttpSession session, FileInfoQuery query,String category) {
		FileCategoryEnums categoryEnum = FileCategoryEnums.getByCode(category);
		if (null!=categoryEnum){
			query.setFileCategory(categoryEnum.getCategory());
		}
		query.setUserId(getUserInfoFromSession(session).getUserId());
		query.setOrderBy("last_update_time desc");
		query.setDelFlag(FileDelFlagEnums.USING.getFlag());
		PaginationResultVO result = fileInfoService.findListByPage(query);
		return getSuccessResponseVO(convert2PaginationVO(result, FileInfoVO.class));
	}

	@RequestMapping("uploadFile")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO uploadFile(HttpSession session,
								 String fileId,
								 MultipartFile file,
								 @VerifyParam(required = true) String fileName,
								 @VerifyParam(required = true) String  filePid,
								 @VerifyParam(required = true) String fileMd5,
								 @VerifyParam(required = true) Integer chunkIndex,
								 @VerifyParam(required = true) Integer chunks) throws BusinessException {
		SessionWebUserDto webUserDto = getUserInfoFromSession(session);
		UploadResultDto resultDto = fileInfoService.uploadFile(webUserDto, fileId, file, fileName, filePid, fileMd5, chunkIndex, chunks);
		return getSuccessResponseVO(resultDto);
	}

	@RequestMapping("getImage/{imageFolder}/{imageName}")
	@GlobalInterceptor(checkParams = true)
	public void getImage(HttpServletResponse response, @PathVariable("imageFolder") String imageFolder
			,@PathVariable("imageName") String imageName){
			super.getImage(response, imageFolder, imageName);
	}

	@RequestMapping("/newFolder")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO newFolder(HttpSession session,
								@VerifyParam(required = true)String filePid,
								@VerifyParam(required = true)String fileName) throws BusinessException {
		SessionWebUserDto webUserDto = getUserInfoFromSession(session);
		FileInfo fileInfo = fileInfoService.newFolder(filePid, webUserDto.getUserId(), fileName);
		return getSuccessResponseVO(fileInfo);
	}

	@RequestMapping("/getFolderInfo")
	@GlobalInterceptor(checkParams = true)
	public ResponseVO getFolderInfo(HttpSession session,
								@VerifyParam(required = true)String path) throws BusinessException {
		SessionWebUserDto webUserDto = getUserInfoFromSession(session);
		return super.getFolderInfo(path,webUserDto.getUserId());
	}
}