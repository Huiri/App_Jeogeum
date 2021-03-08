package com.example.jeogeum;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class searchtextadaper extends RecyclerView.Adapter<searchtextadaper.ViewHolder> {

    ArrayList<String> searchText;
    ArrayList<String> nickname;
    String instring;


    searchtextadaper(ArrayList<String> searchText, String instring, ArrayList<String> nickname) {
        this.searchText = searchText;
        this.instring = instring;
        this.nickname = nickname;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public TextView textView2;
        public TextView textView3;

        public ViewHolder(View view){
            super(view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(v.getContext(), SearchShowText.class);

                        intent.putExtra("searchText",searchText.get(pos));
                        intent.putExtra("instring", instring);

                        v.getContext().startActivity(intent);
                    }
                }
            });

            this.textView = view.findViewById(R.id.title1);
            this.textView2 = view.findViewById(R.id.sub1);
            this.textView3 = view.findViewById(R.id.author);
        }

    }

    @NonNull
    @Override
    public searchtextadaper.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_view_list, parent, false);

        searchtextadaper.ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull searchtextadaper.ViewHolder holder, int position) {
        holder.textView.setText(instring);
        holder.textView2.setText(searchText.get(position));
        holder.textView3.setText(nickname.get(position));
    }

    @Override
    public int getItemCount() {
        System.out.println(searchText.size());
        return searchText.size();
    }


}
