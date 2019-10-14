package com.creative.share.apps.aamalnaa.activities_fragments.activity_adsdetails;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.creative.share.apps.aamalnaa.R;
import com.creative.share.apps.aamalnaa.activities_fragments.activity_profile.ProfileActivity;
import com.creative.share.apps.aamalnaa.adapters.Ads_Adapter;
import com.creative.share.apps.aamalnaa.databinding.ActivityAdsBinding;
import com.creative.share.apps.aamalnaa.databinding.ActivityAdsDetialsBinding;
import com.creative.share.apps.aamalnaa.databinding.ActivityMyAdsBinding;
import com.creative.share.apps.aamalnaa.interfaces.Listeners;
import com.creative.share.apps.aamalnaa.language.Language;
import com.creative.share.apps.aamalnaa.models.Adversiment_Model;
import com.creative.share.apps.aamalnaa.models.Single_Adversiment_Model;
import com.creative.share.apps.aamalnaa.models.UserModel;
import com.creative.share.apps.aamalnaa.preferences.Preferences;
import com.creative.share.apps.aamalnaa.remote.Api;
import com.creative.share.apps.aamalnaa.share.Common;
import com.creative.share.apps.aamalnaa.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdsDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityAdsDetialsBinding binding;
    private String lang;
    private Ads_Adapter ads_adapter;
    private List<Adversiment_Model.Data> advesriment_data_list;
    private Single_Adversiment_Model single_adversiment_model;
    private LinearLayoutManager manager;
    private boolean isLoading = false;
    private int current_page2 = 1;
    private Preferences preferences;
    private UserModel userModel;
    private List<Single_Adversiment_Model.comments> commentsList;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_ads_detials);
        initView();
        getsingleads();

    }

    private void initView() {
        commentsList=new ArrayList<>();
        single_adversiment_model=new Single_Adversiment_Model();
        advesriment_data_list=new ArrayList<>();
        preferences= Preferences.getInstance();
        userModel=preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        if(userModel!=null){
        binding.setUsermodel(userModel.getUser());
        manager = new LinearLayoutManager(this);}
binding.setAdsmodel(single_adversiment_model);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.reccomment.setLayoutManager(manager);
        ads_adapter = new Ads_Adapter(advesriment_data_list,this);
        binding.reccomment.setItemViewCacheSize(25);
        binding.reccomment.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.reccomment.setDrawingCacheEnabled(true);
        binding.progBar.setVisibility(View.GONE);
        binding.llAds.setVisibility(View.GONE);

        binding.reccomment.setAdapter(ads_adapter);

    }
    public void getsingleads() {
        //   Common.CloseKeyBoard(homeActivity, edt_name);

        ProgressDialog dialog = Common.createProgressDialog(AdsDetialsActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        // rec_sent.setVisibility(View.GONE);
        try {


            Api.getService( Tags.base_url)
                    .getSingleAds(userModel.getUser().getId()+"")
                    .enqueue(new Callback<Single_Adversiment_Model>() {
                        @Override
                        public void onResponse(Call<Single_Adversiment_Model> call, Response<Single_Adversiment_Model> response) {
                            dialog.dismiss();

                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body() != null) {
                                update(response.body());
                            } else {


                                Toast.makeText(AdsDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Single_Adversiment_Model> call, Throwable t) {
                            try {

dialog.dismiss();

                                Toast.makeText(AdsDetialsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        }catch (Exception e){

dialog.dismiss();
        }
    }

    private void update(Single_Adversiment_Model body) {
        binding.setAdsmodel(body);
        commentsList.clear();
        if(body.getComments()!=null){
        commentsList.addAll(body.getComments());}
    }


    @Override
    public void back() {
        finish();
    }
}