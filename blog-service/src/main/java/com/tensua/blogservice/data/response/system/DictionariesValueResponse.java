package com.tensua.blogservice.data.response.system;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author: zhooke
 * @create: 2022-04-29 11:42
 * @description:
 **/
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DictionariesValueResponse {

    Integer id;
    /**
     * 字典类型
     */
    String typeName;

    /**
     * 字典对应值
     */
    String typeValue;
}
