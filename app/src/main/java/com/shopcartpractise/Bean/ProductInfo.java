package com.shopcartpractise.Bean;

/**
 * User: 张亚博
 * Date: 2017-11-01 21:16
 * Description：
 */
public class ProductInfo {
    private String ProductName;//商品名
    private String ProductPrice;//商品价格
    private boolean Selected;//是否选中

    public int getProductNum() {
        return ProductNum;
    }

    public void setProductNum(int productNum) {
        ProductNum = productNum;
    }

    private int  ProductNum;//商品数量

    public ProductInfo(String productName, String productPrice, boolean selected) {
        ProductName = productName;
        ProductPrice = productPrice;
        Selected = selected;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }
}
