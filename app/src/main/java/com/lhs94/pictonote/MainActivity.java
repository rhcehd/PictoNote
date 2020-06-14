package com.lhs94.pictonote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lhs94.pictonote.note.Note;
import com.lhs94.pictonote.note.NoteActivity;
import com.lhs94.pictonote.sqlite.SQLiteControler;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteControler sqliteControler;

    private NoteListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final String[] permissionList = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
            int check;
            for (String permission : permissionList) {
                 check = checkCallingOrSelfPermission(permission);
                if (check == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(permissionList, 0);
                }
            }
        }

        if(savedInstanceState != null) {

        }
        else {
            initViews();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<Note> notes = sqliteControler.getAllDatas();
        adapter.updateData(notes);
    }

    @Override
    protected void onDestroy() {
        sqliteControler.closeControler();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add :
                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_info :
                //show info
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            for (int grantResult : grantResults) {
                if(grantResult == PackageManager.PERMISSION_GRANTED) {
                    initViews();
                }
                else if (grantResult == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(getApplicationContext(), "권한 설정이 필요합니다", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    private void initViews() {
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        SQLiteControler.initControler(this);
        sqliteControler = SQLiteControler.getInstance();
        //SQLiteControler.getInstance().clearAllDatabase();

        RecyclerView recyclerView = findViewById(R.id.recycler);
        adapter = new NoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        GridLayoutManager lm = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
    }
}
