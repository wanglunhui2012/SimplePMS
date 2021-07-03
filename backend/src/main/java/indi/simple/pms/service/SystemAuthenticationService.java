package indi.simple.pms.service;

import indi.simple.pms.entity.datatransferobject.SystemLoginDTO;
import indi.simple.pms.entity.viewobject.SystemTokenVO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/26 22:56
 * @Description:
 */
public interface SystemAuthenticationService {
    public String getCaptcha(String uuid, OutputStream os) throws IOException;
    public SystemTokenVO login(SystemLoginDTO systemLoginDTO, HttpServletRequest request);
    public boolean logout(HttpServletRequest request);
}
