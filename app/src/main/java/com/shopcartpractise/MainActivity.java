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

public class MainActivity extends AppCompatActivity implements ProductAdapter.ClickCheckBox, View.OnClickListener {

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
            product.setProductNum(1);
            product.setProductNum(0);
            products.add(product);
        }

        adapter = new ProductAdapter(products,this);
        mRecycleView.setAdapter(adapter);
        adapter.setClickCheckBox(this);

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
        mSelectedNum=0;
        zongjia=0;
        //判断点击的多选框是否选中
        //选中，将当前点击的多选框位置，保存起来，并将所对应的商品设置为true
        if (selected) {
            mPositons.add(positon);
            ProductInfo productInfo = products.get(positon);
            productInfo.setSelected(true);
        }else {
            //不选中，遍历位置集合
            for (int i=0 ; i<mPositons.size();i++){
                int cPosition = mPositons.get(i);
                if (cPosition==positon) {
                    mPositons.remove(i);
                    ProductInfo productInfo = products.get(positon);
                    productInfo.setSelected(false);
                }
            }
        }
        for (int i=0;i<products.size(); i++) {
            ProductInfo product = products.get(i);
            boolean pSelected = product.isSelected();
            if (pSelected) {
                mSelectedNum+=product.getProductNum();
                zongjia+=product.getProductNum()*Float.parseFloat(product.getProductPrice());
            }
        }
        if (mPositons.size()==products.size()) {
            QuanXuanCB.setChecked(true);
        }else {
            QuanXuanCB.setChecked(false);
        }
        ProductNumTV.setText(""+mSelectedNum);
        String s = takeLengthTwo(zongjia);
        AllPriceTV.setText(s+"￥");
    }

    @SuppressLint("LongLogTag")
    @Override
    public void clickSubAddBut(int position, int value, boolean isAdd,float price) {
        products.get(position).setProductNum(value);
        for (int i=0;i<products.size();i++){
            ProductInfo product = products.get(i);
            boolean pSelected = product.isSelected();
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
                zongjia=0;
                mSelectedNum=0;
                mPositons.clear();
                //获取到全选多选框的选中状态
                boolean checked = QuanXuanCB.isChecked();
                isAllSelected=checked;
                if (isAllSelected) {
                    for (int i=0;i<products.size();i++) {
                        ProductInfo productInfo = products.get(i);
                        productInfo.setSelected(true);
                        mPositons.add(i);
                    }
                    isAllSelected=false;
                    QuanXuanCB.setChecked(true);
                    for (ProductInfo product : products) {
                        boolean selected = product.isSelected();
                        int productNum = product.getProductNum();
                        float price = Float.parseFloat(product.getProductPrice());
                        if (selected) {
                            zongjia+=productNum*price;
                            mSelectedNum+=productNum;
                        }
                    }
                    ProductNumTV.setText(mSelectedNum+"");
                    //将总价转换为保留两位小数的String类型
                    String s = takeLengthTwo(zongjia);
                    AllPriceTV.setText(s+"￥");
                }else {
                    for (ProductInfo product : products) {
                        product.setSelected(false);
                    }
                    isAllSelected=true;
                    QuanXuanCB.setChecked(false);

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
    public String takeLengthTwo(Float price){
        DecimalFormat format=new DecimalFormat("0.00");
        String result = format.format(price);
        return result;
    }
}
