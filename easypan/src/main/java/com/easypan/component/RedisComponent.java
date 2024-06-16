package com.easypan.component;

import com.easypan.entity.constants.Constants;
import com.easypan.entity.dto.SysSettingDto;
import com.easypan.entity.dto.UseSpaceDto;
import com.easypan.entity.po.FileInfo;
import com.easypan.entity.query.FileInfoQuery;
import com.easypan.mappers.FileInfoMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("redisComponent")
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private FileInfoMapper<FileInfo, FileInfoQuery> fileInfoMapper;

    public SysSettingDto getSysSettingDto() {
        SysSettingDto sysSettingDto = (SysSettingDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
        if (null == sysSettingDto) {
            sysSettingDto = new SysSettingDto();
            redisUtils.set(Constants.REDIS_KEY_SYS_SETTING, sysSettingDto);
        }
        return sysSettingDto;
    }

    public void saveUseSpaceUse(String userId, UseSpaceDto useSpaceDto) {
        redisUtils.setex(Constants.REDIS_KEY_USER_SPACE_USE + userId, useSpaceDto, Constants.REDIS_KEY_EXPIRES_DAY);
    }

    public UseSpaceDto getUseSpaceUse(String userId) {
        UseSpaceDto spaceDto = (UseSpaceDto) redisUtils.get(Constants.REDIS_KEY_USER_SPACE_USE + userId);
        if (spaceDto == null) {
            spaceDto = new UseSpaceDto();
            Long useSpace = fileInfoMapper.selectUseSpace(userId);
            spaceDto.setTotalSpace(getSysSettingDto().getUserInitUseSpace() * Constants.MB);
            saveUseSpaceUse(userId, spaceDto);
        }
        return spaceDto;
    }

    public void saveFileTempSize(String userId,String fileId,Long fileSize){
        Long currentSize = getFileTempSize(userId, fileId);
        redisUtils.setex(Constants.REDIS_KEY_USER_FILE_TEMP_SIZE+userId + fileId, currentSize+fileSize,Constants.REDIS_KEY_EXPIRES_ONE_HOUR);
    }

    //获取临时文件大小
    public Long getFileTempSize(String userId,String fileId){
        Long currentSize = getFileSizeFromRedis(Constants.REDIS_KEY_USER_FILE_TEMP_SIZE + userId + fileId);
        return currentSize;
    }

    private Long getFileSizeFromRedis(String key){
        Object sizeObj = redisUtils.get(key);
        if (sizeObj==null){
            return 0L;
        }
        if (sizeObj instanceof Integer){
            return ((Integer)sizeObj).longValue();
        }else if (sizeObj instanceof Long){
            return (Long) sizeObj;
        }
        return 0L;
    }
}