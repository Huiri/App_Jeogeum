package com.example.jeogeum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class myWritingAdapter extends RecyclerView.Adapter<myWritingAdapter.MyViewHolder> {
    private String[][] mDataset;

    //private ArrayList<WordData> mDataset;
    //내이메일 확인용(view 다르게 주는거 때메 선언)
    String idemail = "";

    public myWritingAdapter(String[][] myDataset) {
        mDataset = myDataset;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_title;
        public TextView textView_content;

        public MyViewHolder(View v) {
            super(v);
            textView_title = v.findViewById(R.id.title);
            textView_content = v.findViewById(R.id.sub);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_writinglist_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder (MyViewHolder holder,int position){
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView_title.setText(mDataset[0][position]);
        holder.textView_content.setText(mDataset[1][position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount () {
        return mDataset[0].length;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView sub;

        ItemViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            sub = itemView.findViewById(R.id.sub);
        }

        void onBind(WordData data) {
            title.setText(data.getWord());
            sub.setText(data.getDate().toString());
        }
    }

}
