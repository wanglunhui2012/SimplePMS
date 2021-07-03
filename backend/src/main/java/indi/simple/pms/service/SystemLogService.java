package indi.simple.pms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import indi.simple.pms.entity.dataobject.SystemLogDO;
import indi.simple.pms.entity.datatransferobject.SystemLogSearchDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 系统日志表 system_log 服务类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:19
 */
public interface SystemLogService extends IService<SystemLogDO> {
    int getIPCount(String beginDateTime, String endDateTime);

    int getPageCount(String beginDateTime, String endDateTime);

    void download(HttpServletResponse response) throws IOException;

    IPage<SystemLogDO> searchPage(SystemLogSearchDTO systemLogSearchDTO);
}
