package com.rultech.naman.syncnewcontact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHoler> {

    private List<ContactVD> contactVOList;
    private Context mContext;

    public MyAdapter(List<ContactVD> contactVDList, Context mContext){
        this.contactVOList=contactVDList;
        this.mContext=mContext;

    }
    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.simple_listitem1, null);
        ViewHoler ViewHolder = new ViewHoler(view);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHoler holder, final int position) {
        final ContactVD contactVO = contactVOList.get(position);
        holder.tvContactName.setText(contactVO.getContactName());
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());
//        holder.ivContactImage.setImageURI(contactVO.getContactImage());
        try {
            if(contactVO.getContactImage() != null){
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), Uri.parse(contactVO.getContactImage()));
                holder.ivContactImage.setImageBitmap(bitmap);
            }
            else {
                holder.ivContactImage.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_launcher_background));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
    }

    public void updateAdapter(ArrayList<ContactVD> contactVDS){
        contactVOList=new ArrayList<>(contactVDS);
        notifyDataSetChanged();

    }

    public class ViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;

        public ViewHoler(View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.image);
            tvContactName = (TextView) itemView.findViewById(R.id.name);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.phone);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
          /*  Intent i = new Intent(view.getContext(), SecondActivity.class);
            ContactVD vd=contactVOList.get(getPosition());
            i.putExtra("name", vd.getContactName());
            i.putExtra("number", vd.getContactNumber());
            i.putExtra("image",vd.getContactImage());
            view.getContext().startActivity(i);*/
            Toast.makeText(mContext,"The Item Clicked is: "+getPosition(),Toast.LENGTH_SHORT).show();


        }
    }
}
