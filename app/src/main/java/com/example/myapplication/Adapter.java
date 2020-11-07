package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

    //INITIALISE
    private Context context;
    private Activity activity;
    private List<String> titles;
    private List<Integer> images;



    //1.CONSTRUCTOR-->  set Var Passed to Adapter
    public Adapter(Activity activity, Context context , List<String> titles, List<Integer> images) {
        this.activity = activity;
        this.context = context;
        this.titles = titles;
        this.images = images;


    }

    //2.ONCREATEVIEWHOLDER -->merge the activity created for relative view(My_row.xml) with java
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //MERGE -- Layout inflater is a class that reads the xml appearance description and convert them into java based View objects
        View view = LayoutInflater.from(context).inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    //3.MYVIEWHOLDER  -->find  View Of activity defined for recycler view (MyRow.XML)
    public class MyViewHolder extends RecyclerView.ViewHolder{

        //FIND  -- findviewById
        TextView my_row_title;
        ImageView my_row_image;
        ConstraintLayout my_row_toplayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            my_row_title = itemView.findViewById(R.id.myRow_title);//id of text1 in my_row
            my_row_image =  itemView.findViewById(R.id.myRow_image);//id of image in my_row
            my_row_toplayout = itemView.findViewById(R.id.myRow_toplayout);//id of top constraint in my_row
        }
    }


    //4.ONBINDVIEWHOLDER() -->set the  View Of activity defined for recycler view (MyRow.XML)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //position = is the position of the item in recyclerview

        //SET  - holder.setText , holder.setImageResource
        holder.my_row_title.setText(titles.get(position));;
        holder.my_row_image.setImageResource(images.get(position));
        holder.my_row_toplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,SecondActivity.class);
                intent.putExtra("text", titles.get(position));
                intent.putExtra("image",images.get(position));


                context.startActivity(intent);
            }
        });
    }






    //5.GETITEMCOUNT()--> returns the count of items recycler view must return
    @Override
    public int getItemCount() {
        return titles.size();
    }




}
