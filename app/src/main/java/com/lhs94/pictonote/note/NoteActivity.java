package com.lhs94.pictonote.note;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.lhs94.pictonote.AppControler;
import com.lhs94.pictonote.note.image.AttachOptionDialog;
import com.lhs94.pictonote.note.image.DepthPageTransformer;
import com.lhs94.pictonote.R;
import com.lhs94.pictonote.note.image.ImageListAdapter;
import com.lhs94.pictonote.sqlite.SQLiteControler;
import com.rd.PageIndicatorView;

import java.io.File;

public class NoteActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    private SQLiteControler sqliteControler;
    private Note note;
    private EditText editTitle;
    private EditText editText;

    private boolean haveToSave = false;
    private boolean selectingImage = false;

    private ImageListAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        AppControler.getInstance().setCurrentNoteActivity(this);

        Toolbar toolbar = findViewById(R.id.toolbar_note);
        setSupportActionBar(toolbar);

        sqliteControler = SQLiteControler.getInstance();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        View v = LayoutInflater.from(this).inflate(R.layout.toolbar_title, toolbar);

        editTitle = v.findViewById(R.id.edit_title);
        editText = findViewById(R.id.edit_text);

        ViewPager2 v2 = findViewById(R.id.view_pager);
        pageAdapter = new ImageListAdapter(this);
        v2.setAdapter(pageAdapter);
        v2.setPageTransformer(new DepthPageTransformer());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            v2.setScrollIndicators(View.SCROLL_INDICATOR_BOTTOM);
        }

        final PageIndicatorView indicator = findViewById(R.id.page_indicator);
        pageAdapter.setIndicator(indicator);
        indicator.setRadius(4);
        indicator.setSelectedColor(0xffecd786);
        indicator.setUnselectedColor(0xfffae798);

        v2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                indicator.setSelection(position);
            }
        });

        note = getIntent().getParcelableExtra("note");
        if(note == null) {
            note = new Note();
            editText.setHint("빈 이미지를 터치해 새로운 이미지를 추가하세요\n\n텍스트를 입력하세요");
            pageAdapter.setImage(note.getImageList());
            haveToSave = true;
        }
        else {
            editTitle.setText(note.getTitle());
            editText.setText(note.getText());
            pageAdapter.setImage(note.getImageList());

            editTitle.setFocusable(false);
            editText.setFocusable(false);
            pageAdapter.setImageEditable(false);
        }
    }

    @Override
    protected void onPause() {
        if(!selectingImage && haveToSave)
            saveNote();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        AppControler.getInstance().setCurrentNoteActivity(null);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_note, menu);
        if(note.getIdx() == Note.NEW_NOTE) {
            menu.findItem(R.id.action_edit).setVisible(false);
            menu.findItem(R.id.action_delete).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_edit :
                editTitle.setFocusable(true);
                editTitle.setFocusableInTouchMode(true);
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.setHint("빈 이미지를 터치해 새로운 이미지를 추가하세요\n\n텍스트를 입력하세요");
                pageAdapter.setImageEditable(true);
                item.setEnabled(false);
                haveToSave = true;
                return true;

            case R.id.action_delete :
                new AlertDialog.Builder(this).setCancelable(true).setMessage("삭제하시겠습니까?")
                        .setPositiveButton("삭제", this).setNegativeButton("취소", this).show();
                haveToSave = false;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch(which) {
            case AlertDialog.BUTTON_POSITIVE :
                sqliteControler.deleteData(note.getIdx());
                dialog.dismiss();
                finish();
                break;

            case AlertDialog.BUTTON_NEGATIVE :
                dialog.dismiss();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectingImage = false;

        Uri uri = null;
        if (data != null) {
            uri = data.getData();
        }
        if(uri == null)
            return;

        ClipData clip = data.getClipData();

        switch(requestCode) {
            case AttachOptionDialog.OPTION_CAMERA :
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                String path = getIntent().getStringExtra("path");
                File f = new File(path);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                sendBroadcast(mediaScanIntent);
                pageAdapter.addImage(uri);
                break;

            case AttachOptionDialog.OPTION_GALLERY :
                if(clip != null)
                    for (int i = 0; i < clip.getItemCount(); i++)
                        pageAdapter.addImage(clip.getItemAt(i).getUri());
                else
                    pageAdapter.addImage(uri);
                break;

            case AttachOptionDialog.OPTION_URL :
                pageAdapter.addImage(uri);
                break;
        }
    }


    public void setSelectingImage(boolean b) {
        selectingImage = b;
    }

    public void saveNote() {
        int idx = note.getIdx();
        String title = editTitle.getText().toString();
        String text = editText.getText().toString();
        String image = note.getImageJson();

        if(title.equals("") && text.equals("") && image.equals("[]"))
            return;

        if(idx == Note.NEW_NOTE) {
            //insertData
            if(title.equals(""))
                title = "제목 없음";
            sqliteControler.insertData(title, text, image);
        }
        else {
            //updateData
            sqliteControler.updateData(idx, title, text, image);
        }

        Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show();
    }
}


