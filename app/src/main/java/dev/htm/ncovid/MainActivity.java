package dev.htm.ncovid;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;

import dev.htm.ncovid.model.NCovidLiveData;
import dev.htm.ncovid.service.FetchAsyncData;
import dev.htm.ncovid.service.OnCallback;
import dev.htm.ncovid.util.GetDataUtil;

public class MainActivity extends AppCompatActivity implements OnCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetDataUtil.totalCase(this, FetchAsyncData.COUNTRIES, this);
    }

    @Override
    public void onSuccess(String data) {

        Gson gson = new GsonBuilder().create();
        Type collectionType = new TypeToken<Collection<NCovidLiveData>>() {
        }.getType();
        Collection<NCovidLiveData> nCovidLiveDatas = gson.fromJson(data, collectionType);
        Toast.makeText(this, "" + nCovidLiveDatas, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String error) {

    }
}
