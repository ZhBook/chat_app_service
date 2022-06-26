package com.tensua.data;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tensua.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> extends BaseResult implements Serializable{
    private static final long serialVersionUID = -275582248840137389L;
    private int pageIndex;
    private int pageSize;
    private long total;

    /**
     * 分页返回
     * @param body
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> pageSuccess(IPage<T> body) {
        PageResult result = new PageResult();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        result.setPageIndex((int) body.getCurrent());
        result.setPageSize((int) body.getSize());
        result.setTotal(body.getTotal());
        result.setData(body.getRecords());
        return result;
    }

    public static <T> PageResult pageSuccess(List<T> body) {
        PageResult result = new PageResult();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        result.setPageIndex(0);
        result.setPageSize(100);
        result.setTotal(body.size());
        result.setData(body);
        return result;
    }
}
