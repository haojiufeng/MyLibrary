package com.mylibrary;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.commonlibrary.dialog.SingleInputDialogView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    TextView textView=findViewById(R.id.click);
    textView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SingleInputDialogView dialogView=new SingleInputDialogView(MainActivity.this);
            dialogView.show();
            Toast.makeText(MainActivity.this,"点击到了",Toast.LENGTH_LONG).show();

        }
    });
    }
}
