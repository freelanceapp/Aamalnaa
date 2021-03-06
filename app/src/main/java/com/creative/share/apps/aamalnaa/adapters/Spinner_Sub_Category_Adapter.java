package com.creative.share.apps.aamalnaa.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.databinding.SpinnerCategoryRowBinding;
import com.creative.share.apps.aamalnaa.databinding.SpinnerSubCategoryRowBinding;
import com.creative.share.apps.aamalnaa.models.Catogries_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Spinner_Sub_Category_Adapter extends BaseAdapter {
    private List<Catogries_Model.Data.Subcategory> dataList;
    private Context context;
    private String lang;
    public Spinner_Sub_Category_Adapter(List<Catogries_Model.Data.Subcategory> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") SpinnerSubCategoryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_sub_category_row,viewGroup,false);
        binding.setLang(lang);
        binding.setCategoryModel(dataList.get(i));
        return binding.getRoot();
    }
}
