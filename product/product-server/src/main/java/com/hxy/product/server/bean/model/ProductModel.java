package com.hxy.product.server.bean.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductModel {
    private Long id;

    private String name;

    private BigDecimal price;

    private Date createTime;

    private Date updateTime;
}