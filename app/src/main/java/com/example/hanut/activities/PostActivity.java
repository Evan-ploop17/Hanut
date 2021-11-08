package com.example.hanut.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hanut.R;
import com.example.hanut.providers.ImageProvider;
import com.example.hanut.providers.PostProvider;
import com.example.hanut.utils.FileUtil;
import com.example.socialmediagamer.providers.AuthProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import dmax.dialog.SpotsDialog;

public class PostActivity extends AppCompatActivity {


    ImageView mImageViewPost1;
    ImageView mImageViewPost2;
    Button mBtnPost;
    String mTitle = "";
    String mDescription = "";
    TextInputEditText mTextInputTitle;
    TextInputEditText mTextInputDescription;
    TextInputEditText mTextInputCategory;
    String mCategory= "";
    File mImageFile;
    File mImageFile2;
    AlertDialog mDialog;
    AlertDialog.Builder mDialogBuilder;
    PostProvider mPostProvider;
    ImageProvider mImageProvider;
    private final int GALLERY_REQUEST_CODE = 1;
    private final int GALLERY_REQUEST_CODE_2  = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mImageProvider = new ImageProvider();
        mPostProvider = new PostProvider();
        //mAuthProvider = new AuthProvider();
        // Mensaje
        mDialog = new SpotsDialog.Builder().setContext(this).setMessage("Espere un momento").build();
        mDialogBuilder = new AlertDialog.Builder(this);
        mDialogBuilder.setTitle("Choose an option");

        mTextInputTitle = findViewById(R.id.textInputTitle);
        mTextInputCategory = findViewById(R.id.textInputCategory);
        mTextInputDescription = findViewById(R.id.textInputDescription);


        mImageViewPost1 = findViewById(R.id.imageViewPost1);
        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(GALLERY_REQUEST_CODE);
            }
        });

        mImageViewPost2 = findViewById(R.id.imageViewPost2);
        mImageViewPost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(GALLERY_REQUEST_CODE_2);
            }
        });

        mBtnPost = findViewById(R.id.btnPost);
        mBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPost();
            }
        });
    }

    private void clickPost() {
        mTitle = mTextInputTitle.getText().toString();
        mCategory = mTextInputCategory.getText().toString();
        mDescription = mTextInputDescription.getText().toString();

        if(!mTitle.isEmpty() && !mDescription.isEmpty() && !mCategory.isEmpty()){
            if(mImageFile != null){
                saveImage();
                //Toast.makeText(PostActivity.this,"Se guardo imagen", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(PostActivity.this,"Choose an Image", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(PostActivity.this,"Fill in the fields", Toast.LENGTH_LONG).show();
        }
    }

    private void saveImage(){
        mImageProvider.save(PostActivity.this, mImageFile ).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PostActivity.this, "La imagen se almaceno",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(PostActivity.this, "NOOO se almaceno",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
   /* private void saveImage() {
        mDialog.show();
        mImageProvider.save(PostActivity.this, mImageFile ).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();

                            mImageProvider.save(PostActivity.this, mImageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if(taskImage2.isSuccessful()){
                                        mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                                String url2 = uri2.toString();
                                                Post post = new Post();
                                                post.setImage1(url);
                                                post.setImage2(url2);
                                                post.setTitle(mTitle);
                                                post.setDescription(mDescription);
                                                post.setCategory(mCategory);
                                                post.setIdUser(mAuthProvider.getUid());

                                                mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> taskSave) {
                                                        mDialog.dismiss();
                                                        if(taskSave.isSuccessful()){
                                                            clearForm();
                                                            Toast.makeText(PostActivity.this, "Info saved", Toast.LENGTH_LONG).show();
                                                        }
                                                        else{
                                                            Toast.makeText(PostActivity.this, "Info unsaved", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                    else{
                                        mDialog.dismiss();
                                        Toast.makeText(PostActivity.this, "Image 2 unsaved", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }else{
                    mDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Image unsaved", Toast.LENGTH_LONG).show();
                }
            }
        });
    }*/

    private void openGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && requestCode != RESULT_OK){
            try{
                // Transformar la URI en archivo
                mImageFile = FileUtil.from(this, data.getData());
                // Para que la imágen seleccionada aparezca en la pantalla
                mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            }catch(Exception e){
                Log.d("ERROR ","Se produjo el error: " + e.getMessage());
                Toast.makeText(PostActivity.this, "Se produjo un error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode == GALLERY_REQUEST_CODE_2 && requestCode != RESULT_OK){
            try{
                // Transformar la URI en archivo
                mImageFile2 = FileUtil.from(this, data.getData());
                // Para que la imágen seleccionada aparezca en la pantalla
                mImageViewPost2.setImageBitmap(BitmapFactory.decodeFile(mImageFile2.getAbsolutePath()));
            }catch(Exception e){
                Log.d("ERROR ","There is an error: " + e.getMessage());
                Toast.makeText(PostActivity.this, "There is an error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}