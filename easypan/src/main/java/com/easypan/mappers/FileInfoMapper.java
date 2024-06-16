package com.easypan.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description:文件信息 Mapper
 * @author: 洛洛
 * @date: 2024/06/10
 */
public interface FileInfoMapper<T, P> extends BaseMapper {
	/**
	 * 根据FileIdAndUserId查询
	 */
	T selectByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") String userId);

	/**
	 * 根据FileIdAndUserId更新
	 */
	Integer updateByFileIdAndUserId(@Param("bean") T t, @Param("fileId") String fileId, @Param("userId") String userId);

	/**
	 * 根据FileIdAndUserId删除
	 */
	Integer deleteByFileIdAndUserId(@Param("fileId") String fileId, @Param("userId") String userId);

	Long selectUseSpace(@Param("userId") String userId);

	void updateFileStatusWithOldStatus(@Param("fileId") String fileId, @Param("userId") String userId,@Param("bean") T t,@Param("oldStatus") Integer oldStatus);

}