package com.MLM;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import static android.view.View.VISIBLE;

public class MakePayment extends AppCompatActivity {

    RelativeLayout rlPopUp;
    Button closeBtn;
    TextView tv_reset;
    ImageView qrCode;

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
        getQRCode("hello");
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
}
