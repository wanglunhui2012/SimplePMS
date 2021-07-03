package indi.simple.pms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import indi.simple.pms.entity.dataobject.SystemUserDO;
import indi.simple.pms.entity.datatransferobject.SystemChangePasswordDTO;
import indi.simple.pms.entity.datatransferobject.SystemUserSearchDTO;
import indi.simple.pms.entity.datatransferobject.SystemUserUpdateProfileDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统用户表 system_user 服务类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:32
 */
public interface SystemUserService extends IService<SystemUserDO> {
    boolean add(String json,MultipartFile avatar);

    boolean delete(List<Long> idList);

    boolean update(String json,MultipartFile newAvatar);

    IPage<SystemUserDO> searchPage(SystemUserSearchDTO systemUserSearchDTO);

    boolean changePassword(SystemChangePasswordDTO systemChangePasswordDTO, HttpServletRequest httpServletRequest);

    String uploadAvatar(Long userId, MultipartFile multipartFile);

    boolean updateProfile(SystemUserUpdateProfileDTO systemUserUpdateProfileDTO);

    SystemUserDO getCurrent();

    List<SystemUserDO> getByJobId(Long jobId);

    String getAvatarUrl(Long userId);

    String getNameById(Long userId);

    List<String> getNameListByDepartmentId(List<Long> departmentIdList);
}
