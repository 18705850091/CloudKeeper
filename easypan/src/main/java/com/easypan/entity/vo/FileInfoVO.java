package com.easypan.entity.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.easypan.entity.enums.DateTimePatternEnum;
import com.easypan.utils.DateUtils;

/**
 * @Description:文件信息
 * @author: 洛洛
 * @date: 2024/06/10
 */
public class FileInfoVO {
    /**
     * 文件ID
     */
    private String fileId;

    /**
     * 父级ID
     */
    private String filePid;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 封面
     */
    private String fileCover;

    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    /**
     * 0:文件 1:目录
     */
    private Integer folderType;

    /**
     * 文件分类 1:视频 2:音频 3:图片 4:文档 5:其他
     */
    private Integer fileCategory;

    /**
     * 1:视频 2:音频 3:图片 4:pdf 5:doc 6:excel 7:txt 8:code 9:zip 10:其他
     */
    private Integer fileType;

    /**
     * 0:转码中 1:转码失败 2:转码成功
     */
    private Integer status;

    /**
     * 进入回收站时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recoveryTime;

    /**
     * 标记删除 0:删除 1:回收站 2:正常
     */
    private Integer delFlag;

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileId() {
        return this.fileId;
    }


    public void setFilePid(String filePid) {
        this.filePid = filePid;
    }

    public String getFilePid() {
        return this.filePid;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileCover(String fileCover) {
        this.fileCover = fileCover;
    }

    public String getFileCover() {
        return this.fileCover;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setFolderType(Integer folderType) {
        this.folderType = folderType;
    }

    public Integer getFolderType() {
        return this.folderType;
    }

    public void setFileCategory(Integer fileCategory) {
        this.fileCategory = fileCategory;
    }

    public Integer getFileCategory() {
        return this.fileCategory;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Integer getFileType() {
        return this.fileType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setRecoveryTime(Date recoveryTime) {
        this.recoveryTime = recoveryTime;
    }

    public Date getRecoveryTime() {
        return this.recoveryTime;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getDelFlag() {
        return this.delFlag;
    }

    @Override
    public String toString() {
        return "文件ID:" + (fileId == null ? "空" : fileId) + ",父级ID:" + (filePid == null ? "空" : filePid) + ",文件大小:" + (fileSize == null ? "空" : fileSize) + ",文件名:" + (fileName == null ? "空" : fileName) + ",封面:" + (fileCover == null ? "空" : fileCover) +  ",0:文件 1:目录:" + (folderType == null ? "空" : folderType) + ",文件分类 1:视频 2:音频 3:图片 4:文档 5:其他:" + (fileCategory == null ? "空" : fileCategory) + ",1:视频 2:音频 3:图片 4:pdf 5:doc 6:excel 7:txt 8:code 9:zip 10:其他:" + (fileType == null ? "空" : fileType) + ",0:转码中 1:转码失败 2:转码成功:" + (status == null ? "空" : status) + ",进入回收站时间:" + (recoveryTime == null ? "空" : DateUtils.format(recoveryTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + ",标记删除 0:删除 1:回收站 2:正常:" + (delFlag == null ? "空" : delFlag) ;
    }
}