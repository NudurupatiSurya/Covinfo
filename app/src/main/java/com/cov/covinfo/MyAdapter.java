package com.cov.covinfo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {
    private ArrayList<String> mDataset;
    private ArrayList<String> mDataset2;
    Context mcontext;


    public MyAdapter(Context context, ArrayList<String> myDataset) {
        this.mDataset = myDataset;
        mDataset2 = new ArrayList<>(myDataset);
        mcontext = context;
    }



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        LinearLayout relativeLayout;
        public TextView textView;
        public MyViewHolder(View v) {

            super(v);
            relativeLayout = v.findViewById(R.id.recyclerrl);
            textView = v.findViewById(R.id.countrynames);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)


    // Create new views (invoked by the layout manager)
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }






    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.textView.setText(mDataset.get(position));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext,""+mDataset.get(position), LENGTH_SHORT).show();
                Intent single_country = new Intent(mcontext,Other_Countries.class);
                single_country.putExtra("country",mDataset.get(position));
                mcontext.startActivity(single_country);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    public int getItemCount() {
        // Toast.makeText(mcontext,""+mDataset.size(),LENGTH_SHORT).show();
        return mDataset.size();
    }
    @Override
    public Filter getFilter() {
        return examplefilter;
    }
    private Filter examplefilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<String> filtered = new ArrayList<String>();
            if(charSequence == null || charSequence.length() == 0){
                filtered.addAll(mDataset2);
            }
            else{
                String filterpattern = charSequence.toString().toLowerCase().trim();

                for(String item: mDataset2){
                    if(item.toLowerCase().contains(filterpattern)){
                        filtered.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filtered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mDataset.clear();
            mDataset.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
