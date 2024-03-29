package com.MLM;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CoustomControl.AppCommon;
import com.CoustomControl.AppService;
import com.CoustomControl.ResponseAndPojoClass.RegistrationMLMP;
import com.CoustomControl.ResponseAndPojoClass.RegistrationMLMResponseClass;
import com.CoustomControl.ServiceGenerator;
import com.MLM.Adapter.MakePaymentAdapter;
import com.R;
import com.app.model.webrequestmodel.NewUserRequestModel;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;

public class MakePayment extends AppCompatActivity {

    RelativeLayout rlPopUp;
    Button closeBtn;
    TextView tv_reset;
    ImageView qrCode;
    MakePaymentAdapter makePaymentAdapter;
    RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        initUI();
    }

    private void initUI() {
        rlPopUp = findViewById(R.id.rlPopUp);
        closeBtn = findViewById(R.id.closeBtn);
        qrCode = findViewById(R.id.qrCode);
        recycler_view = findViewById(R.id.recycler_view);
        getQRCode("hello");
        recycler_view = findViewById(R.id.recycler_view);
        makePaymentAdapter = new MakePaymentAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(makePaymentAdapter);
    }

    public void makePaymentpopup(View view) {
        rlPopUp.setVisibility(VISIBLE);
    }

    public void close(View view) {
        rlPopUp.setVisibility(View.GONE);
    }
    private void getQRCode(String address) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(address, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCode.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    private void callMLMRegistration(final RegistrationMLMP registerRequestModel, final NewUserRequestModel requestModel) {
        if (AppCommon.getInstance(this).isConnectingToInternet(this)) {
            AppCommon.getInstance(this).setNonTouchableFlags(this);
            //loaderView.setVisibility(View.VISIBLE);
            AppService apiService = ServiceGenerator.createService(AppService.class);
            Call call = apiService.REGISTRATION_RESPONSE_CLASS_CALL(registerRequestModel);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    AppCommon.getInstance(MakePayment.this).clearNonTouchableFlags(MakePayment.this);
                    // loaderView.setVisibility(View.GONE);

                    RegistrationMLMResponseClass registrationMLMResponseClass = (RegistrationMLMResponseClass) response.body();
                    if (registerRequestModel != null) {
                        Log.i("roiWithdrawalResponse::", new Gson().toJson(registrationMLMResponseClass));
                        if (registrationMLMResponseClass.getCode() == 200 ) {

                        } else {

                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                    AppCommon.getInstance(getApplicationContext()).clearNonTouchableFlags(MakePayment.this);
                    // loaderView.setVisibility(View.GONE);
                    Toast.makeText(MakePayment.this, "Please check your internet",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

}
