package com.technology.circles.apps.testahil.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.testahil.R;
import com.creative.share.apps.testahil.databinding.LoadMoreBinding;
import com.creative.share.apps.testahil.databinding.ProductRowBinding;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.fragments.Fragment_Favorite;
import com.technology.circles.apps.testahil.activities_fragments.activity_home.fragments.Fragment_Offers;
import com.technology.circles.apps.testahil.activities_fragments.activity_search.SearchActivity;
import com.technology.circles.apps.testahil.models.ProductModel;

import java.util.List;

import io.paperdb.Paper;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int LOAD_DATA = 1;
    private final int LOAD_MORE = 2;


    private List<ProductModel> list;
    private Context context;
    private Fragment fragment;
    private LayoutInflater inflater;
    private String lang;
    private AppCompatActivity activity;

    public ProductAdapter(List<ProductModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
        activity = (AppCompatActivity) context;

        Paper.init(context);
        lang = Paper.book().read("lang","ar");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LOAD_DATA)
        {
            ProductRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.product_row,parent,false);
            return new MyHolder(binding);
        }else
            {
                LoadMoreBinding binding = DataBindingUtil.inflate(inflater, R.layout.load_more,parent,false);
                return new LoadMoreHolder(binding);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder)
        {
            MyHolder myHolder = (MyHolder) holder;
            ProductModel model = list.get(myHolder.getAdapterPosition());
            myHolder.binding.setLang(lang);
            myHolder.binding.setModel(model);


            myHolder.itemView.setOnClickListener(view -> {

                if (fragment==null){
                    if (activity instanceof SearchActivity)
                    {
                        ProductModel model2 = list.get(myHolder.getAdapterPosition());

                        SearchActivity searchActivity = (SearchActivity) activity;
                        searchActivity.setItemProduct(model2);
                    }
                }else
                    {

                        if (fragment instanceof Fragment_Offers)
                        {
                            ProductModel model2 = list.get(myHolder.getAdapterPosition());

                            Fragment_Offers fragment_offers = (Fragment_Offers) fragment;
                            fragment_offers.setItemProduct(model2);
                        }else if (fragment instanceof Fragment_Favorite)
                        {
                            Fragment_Favorite fragment_favorite = (Fragment_Favorite) fragment;
                            ProductModel model2 = list.get(myHolder.getAdapterPosition());
                            fragment_favorite.setItemProduct(model2,myHolder.getAdapterPosition());

                        }
                    }


            });


        }else if (holder instanceof LoadMoreHolder)
        {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            loadMoreHolder.binding.progBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {
        return list.size()>0?list.size():0;
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private ProductRowBinding binding;
        public MyHolder(@NonNull ProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public class LoadMoreHolder extends RecyclerView.ViewHolder {
        private LoadMoreBinding binding;
        public LoadMoreHolder(@NonNull LoadMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (list.get(position)==null)
        {
            return LOAD_MORE;
        }else
        {
            return LOAD_DATA;
        }


    }
}
