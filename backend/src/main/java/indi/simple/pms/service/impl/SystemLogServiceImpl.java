package indi.simple.pms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.simple.pms.entity.dataobject.SystemLogDO;
import indi.simple.pms.entity.datatransferobject.SystemLogSearchDTO;
import indi.simple.pms.mapper.SystemLogMapper;
import indi.simple.pms.service.SystemLogService;
import indi.simple.pms.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 系统日志表 system_log 服务实现类
 *
 * @author wanglunhui
 * @since 2021-04-19 21:10:19
 */
@Service("systemLogService")
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLogDO> implements SystemLogService {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    public IPage<SystemLogDO> getIPageLogDOFromRedis(long pageNum, long pageSize) {
        long startOffset = (pageNum - 1L) * pageSize;
        long endOffset = startOffset + pageSize - 1L;
        Set<Object> set = this.redisTemplate.opsForZSet().range("gzport:log:page", startOffset, endOffset);
        if (set != null && set.size() != 0) {
            int allCount = super.count();
            double temp = (double)allCount / (double)pageSize;
            long allPageNum;
            if ((double)((int)temp) == temp) {
                allPageNum = (long)((int)temp);
            } else {
                allPageNum = (long)((int)temp + 1);
            }

            if (allPageNum == 0L && allCount != 0) {
                allPageNum = 1L;
            }

            if (allPageNum == 0L) {
                return null;
            } else {
                long realPageSize;
                if (pageNum < allPageNum) {
                    realPageSize = pageSize;
                } else if (pageNum == allPageNum) {
                    realPageSize = (long)allCount - pageSize * (pageNum - 1L);
                } else {
                    realPageSize = 0L;
                }

                if ((long)set.size() < realPageSize) {
                    return null;
                } else {
                    IPage<SystemLogDO> systemLogDOIPage = new Page<>();
                    systemLogDOIPage.setCurrent(pageNum);
                    systemLogDOIPage.setSize(pageSize);
                    systemLogDOIPage.setTotal((long)allCount);
                    systemLogDOIPage.setPages(allPageNum);
                    systemLogDOIPage.setRecords(new ArrayList(set));
                    return systemLogDOIPage;
                }
            }
        } else {
            return null;
        }
    }

    public int getIPCount(String beginDateTime, String endDateTime) {
        return ((SystemLogMapper)this.baseMapper).getIPCount(beginDateTime, endDateTime);
    }

    public int getPageCount(String beginDateTime, String endDateTime) {
        return ((SystemLogMapper)this.baseMapper).getPageCount(beginDateTime, endDateTime);
    }

    @Override
    public void download(HttpServletResponse response) throws IOException {
        List<SystemLogDO> systemLogDOList = super.list();
        List<Map<String, Object>> list = new ArrayList<>();

        for (SystemLogDO systemLogDO:systemLogDOList){
            Map<String, Object> map = new LinkedHashMap();
            map.put("用户名", systemLogDO.getUsername());
            map.put("IP", systemLogDO.getIp());
            map.put("描述", systemLogDO.getName());
            map.put("请求耗时/毫秒", systemLogDO.getTime());
            map.put("异常详情", systemLogDO.getException());
            map.put("创建日期", systemLogDO.getCreateTime());
            list.add(map);
        }

        FileUtil.downloadExcel(list, response);
    }

    @Override
    public IPage<SystemLogDO> searchPage(SystemLogSearchDTO systemLogSearchDTO) {
        Long pageNum = Optional.ofNullable(systemLogSearchDTO.getPageNum()).orElse(0L);
        Long pageSize = Optional.ofNullable(systemLogSearchDTO.getPageSize()).orElse(-1L);

        IPage<SystemLogDO> systemLogDOIPage = super.baseMapper.searchPage(new Page<>(pageNum, pageSize), systemLogSearchDTO);
        return systemLogDOIPage;
    }
}
