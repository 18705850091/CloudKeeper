package com.easypan.service;

import java.io.Serializable;

import com.easypan.entity.dto.SessionWebUserDto;
import com.easypan.entity.dto.UploadResultDto;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.query.FileInfoQuery;
import java.util.List;
import com.easypan.entity.vo.PaginationResultVO;
import com.easypan.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description:文件信息 Service
 * @author: 洛洛
 * @date: 2024/06/10
 */

public interface FileInfoService{

	/**
	 * 根据条件查询列表
	 */
	List<FileInfo> findListByParam(FileInfoQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(FileInfoQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<FileInfo> findListByPage(FileInfoQuery query);

	/**
	 * 新增
	 */
	Integer add(FileInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<FileInfo> listBean);

	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<FileInfo> listBean);

	/**
	 * 根据FileIdAndUserId查询
	 */
	FileInfo getFileInfoByFileIdAndUserId(String fileId, String userId);

	/**
	 * 根据FileIdAndUserId更新
	 */
	Integer updateFileInfoByFileIdAndUserId( FileInfo t, String fileId, String userId);

	/**
	 * 根据FileIdAndUserId删除
	 */
	Integer deleteFileInfoByFileIdAndUserId(String fileId, String userId);

	UploadResultDto uploadFile(SessionWebUserDto webUserDto, String fileId, MultipartFile file, String fileName,
							   String filePid, String fileMd5, Integer chunkIndex, Integer chunks) throws BusinessException;

	FileInfo newFolder(String filePid,String userId,String folderName) throws BusinessException;
}