package indi.simple.pms.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import indi.simple.pms.entity.dataobject.SystemLogDO;
import indi.simple.pms.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wanglunhui
 * @Date: 2021/6/7 23:22
 * @Description:
 */
@RestController
@RequestMapping("/system/common")
@RequiredArgsConstructor
public class SystemCommonController {
    private final SystemUserService systemUserService;
    private final SystemRoleService systemRoleService;
    private final SystemDepartmentService systemDepartmentService;
    private final SystemJobService systemJobService;
    private final SystemLogService systemLogService;

    @GetMapping("/get/dashboard")
    public ResponseEntity getDashboard(){
        LocalDateTime localDateTime=LocalDateTime.now();

        // 用户数量
        int userCount= systemUserService.count();

        // 角色数量
        int roleCount= systemRoleService.count();

        // 部门数量
        int departmentCount=systemDepartmentService.count();

        // 职位数量
        int jobCount= systemJobService.count();

        // 如果今天周六，todayOfWeek为DayOfWeek.SATURDAY，mondayToToday为5
        DayOfWeek todayOfWeek=localDateTime.getDayOfWeek();
        int mondayToToday=todayOfWeek.compareTo(DayOfWeek.MONDAY);// 从周一到今天的天数
        List<Integer> thisWeekLogCountList=new ArrayList<>();
        for (int i=mondayToToday;i>=0;i--){// 本周内今天之前的从数据库获取
            LocalDateTime thatLocalDateTime=localDateTime.plusDays(-i);// 减去天数得到那天的日期

            int thatDayLogCount= systemLogService.count(Wrappers.<SystemLogDO>lambdaQuery().apply("date(create_time)={0}",thatLocalDateTime.toLocalDate().toString()));

            thisWeekLogCountList.add(thatDayLogCount);
        }
        for (int i=thisWeekLogCountList.size()+1;i<=7;i++){// 本周内今天之后的都为0，因为还没到那天
            thisWeekLogCountList.add(0);
        }

        Map<String,Object> map=new HashMap<>();
        map.put("userCount",userCount);
        map.put("roleCount",roleCount);
        map.put("departmentCount",departmentCount);
        map.put("jobCount",jobCount);
        map.put("thisWeekLogCountList",thisWeekLogCountList);

        return ResponseEntity.ok(map);
    }
}
