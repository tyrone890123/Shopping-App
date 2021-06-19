package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PaymentOptions extends AppCompatActivity {

    private RadioGroup paymentChoice;
    private Button confirm;
    private String choiceinput;
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent=new Intent(PaymentOptions.this,checkout.class);
        intent.putExtra("chosen",getIntent().getStringExtra("chosen"));
        startActivity(intent);
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);
        paymentChoice=findViewById(R.id.groupedradiobuttonspayment);
        confirm=findViewById(R.id.confirmpaymentchoice);
        confirm.setEnabled(false);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        paymentChoice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                confirm.setEnabled(true);
                choiceinput = ((RadioButton) findViewById(paymentChoice.getCheckedRadioButtonId())).getText().toString().trim();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PaymentOptions.this,checkout.class);
                intent.putExtra("chosen",choiceinput);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}