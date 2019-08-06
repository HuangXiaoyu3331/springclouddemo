package com.hxy.product.server.bean.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品实体类
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ProductClient
 * @date 2019年07月13日 14:15:30
 */
public class ProductModel {
    private Long id;

    private String name;

    private BigDecimal price;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}