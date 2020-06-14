package com.lhs94.pictonote.note.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.lhs94.pictonote.AppControler;
import com.lhs94.pictonote.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AttachOptionDialog extends AlertDialog {

    public static final int OPTION_CAMERA = 0;
    public static final int OPTION_GALLERY = 1;
    public static final int OPTION_URL = 2;

    AttachOptionDialog(@NonNull Context context) {
        super(context);

        View v = getLayoutInflater().inflate(R.layout.dialog_options_attach_image, null);
        AttachOptionListener listener = new AttachOptionListener(context);
        v.findViewById(R.id.attach_camera).setOnClickListener(listener);
        v.findViewById(R.id.attach_gallery).setOnClickListener(listener);
        v.findViewById(R.id.attach_url).setOnClickListener(listener);
        setView(v);
        setCancelable(true);
    }

    private class AttachOptionListener implements View.OnClickListener {
        Context context;
        AttachOptionListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Intent intent =  new Intent();
            Activity ac = (Activity)context;

            switch (v.getId()) {
                case R.id.attach_camera :
                    AppControler.getInstance().setSelectingImage(true);
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(ac.getPackageManager()) != null) {
                        File image = null;
                        String currentPhotoPath;
                        try {
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(new Date());
                            String imageFileName = "JPEG_" + timeStamp + "_";
                            //File storageDir = ac.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            storageDir = new File(storageDir,"PictoNote");
                            if(!storageDir.exists()) {
                                if(storageDir.mkdir())
                                    Toast.makeText(context, "fail to mkdir..", Toast.LENGTH_SHORT).show();
                            }
                            image = File.createTempFile(imageFileName, ".jpg", storageDir);
                            currentPhotoPath = image.getAbsolutePath();
                            Intent path = new Intent();
                            path.putExtra("path", currentPhotoPath);
                            ((Activity) context).setIntent(path);

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        if (image != null) {
                            Uri photoURI = FileProvider.getUriForFile(context,"com.lhs94.pictonote.fileprovider", image);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            ac.startActivityForResult(takePictureIntent, OPTION_CAMERA);
                        }
                    }
                    break;

                case R.id.attach_gallery :
                    AppControler.getInstance().setSelectingImage(true);
                    intent.setAction(Intent.ACTION_PICK);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setType("image/*");
                    ac.startActivityForResult(intent, OPTION_GALLERY);
                    break;

                case R.id.attach_url :
                    AppControler.getInstance().setSelectingImage(true);
                    intent.setClass(context, UrlOptionActivity.class);
                    ac.startActivityForResult(intent, OPTION_URL);
                    break;
            }
            dismiss();
        }
    }
}
