package com.example.dibyenduroy.livingsocial.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dibyenduroy.livingsocial.R;
import com.example.dibyenduroy.livingsocial.helper.PicassoHelper;
import com.example.dibyenduroy.livingsocial.model.Merchant;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by dibyenduroy on 2/23/15.
 */
public class MerchantListAdapter extends BaseAdapter{
    private final WeakReference<Activity> activity;
    private final LayoutInflater inflater;
    List<Merchant> merchantList;
    public MerchantListAdapter(WeakReference<Activity> activity
            ,List<Merchant> merchantList){
        this.activity = activity;
        this.merchantList = merchantList;
        inflater = (LayoutInflater) activity.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return merchantList.size();
    }

    @Override
    public Object getItem(int position) {
        return merchantList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clearAddAllItems(List<Merchant> merchants){
        merchantList.clear();
        merchantList.addAll(merchants);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.merchant_summary, null);
            MerchantViewHolder vh = new MerchantViewHolder(convertView);
            convertView.setTag(vh);
        }

        final Merchant merchant = merchantList.get(position);
        MerchantViewHolder vh = (MerchantViewHolder) convertView.getTag();

        vh.merchantName.setText(merchant.merchant_name);
        vh.merchantTitle.setText(merchant.title);
        new PicassoHelper.PicassoBuilder(activity.get())
                .setUrl(merchant.image_url)
                .setImageView(vh.productImage)
                .build();
        //vh.productImage.setImageURI(Uri.parse(merchant.image_url));
        vh.price.setText("$"+merchant.price);


        return convertView;
    }

    private class MerchantViewHolder{
        ImageView productImage;
        TextView merchantName;
        TextView merchantTitle;
        TextView price;
        public MerchantViewHolder(View view){
            productImage = (ImageView) view.findViewById(R.id.product_img);
            merchantName = (TextView) view.findViewById(R.id.merchant_name);
            merchantTitle = (TextView) view.findViewById(R.id.merchant_title);
            price = (TextView) view.findViewById(R.id.price);
        }
    }
}
