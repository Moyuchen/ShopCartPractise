package com.shopcartpractise;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shopcartpractise.Adapter.ProductAdapter;
import com.shopcartpractise.Bean.ProductInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductAdapter.ClickCheckBox, View.OnClickListener, ProductAdapter.ClickDelete {

    private RecyclerView mRecycleView;
    private TextView  AllPriceTV;
    private TextView  ProductNumTV;
    private CheckBox  QuanXuanCB;
    private List<ProductInfo> products;
    private List<Integer> mPositons=new ArrayList<>();
    private int mSelectedNum;
    private ProductAdapter adapter;
    private boolean isAllSelected=true;
    private String TAG="=================MAINACITVITY=============";
    private float zongjia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //加载视图
        initView();
        //加载数据方法
        initData();
    }

    /**
     * 加载数据方法
     */
    private void initData() {
        products = new ArrayList<>();
        for (int i=0;i<5;i++){
            ProductInfo product=new ProductInfo("商品"+i,"55.4",false);
            product.setProductNum(0);
            products.add(product);
        }

        adapter = new ProductAdapter(products,this);
        mRecycleView.setAdapter(adapter);
        adapter.setClickCheckBox(this);
        adapter.setClickDelete(this);

    }

    /**
     * 加载视图方法
     */
    private void initView() {
        mRecycleView = (RecyclerView) findViewById(R.id.RCV);//RecycleView列表
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        AllPriceTV= (TextView) findViewById(R.id.AP);//加载总价视图
        ProductNumTV= (TextView) findViewById(R.id.PN);//选中的商品数量
        QuanXuanCB= (CheckBox) findViewById(R.id.QX);//全选框

        QuanXuanCB.setOnClickListener(this);


    }

    /**
     * 重写点击CheckBox的点击事件
     * @param positon
     */
    @Override
    public void clickCheckBox(int positon,boolean selected) {
        //清空总价，总件数
        ClearMethod();
        //判断点击的多选框是否选中
        //选中，将当前点击的多选框位置，保存起来，并将所对应的商品设置为true
        if (selected) {
            //将选中的商品地址保存起来
            mPositons.add(positon);
            //将选中的商品设置为选中装态
            ProductInfo productInfo = products.get(positon);
            productInfo.setSelected(true);
        }else {
            //不选中，遍历位置集合
            for (int i=0 ; i<mPositons.size();i++){
                //取出选中的商品位置
                int cPosition = mPositons.get(i);
                //判断商品位置是否与当前选中的商品位置相等
                //如果相等，就把位置移除
                if (cPosition==positon) {
                    mPositons.remove(i);
                    //并将商品设置为不选中装态
                    ProductInfo productInfo = products.get(positon);
                    productInfo.setSelected(false);
                }
            }

        }

        //遍历商品集合,计算总价 和总件数
        JsAllPriceAllNumber();
        //判断位置集合的个数是否等于商品集合的商品件数
        if (mPositons.size()==products.size()) {
            QuanXuanCB.setChecked(true);
        }else {
            QuanXuanCB.setChecked(false);
        }
        ProductNumTV.setText(""+mSelectedNum);
        //将商品总价转换为保留两位小数的值
        String s = takeLengthTwo(zongjia);
        AllPriceTV.setText(s+"￥");
    }
//清空总价，总件数
    private void ClearMethod() {
        mSelectedNum=0;
        zongjia=0;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void clickSubAddBut(int position, int value, boolean isAdd,float price) {
        //更新点击的商品的件数
        products.get(position).setProductNum(value);
        //遍历商品集合
        for (int i=0;i<products.size();i++){
            ProductInfo product = products.get(i);
            boolean pSelected = product.isSelected();
            //判断商品是否选中
            //如果选中，判断点击的商品是否等于选中的商品，如果等于，
            //在判断，当前是否点击了加按钮 还是减按钮
            if (pSelected) {
                if (i==position) {
                if (isAdd) {
                    mSelectedNum+=1;
                    zongjia+=Float.parseFloat(product.getProductPrice())*1;
                    Log.i(TAG, "clickSubAddBut1: pSelected:"+pSelected+";isAdd"+isAdd+";mSelectedNum:"+mSelectedNum+";zongjia:"+zongjia);
                }else {
                    if (value>-1) {
                        mSelectedNum-=1;
                        zongjia-=Float.parseFloat(product.getProductPrice())*1;
                        Log.i(TAG, "clickSubAddBut2: pSelected:"+pSelected+";isAdd"+isAdd+";mSelectedNum:"+mSelectedNum+";zongjia:"+zongjia);
                    }

                }
                }
            }
        }
        ProductNumTV.setText(""+mSelectedNum);
        String s = takeLengthTwo(zongjia);
        AllPriceTV.setText(s+"￥");

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.QX:
                //清空总件数，总价
               ClearMethod();
                //清空位置集合
                mPositons.clear();
                //获取到全选多选框的选中状态
                boolean checked = QuanXuanCB.isChecked();
                isAllSelected=checked;
                if (isAllSelected) {
                   SetSelectState(true);
                    //计算总价和总选中的件数
                    JsAllPriceAllNumber();
                }else {
                    SetSelectState(false);
                    //将总价转换为保留两位小数的String类型
                    String s = takeLengthTwo(zongjia);
                    AllPriceTV.setText(s+"￥");
                    ProductNumTV.setText(mSelectedNum+"");
                }
                //刷新数据
                adapter.notifyDataSetChanged();
                break;
        }
    }
//设置选中的状态
    private void SetSelectState(boolean selected) {
        for (ProductInfo product : products) {
            product.setSelected(selected);
        }
        if (selected) {
            isAllSelected=false;
        }else {
            isAllSelected=true;
        }
        QuanXuanCB.setChecked(selected);
    }

    /**
     * 转换为保留两位小数的值
     * @param price
     * @return
     */
    public String takeLengthTwo(Float price){
        DecimalFormat format=new DecimalFormat("0.00");
        String result = format.format(price);
        return result;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void clickDelete(int positon) {
        //清空总件数，总价
         ClearMethod();
        Log.i(TAG, "clickDelete:position: "+positon);
        products.remove(positon);
        //清空位置位置集合
        mPositons.clear();
        //循环遍历，将选中的商品的位置添加到位置集合中
        for (int i=0;i<products.size();i++){
            ProductInfo productInfo = products.get(i);
            boolean selected = productInfo.isSelected();
            if (selected) {
                mPositons.add(i);
            }
        }
        if ( mPositons.size()<=0){
            QuanXuanCB.setChecked(false);
        }

        //计算总价和总选中的件数
        JsAllPriceAllNumber();
        //刷新数据
        adapter.notifyDataSetChanged();


    }
//计算总价和总件数
    private void JsAllPriceAllNumber() {
        for (ProductInfo product : products) {
            boolean selected = product.isSelected();
            int productNum = product.getProductNum();
            String productPrice = product.getProductPrice();
            float price = Float.parseFloat(productPrice);
            if (selected) {
                zongjia+=productNum*price;
                mSelectedNum+=productNum;
            }
        }
        //将总价转换为保留两位小数的String类型
        String s = takeLengthTwo(zongjia);
        AllPriceTV.setText(s+"￥");
        ProductNumTV.setText(mSelectedNum+"");
    }
}
