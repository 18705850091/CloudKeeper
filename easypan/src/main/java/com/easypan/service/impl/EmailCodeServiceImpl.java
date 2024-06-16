package com.easypan.service.impl;

import com.easypan.component.RedisComponent;
import com.easypan.entity.config.AppConfig;
import com.easypan.entity.constants.Constants;
import com.easypan.entity.dto.SysSettingDto;
import com.easypan.entity.po.UserInfo;
import com.easypan.entity.query.SimplePage;
import com.easypan.entity.query.UserInfoQuery;
import com.easypan.entity.enums.PageSize;
import com.easypan.entity.vo.PaginationResultVO;
import com.easypan.entity.po.EmailCode;
import com.easypan.entity.query.EmailCodeQuery;
import com.easypan.exception.BusinessException;
import com.easypan.mappers.EmailCodeMapper;
import com.easypan.mappers.UserInfoMapper;
import com.easypan.service.EmailCodeService;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import com.easypan.utils.StringTools;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description:邮箱验证码Service
 * @author: 洛洛
 * @date: 2024/06/06
 */

@Service("emailCodeService")
public class EmailCodeServiceImpl implements EmailCodeService {

    private static final Logger logger = LoggerFactory.getLogger(EmailCodeServiceImpl.class);

    @Resource
    private EmailCodeMapper<EmailCode, EmailCodeQuery> emailCodeMapper;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private AppConfig appConfig;

    @Resource
    private RedisComponent redisComponent;
    /**
     * 根据条件查询列表
     */
    public List<EmailCode> findListByParam(EmailCodeQuery query) {
        return this.emailCodeMapper.selectList(query);
    }

    /**
     * 根据条件查询数量
     */
    public Integer findCountByParam(EmailCodeQuery query) {
        return this.emailCodeMapper.selectCount(query);
    }

    /**
     * 分页查询
     */
    public PaginationResultVO<EmailCode> findListByPage(EmailCodeQuery query) {
        Integer count = this.findCountByParam(query);
        int pageSize = query.getPageSize() == null ? PageSize.SIZE15.getSize() : query.getPageSize();
        SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
        query.setSimplePage(page);
        List<EmailCode> list = this.findListByParam(query);
        PaginationResultVO<EmailCode> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    public Integer add(EmailCode bean) {
        return this.emailCodeMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    public Integer addBatch(List<EmailCode> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.emailCodeMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或修改
     */
    public Integer addOrUpdateBatch(List<EmailCode> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.emailCodeMapper.insertOrUpdateBatch(listBean);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendEmailCode(String email, Integer type) throws BusinessException {
        if (type == Constants.ZERO) {
            UserInfo userInfo = userInfoMapper.selectByEmail(email);
            if (null != userInfo) {
                throw new BusinessException("邮箱已经存在");
            }
        }
        String code = StringTools.getRandomNumber(Constants.LENGTH_5);

        //TODO 发送验证码
        sendMailCode(email, code);
        //将之前发的验证码值为无效
        emailCodeMapper.disableEmailCode(email);

        EmailCode emailCode = new EmailCode();
        emailCode.setCode(code);
        emailCode.setEmail(email);
        emailCode.setStatus(Constants.ZERO);
        emailCode.setCreateTime(new Date());
        emailCodeMapper.insert(emailCode);
    }

    @Override
    public void checkCode(String email, String code) throws BusinessException {
        EmailCode emailCode = this.emailCodeMapper.selectByEmailAndCode(email, code);
        if (null==emailCode){
            throw new BusinessException("邮箱验证码不正确");
        }
        if (emailCode.getStatus()==1||System.currentTimeMillis()-emailCode.getCreateTime().getTime()>Constants.LENGTH_15*1000*60){
            throw new BusinessException("邮箱验证码已失效");
        }
        emailCodeMapper.disableEmailCode(email);
    }

    private void sendMailCode(String toEmail, String code) throws BusinessException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(appConfig.getSendUserName());
            helper.setTo(toEmail);

            SysSettingDto sysSettingDto = redisComponent.getSysSettingDto();

            helper.setSubject(sysSettingDto.getRegisterMailTitle());
            helper.setText(String.format(sysSettingDto.getRegisterEmailContent(),code));
            helper.setSentDate(new Date());
            javaMailSender.send(message);
        } catch (Exception e) {
            logger.error("邮件发送失败", e);
            throw new BusinessException("邮件发送失败");
        }
    }
}