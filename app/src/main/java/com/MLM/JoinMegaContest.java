package com.MLM;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.R;

public class JoinMegaContest extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_mega_contest);
    }
    public void backBtn(View view) {
        onBackPressed();
    }
    public void makePayment(View view) {
        startActivity(new Intent(this , MakePayment.class));
    }
}
