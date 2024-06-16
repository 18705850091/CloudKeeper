package com.easypan.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.easypan.component.RedisComponent;
import com.easypan.entity.config.AppConfig;
import com.easypan.entity.constants.Constants;
import com.easypan.entity.dto.SessionWebUserDto;
import com.easypan.entity.dto.UploadResultDto;
import com.easypan.entity.dto.UseSpaceDto;
import com.easypan.entity.po.UserInfo;
import com.easypan.entity.query.SimplePage;
import com.easypan.entity.query.UserInfoQuery;
import com.easypan.entity.enums.*;
import com.easypan.entity.vo.PaginationResultVO;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.query.FileInfoQuery;
import com.easypan.exception.BusinessException;
import com.easypan.mappers.FileInfoMapper;
import com.easypan.mappers.UserInfoMapper;
import com.easypan.service.FileInfoService;

import javax.annotation.Resource;

import com.easypan.utils.DateUtils;
import com.easypan.utils.ProcessUtils;
import com.easypan.utils.ScaleFilter;
import com.easypan.utils.StringTools;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @Description:文件信息 Service
 * @author: 洛洛
 * @date: 2024/06/10
 */

@Service("fileInfoService")
public class FileInfoServiceImpl implements FileInfoService {

    private static final Logger logger = LoggerFactory.getLogger(FileInfoServiceImpl.class);

    @Resource
    private FileInfoMapper<FileInfo, FileInfoQuery> fileInfoMapper;

    @Resource
    private RedisComponent redisComponent;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Resource
    private AppConfig appConfig;

    @Resource
    @Lazy
    private FileInfoServiceImpl fileInfoService;

