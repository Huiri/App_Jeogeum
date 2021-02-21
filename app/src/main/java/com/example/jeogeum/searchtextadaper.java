package com.example.jeogeum;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class searchtextadaper extends RecyclerView.Adapter<searchtextadaper.ViewHolder> {

    ArrayList<String> searchText;
    String instring;

    searchtextadaper(ArrayList<String> searchText, String instring) {
        this.searchText = searchText;
        this.instring = instring;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public TextView textView2;

        public ViewHolder(View view){
            super(view);
            this.textView = view.findViewById(R.id.title1);
            this.textView2 = view.findViewById(R.id.sub1);
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
    }

    @Override
    public int getItemCount() {
        System.out.println(searchText.size());
        return searchText.size();
    }


}
