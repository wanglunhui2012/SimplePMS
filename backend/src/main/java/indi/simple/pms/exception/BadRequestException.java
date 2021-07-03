package indi.simple.pms.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:22
 * @Description:
 */
@Getter
@Setter
public class BadRequestException extends RuntimeException {
    private Integer status;

    public BadRequestException(String msg) {
        this(HttpStatus.BAD_REQUEST,msg);
    }

    public BadRequestException(HttpStatus status, String msg) {
        super(msg);
        this.status = status.value();
    }
}