    /**
     * 根据条件查询列表
     */
    public List<FileInfo> findListByParam(FileInfoQuery query) {
        return this.fileInfoMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(FileInfoQuery query) {
        return this.fileInfoMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    public PaginationResultVO<FileInfo> findListByPage(FileInfoQuery query) {
        Integer count = this.findCountByParam(query);
        int pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<FileInfo> list = this.findListByParam(query);
        PaginationResultVO<FileInfo> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(FileInfo bean) {
        return this.fileInfoMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<FileInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.fileInfoMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或修改
     */
    public Integer addOrUpdateBatch(List<FileInfo> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.fileInfoMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据FileIdAndUserId查询
     */
    public FileInfo getFileInfoByFileIdAndUserId(String fileId, String userId) {
        return this.fileInfoMapper.selectByFileIdAndUserId(fileId, userId);
    }

    /**
     * 根据FileIdAndUserId更新
     */
    public Integer updateFileInfoByFileIdAndUserId(FileInfo bean, String fileId, String userId) {
        return this.fileInfoMapper.updateByFileIdAndUserId(bean, fileId, userId);
    }

    /**
     * 根据FileIdAndUserId删除
     */
    public Integer deleteFileInfoByFileIdAndUserId(String fileId, String userId) {
        return this.fileInfoMapper.deleteByFileIdAndUserId(fileId, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UploadResultDto uploadFile(SessionWebUserDto webUserDto, String fileId, MultipartFile file, String fileName, String filePid, String fileMd5, Integer chunkIndex, Integer chunks) throws BusinessException {
        UploadResultDto resultDto = new UploadResultDto();
        Boolean uploadSuccess = true;
        File tempFileFolder = null;
        try {
            if (StringTools.isEmpty(fileId)) {
                fileId = StringTools.getRandomNumber(Constants.LENGTH_10);
            }
            resultDto.setFileId(fileId);
            Date curDate = new Date();
            UseSpaceDto spaceDto = redisComponent.getUseSpaceUse(webUserDto.getUserId());

            if (chunkIndex == 0) {
                FileInfoQuery infoQuery = new FileInfoQuery();
                infoQuery.setFileMd5(fileMd5);
                infoQuery.setSimplePage(new SimplePage(0, 1));
                infoQuery.setStatus(FileStatusEnums.USING.getStatus());
                List<FileInfo> dbFileList = this.fileInfoMapper.selectList(infoQuery);
                //秒传
                if (!dbFileList.isEmpty()) {
                    FileInfo dbFile = dbFileList.get(0);
                    //判断文件大小
                    if (dbFile.getFileSize() + spaceDto.getUseSpace() > spaceDto.getTotalSpace()) {
                        throw new BusinessException(ResponseCodeEnum.CODE_904);
                    }

                    dbFile.setFileId(fileId);
                    dbFile.setFilePid(filePid);
                    dbFile.setUserId(webUserDto.getUserId());
                    dbFile.setCreateTime(curDate);
                    dbFile.setLastUpdateTime(curDate);
                    dbFile.setStatus(FileStatusEnums.USING.getStatus());
                    dbFile.setDelFlag(FileDelFlagEnums.USING.getFlag());
                    dbFile.setFileMd5(fileMd5);
                    //文件重命名
                    fileName = autoRename(filePid, webUserDto.getUserId(), fileName);
                    dbFile.setFileName(fileName);
                    this.fileInfoMapper.insert(fileName);
                    resultDto.setStatus(UploadStatusEnums.UPLOAD_SECONDS.getCode());
                    //更新用户使用空间
                    updateUseSpace(webUserDto, dbFile.getFileSize());
                    return resultDto;
                }
                //判断磁盘空间

                Long currentTempSize = redisComponent.getFileTempSize(webUserDto.getUserId(), fileId);
                if (file.getSize() + currentTempSize + spaceDto.getUseSpace() > spaceDto.getTotalSpace()) {
                    throw new BusinessException(ResponseCodeEnum.CODE_904);
                }
            }
            //暂存临时目录
            String tempFolderName = appConfig.getProjectFolder() + Constants.FILE_FOLDER_TEMP;
            String currentUserFolderName = webUserDto.getUserId() + fileId;

            tempFileFolder = new File(tempFolderName + currentUserFolderName);
            if (!tempFileFolder.exists()) {
                tempFileFolder.mkdirs();
            }

            File newFile = new File(tempFileFolder.getPath() + "/" + chunkIndex);
            file.transferTo(newFile);
            if (chunkIndex < chunks - 1) {
                resultDto.setStatus(UploadStatusEnums.UPLOADING.getCode());
                //保存临时大小
                redisComponent.saveFileTempSize(webUserDto.getUserId(), fileId, file.getSize());
                return resultDto;
            }

            redisComponent.saveFileTempSize(webUserDto.getUserId(), fileId, file.getSize());
            //最后一个分片上传完成，记录数据库，异步合成分片
            String month = DateUtils.format(new Date(), DateTimePatternEnum.YYYYMM.getPattern());
            String fileSuffix = StringTools.getFileSuffix(fileName);
            //真实文件名
            String realFileName = currentUserFolderName+fileSuffix;
            FileTypeEnums fileTypeEnums = FileTypeEnums.getFileTypeBySuffix(fileSuffix);
            //自动重命名
            fileName = autoRename(filePid, webUserDto.getUserId(), fileName);

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileId(fileId);
            fileInfo.setUserId(webUserDto.getUserId());
            fileInfo.setFileMd5(fileMd5);
            fileInfo.setFileName(fileName);
            fileInfo.setFilePath(month + "/" + realFileName);
            fileInfo.setFilePid(filePid);
            fileInfo.setCreateTime(curDate);
            fileInfo.setLastUpdateTime(curDate);
            fileInfo.setFileCategory(fileTypeEnums.getCategory().getCategory());
            fileInfo.setFileType(fileTypeEnums.getType());
            fileInfo.setStatus(FileStatusEnums.TRANSFER.getStatus());
            fileInfo.setFolderType(FileFolderTypeEnums.FILE.getType());
            fileInfo.setDelFlag(FileDelFlagEnums.USING.getFlag());
            this.fileInfoMapper.insert(fileInfo);
            Long totalSize = redisComponent.getFileTempSize(webUserDto.getUserId(), fileId);
            updateUseSpace(webUserDto, totalSize);
            resultDto.setStatus(UploadStatusEnums.UPLOAD_FINISH.getCode());

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    fileInfoService.transferFile(fileInfo.getFileId(), webUserDto);//
                }
            });
            return resultDto;
        } catch (BusinessException e) {
            logger.error("文件上传失败", e);
            uploadSuccess = false;
            throw e;
        }catch (Exception e) {
            logger.error("文件上传失败", e);
            uploadSuccess = false;
        }finally {
            if (!uploadSuccess&&tempFileFolder!=null){
                try {
                    FileUtils.deleteDirectory(tempFileFolder);
                } catch (IOException e) {
                    logger.error("删除临时目录失败", e);
                }
            }
        }

        return resultDto;
    }


    private String autoRename(String filePid, String userId, String fileName) {
        FileInfoQuery fileInfoQuery = new FileInfoQuery();
        fileInfoQuery.setUserId(userId);
        fileInfoQuery.setFilePid(filePid);
        fileInfoQuery.setDelFlag(FileDelFlagEnums.USING.getFlag());
        fileInfoQuery.setFileName(fileName);
        Integer count = this.fileInfoMapper.selectCount(fileInfoQuery);
        if (count > 0) {
            fileName = StringTools.rename(fileName);
        }
        return fileName;
    }

    private void updateUseSpace(SessionWebUserDto webUserDto, Long useSpace) throws BusinessException {
        Integer count = userInfoMapper.updateUseSpace(webUserDto.getUserId(), useSpace, null);
        if (count == 0) {
            throw new BusinessException(ResponseCodeEnum.CODE_904);
        }
        UseSpaceDto spaceDto = redisComponent.getUseSpaceUse(webUserDto.getUserId());
        spaceDto.setUseSpace(spaceDto.getUseSpace() + useSpace);
        redisComponent.saveUseSpaceUse(webUserDto.getUserId(), spaceDto);
    }

    @Async
    public void transferFile(String fileId,SessionWebUserDto webUserDto){
        Boolean transferSuccess = true;
        String targetFilePath = null;
        String cover = null;
        FileTypeEnums fileTypeEnum = null;
        FileInfo fileInfo  = this.fileInfoMapper.selectByFileIdAndUserId(fileId, webUserDto.getUserId());
        try{
            if (fileInfo == null||!FileStatusEnums.TRANSFER.getStatus().equals(fileInfo.getStatus())){
                return;
            }
            //临时目录
            String tempFolderName = appConfig.getProjectFolder()+Constants.FILE_FOLDER_TEMP;
            String currentUserFolderName = webUserDto.getUserId()+fileId;
            File fileFolder = new File(tempFolderName+currentUserFolderName);

            String fileSuffix = StringTools.getFileSuffix(fileInfo.getFileName());
            String month = DateUtils.format(fileInfo.getCreateTime(),DateTimePatternEnum.YYYYMM.getPattern());
            //目标目录
            String targetFolderName = appConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE;
            File targetFolder = new File(targetFolderName + "/" + month);
            if (!targetFolder.exists()){
                targetFolder.mkdirs();
            }
            //真实的文件名
            String realFileName = currentUserFolderName + fileSuffix;
            targetFilePath = targetFolder.getPath() + "/" + realFileName;

            //合并文件
            union(fileFolder.getPath(), targetFilePath, fileInfo.getFileName(), true);
            //视频文件分割
            fileTypeEnum = FileTypeEnums.getFileTypeBySuffix(fileSuffix);
            if(FileTypeEnums.VIDEO==fileTypeEnum){
                cutFile4Video(fileId, targetFilePath);
                //视频生成缩略图
                cover = month + "/" + currentUserFolderName+Constants.IMAGE_PNG_SUFFIX;
                String coverPath = targetFolderName + "/" + cover;
                ScaleFilter.createCover4Video(new File(targetFilePath) , Constants.LENGTH_150,new File(coverPath));
            }else if (FileTypeEnums.IMAGE==fileTypeEnum){
                //生成缩略图
                cover = month + "/" + realFileName.replace(".", "pic.");
                String coverPath = targetFolderName + "/"+cover;
                Boolean created = ScaleFilter.createThumbnailWidthFFmpeg(new File(targetFilePath), Constants.LENGTH_150, new File(coverPath), false);
                if (!created){
                    FileUtils.copyFile(new File(targetFilePath), new File(coverPath));
                }
            }
        }catch (Exception e){
            logger.error("文件转码失败,文件ID:{},userId:{}",fileId,webUserDto.getUserId(),e);
            transferSuccess = false;
        }finally {
            FileInfo updateInfo = new FileInfo();
            updateInfo.setFileSize(new File(targetFilePath).length());
            updateInfo.setFileCover(cover);
            updateInfo.setStatus(transferSuccess?FileStatusEnums.USING.getStatus():FileStatusEnums.TRANSFER.getStatus());
            fileInfoMapper.updateFileStatusWithOldStatus(fileId, webUserDto.getUserId(),  updateInfo, FileStatusEnums.TRANSFER.getStatus());

        }
    }

    private void union(String dirPath,String toFilePath,String fileName,Boolean delSource) throws BusinessException {
        File dir = new File(dirPath);
        if (!dir.exists()){
            throw new BusinessException("目录不存在");
        }

        File[] fileList = dir.listFiles();
        File targetFile = new File(toFilePath);
        RandomAccessFile writeFile = null;
        try {
            writeFile = new RandomAccessFile(targetFile, "rw");
            byte[] b = new byte[1024 * 10];
            for (int i = 0; i < fileList.length; i++) {
                int len = -1;
               File chunkFile = new File(dirPath + "/" + i);
               RandomAccessFile readFile = null;
               try {
                   readFile = new RandomAccessFile(chunkFile, "r");
                   while ((len = readFile.read(b)) != -1){
                       writeFile.write(b,0,len);
                   }
               }catch (Exception e){
                   logger.error("合并分片失败");
                   throw new BusinessException("合并分片失败");
               }finally {
                   readFile.close();
               }
            }
        }catch (Exception e){
            logger.error("合并文件:{}失败",fileName,e);
            throw new BusinessException("合并文件"+fileName+"出错了");
        }finally {
            if (null!=writeFile){
                try {
                    writeFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (delSource&&dir.exists()){
                try {
                    FileUtils.deleteDirectory(dir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void cutFile4Video(String fileId,String videoFilePath) throws BusinessException {
        //创建同名切片目录
        File tsFolder = new File(videoFilePath.substring(0,videoFilePath.lastIndexOf(".")));
        if (!tsFolder.exists()){
            tsFolder.mkdirs();
        }
        final String CMD_TRANSFER_2TS = "ffmpeg -y -i %s  -vcodec copy -acodec copy -vbsf h264_mp4toannexb %s";
        final String CMD_CUT_TS = "ffmpeg -i %s -c copy -map 0 -f segment -segment_list %s -segment_time 30 %s/%s_%%4d.ts";

        String tsPath = tsFolder + "/" + Constants.TS_NAME;
        //生成.ts
        String cmd = String.format(CMD_TRANSFER_2TS, videoFilePath, tsPath);
        ProcessUtils.executeCommand(cmd, false);
        cmd = String.format(CMD_CUT_TS, tsPath, tsFolder.getPath() + "/" + Constants.M3U8_NAME, tsFolder.getPath(), fileId);
        ProcessUtils.executeCommand(cmd, false);
        new File(tsPath).delete();
    }

    @Override
    public FileInfo newFolder(String filePid, String userId, String folderName) throws BusinessException {
        checkFileName(filePid, userId, folderName, FileFolderTypeEnums.FOLDER.getType());
        Date curDate = new Date();
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileId(StringTools.getRandomString(Constants.LENGTH_10));
        fileInfo.setUserId(userId);
        fileInfo.setFilePid(filePid);
        fileInfo.setFileName(folderName);
        fileInfo.setFolderType(FileFolderTypeEnums.FOLDER.getType());
        fileInfo.setCreateTime(curDate);
        fileInfo.setLastUpdateTime(curDate);
        fileInfo.setStatus(FileStatusEnums.USING.getStatus());
        fileInfo.setDelFlag(FileDelFlagEnums.USING.getFlag());
        this.fileInfoMapper.insert(fileInfo);
        return fileInfo;
    }

    private void checkFileName(String filePid,String userId,String fileName,Integer folderType) throws BusinessException {
        FileInfoQuery fileInfoQuery = new FileInfoQuery();
        fileInfoQuery.setFolderType(folderType);
        fileInfoQuery.setFileName(fileName);
        fileInfoQuery.setFilePid(filePid);
        fileInfoQuery.setUserId(userId);
        Integer count = this.fileInfoMapper.selectCount(fileInfoQuery);
        if (count > 0){
            throw new BusinessException("此目录下以存在同名文件，请修改名称");
        }
    }
}