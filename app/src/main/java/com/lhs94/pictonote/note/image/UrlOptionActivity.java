package com.lhs94.pictonote.note.image;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lhs94.pictonote.R;

public class UrlOptionActivity extends AppCompatActivity {

    private Intent intent = new Intent();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new UrlOptionDialog(this).show();
    }

    private class UrlOptionDialog extends AlertDialog implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {

        EditText editText;

        UrlOptionDialog(@NonNull Context context) {
            super(context);
            View v = getLayoutInflater().inflate(R.layout.dialog_option_url, null);
            editText = v.findViewById(R.id.url_edit);
            setView(v);
            setCancelable(true);
            setOnCancelListener(this);
            setButton(BUTTON_POSITIVE, "완료", this);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            String url = editText.getText().toString();
            Uri uri = Uri.parse(url);
            intent.setData(uri);
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }
}
