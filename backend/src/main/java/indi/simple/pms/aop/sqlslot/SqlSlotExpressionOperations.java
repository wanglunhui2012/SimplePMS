package indi.simple.pms.aop.sqlslot;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import indi.simple.pms.entity.businessobject.JwtUserBO;
import indi.simple.pms.entity.dataobject.SystemRoleDO;
import indi.simple.pms.service.SystemDepartmentService;
import indi.simple.pms.service.SystemRoleDepartmentService;
import indi.simple.pms.service.SystemRoleService;
import indi.simple.pms.util.SecurityUtil;
import indi.simple.pms.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: wanglunhui
 * @Date: 2021/5/16 11:32
 * @Description: {@link org.springframework.security.access.expression.SecurityExpressionOperations}
 */
@Component("sqlSlotExpressionOperations")
public class SqlSlotExpressionOperations {
    @Autowired
    private SystemRoleService systemRoleService;
    @Autowired
    private SystemRoleDepartmentService systemRoleDepartmentService;
    @Autowired
    private SystemDepartmentService systemDepartmentService;

    public String dataScope(){
        StringBuilder sqlStringBuilder = new StringBuilder();
        JwtUserBO jwtUserBO = SecurityUtil.getJwtUserBO();

        List<Long> roleIdList=jwtUserBO.getRoleIdList();
        List<SystemRoleDO> systemRoleDOList=systemRoleService.list(Wrappers.<SystemRoleDO>lambdaQuery().in(SystemRoleDO::getId,roleIdList));

        Set<Long> departmentIdSet=new HashSet<>();
        Set<Long> userIdSet=new HashSet<>();
        for (SystemRoleDO systemRoleDO:systemRoleDOList){
            switch(systemRoleDO.getDataScope()) {
                case "0": // 0全部
                    return " or 1=1"; // 直接return
                case "1": // 1自定义
                    departmentIdSet.addAll(systemRoleDepartmentService.getDepartmentIdByRoleId(systemRoleDO.getId()));
                    break;
                case "2": // 2本部门及以下部门
                    departmentIdSet.addAll(systemDepartmentService.getChildrenIdListById(jwtUserBO.getDepartmentId()));
                    break;
                case "3": // 3本部门
                    departmentIdSet.add(jwtUserBO.getDepartmentId());
                    break;
                case "4": // 4仅本人
                    userIdSet.add(jwtUserBO.getId());
                    break;
                default:
                    throw new IllegalArgumentException("未知数据权限值!");
            }
        }
        if(departmentIdSet.size()!=0){
            sqlStringBuilder.append(String.format(" or create_department_id in (%s) ", StringUtil.join(departmentIdSet,",")));
        }
        if(userIdSet.size()!=0){
            sqlStringBuilder.append(String.format(" or create_user_id = %d ", jwtUserBO.getId()));
        }

        return sqlStringBuilder.toString();
    }
}
