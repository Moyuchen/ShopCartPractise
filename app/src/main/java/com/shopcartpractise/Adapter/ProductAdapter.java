package com.shopcartpractise.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.shopcartpractise.Bean.ProductInfo;
import com.shopcartpractise.Owner.NumAddSubView;
import com.shopcartpractise.R;

import java.util.List;

/**
 * User: 张亚博
 * Date: 2017-11-01 21:21
 * Description：
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyProductViewHolder> {
    private List<ProductInfo> products;
    private Context context;
    private String TAG="==============ProductAdapter===============";
    public ProductAdapter(List<ProductInfo> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @Override
    public MyProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.product_item_layout, parent, false);
        MyProductViewHolder myProductViewHolder = new MyProductViewHolder(inflate);
        return myProductViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyProductViewHolder holder, final int position) {
        ProductInfo productInfo = products.get(position);
        String productName = productInfo.getProductName();
        int productNum = productInfo.getProductNum();
        final String productPrice = productInfo.getProductPrice();
        boolean selected = productInfo.isSelected();


        holder.checkBox.setChecked(selected);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                boolean b = holder.checkBox.isChecked();
                Log.i("=======ProductAdapter=========", "onCheckedChanged: "+b+"+position:"+position);
                clickCheckBox.clickCheckBox(position,b);
            }
        });


//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        holder.nASV.setValue(productNum);
        holder.nASV.setOnButtonClickListenter(new NumAddSubView.OnButtonClickListenter() {
            @Override
            public void onButtonAddClick(View view, int value) {
                    holder.nASV.setValue(value);
                    clickCheckBox.clickSubAddBut(position,value,true,Float.parseFloat(productPrice));
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onButtonSubClick(View view, int value) {
                int minValue = holder.nASV.getMinValue();
                Log.i(TAG, "onButtonSubClick:  minValue: "+minValue+";vlaue:"+value);
                if (value<=minValue) {
                    Toast.makeText(context,"已为最小值",Toast.LENGTH_LONG).show();
                }else {
                    if (value>=0) {
                        holder.nASV.setValue(value);
                        clickCheckBox.clickSubAddBut(position,value,false,Float.parseFloat(productPrice));
                    }

                }

            }
        });
        holder.proName.setText(productName);
        holder.proPrice.setText("￥："+productPrice);

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyProductViewHolder extends RecyclerView.ViewHolder{

        private final CheckBox checkBox;//多选框，商品前
        private final TextView proName;//商品名
        private final TextView proPrice;//商品价格
        private final Button delete;//删除商品
        private final NumAddSubView nASV;

        public MyProductViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.Pro_Item_CB);
            proName = itemView.findViewById(R.id.ProName);
            proPrice = itemView.findViewById(R.id.ProPrice);
            delete = itemView.findViewById(R.id.Delete);
            nASV = itemView.findViewById(R.id.NASV);

        }
    }
    //点击商品前的多选框
    private ClickCheckBox clickCheckBox;
    //将点击多选框接口暴露
    public void setClickCheckBox(ClickCheckBox clickCheckBox) {
        this.clickCheckBox = clickCheckBox;
    }
//点击多选框接口
    public interface ClickCheckBox{
        void clickCheckBox(int positon,boolean selected);
        void clickSubAddBut(int position,int value,boolean isAdd,float price);
    }
    //点击删除按钮
    private ClickDelete clickDelete;
//暴露点击删除按钮接口
    public void setClickDelete(ClickDelete clickDelete) {
        this.clickDelete = clickDelete;
    }
    //点击删除商品接口

    public interface ClickDelete{
        void clickDelete(int positon);
    }
}
