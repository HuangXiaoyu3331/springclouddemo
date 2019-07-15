package com.hxy.product.server;

import com.github.dozermapper.core.Mapper;
import com.hxy.common.utils.DozerUtil;
import com.hxy.product.client.vo.ProductVo;
import com.hxy.product.client.vo.ProductVo2;
import com.hxy.product.server.bean.model.ProductModel;
import com.hxy.product.server.bean.dozer.test.DesDeepObject;
import com.hxy.product.server.bean.dozer.test.SrcDeepObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * dozer简单使用示例
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: dozerTest
 * @date 2019年07月12日 17:56:26
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class dozerTest {

    @Autowired
    private Mapper dozerMapper;
    private ProductModel productModel;
    private List<ProductModel> productModelList;
    private SrcDeepObject srcDeepObject;

    @Before
    public void initProductModel() {
        productModelList = new ArrayList<>();

        productModel = new ProductModel();
        productModel.setId(1L);
        productModel.setName("测试");
        productModel.setCreateTime(new Date());
        productModel.setPrice(new BigDecimal("2323.11"));

        productModelList.add(productModel);

        srcDeepObject = new SrcDeepObject();
        srcDeepObject.setName("hxy");
        srcDeepObject.setSrcProductModel(productModel);
    }

    /**
     * dozer对象简单映射示例
     * 根据属性名映射，两个类相同的属性名可以映射(忽略属性的修饰符)
     */
    @Test
    public void simpleTest() {
        ProductVo productVo = dozerMapper.map(productModel, ProductVo.class);
        log.info(productVo.toString());
    }

    /**
     * 单个字段级别的映射
     * 对于两个对象有不同属性名的属性需要映射时，可以自定义映射文件，指定属性名的对应关系
     * ProductModel -> ProductVo2 的映射文件是 resources/mapping/productModelMapping.xml
     */
    @Test
    public void individualFieldLevelTest() {
        ProductVo2 productVo2 = dozerMapper.map(productModel, ProductVo2.class);
        log.info(productVo2.toString());
    }

    /**
     * List与List之间的映射
     */
    @Test
    public void mapListTest() {
        List<ProductVo2> productVoList = DozerUtil.mapList(dozerMapper, productModelList, ProductVo2.class);
        log.info(productVoList.toString());
    }

    /**
     * 深复制
     * 深复制的话，如果属性是对象，在xml指定对象属性的映射关系
     * SrcDeepObject -> DesDeepObject 的映射关系文件是 desDeepObjectMapping.xml
     */
    @Test
    public void deepMappingTest() {
        DesDeepObject desDeepObject = dozerMapper.map(srcDeepObject, DesDeepObject.class);
        log.info(desDeepObject.toString());
    }

}
