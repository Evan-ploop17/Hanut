package com.example.hanut.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hanut.R;
import com.example.hanut.models.Post;
import com.example.hanut.providers.ImageProvider;
import com.example.hanut.providers.PostProvider;
import com.example.hanut.utils.FileUtil;
import com.example.socialmediagamer.providers.AuthProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

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
    AuthProvider mAuthProvider;
    private final int GALLERY_REQUEST_CODE = 1;
    private final int GALLERY_REQUEST_CODE_2  = 2;
    private final int PHOTO_REQUEST_CODE = 3;
    CharSequence options[];

    String mAbsolutePhotoPhat;
    String mPhotoPath;
    File mPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mImageProvider = new ImageProvider();
        mPostProvider = new PostProvider();
        mAuthProvider = new AuthProvider();

        // Mensaje
          mDialog = new SpotsDialog.Builder().setContext(this).setMessage("Espere un momento").build();
        mDialogBuilder = new AlertDialog.Builder(this);
        mDialogBuilder.setTitle("Choose an option");

        mTextInputTitle = findViewById(R.id.textInputTitle);
        mTextInputCategory = findViewById(R.id.textInputCategory);
        mTextInputDescription = findViewById(R.id.textInputDescription);

        options = new CharSequence[] { "Imagen de galeria",  "Camara"};

        mImageViewPost1 = findViewById(R.id.imageViewPost1);
        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(GALLERY_REQUEST_CODE);
                //openGallery(GALLERY_REQUEST_CODE);
            }
        });

        mImageViewPost2 = findViewById(R.id.imageViewPost2);
        mImageViewPost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(GALLERY_REQUEST_CODE_2);
                //openGallery(GALLERY_REQUEST_CODE_2);
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

    private void selectOptionImage(int requestCode) {
        mDialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    openGallery(requestCode);
                }
                else{
                    takePhoto();
                }
            }
        });
        mDialogBuilder.show();
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createPhotoFile();
            }catch (Exception e){
                Toast.makeText(PostActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile( PostActivity.this, "com.example.hanut", photoFile );
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                // este metodo siempre espera lo que pase en el onActivityResult()
                startActivityForResult(takePictureIntent, PHOTO_REQUEST_CODE);
            }
        }
    }
    private File createPhotoFile() throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
                new Date() + "_photo",
                ".jpg",
                storageDir
        );
        mPhotoPath = "file:" + photoFile.getAbsolutePath();
        mAbsolutePhotoPhat = photoFile.getAbsolutePath();
        return photoFile;
    }

    private void clickPost() {
        mTitle = mTextInputTitle.getText().toString();
        mCategory = mTextInputCategory.getText().toString();
        mDescription = mTextInputDescription.getText().toString();

        if(!mTitle.isEmpty() && !mDescription.isEmpty() && !mCategory.isEmpty()){
            if(mImageFile != null){
                saveImage();
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
        mDialog.show();
        mImageProvider.save(PostActivity.this, mImageFile ).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    // Con esto obtenemos la URL de donde esta la imágen
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Este método nos devuelve una uri y la transformarmos en string
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
                                                // Los objetos mXXX se hicieron globales para no tener que estarlos pasando como parametro a la funcion
                                                post.setImage1(url);
                                                post.setImage2(url2);
                                                post.setTitle(mTitle);
                                                post.setDescription(mDescription);
                                                post.setCategory(mCategory);
                                                post.setIdUser(mAuthProvider.getUid());
                                                // addOnCompleteListener se usa para saer si se termino de ejecutar la tarea
                                                mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    // cambiamos el nombre de la tarea a taskSave para evitar choques con la otra
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> taskSave) {
                                                        mDialog.dismiss();
                                                        if(taskSave.isSuccessful()){
                                                            clearForm();
                                                            Toast.makeText(PostActivity.this, "La info se almaceno correctamente",Toast.LENGTH_LONG).show();
                                                        } else {
                                                            Toast.makeText(PostActivity.this, "La info NOO se almaceno correctamente",Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    } else{
                                        mDialog.dismiss();
                                        Toast.makeText(PostActivity.this, "Imagen dos no se pudo guardar",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }else{
                    mDialog.dismiss();
                    Toast.makeText(PostActivity.this, "NOOO se almaceno la imágen",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SELECCIÓN DE IMÁGEN CON GALERIA

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
        // SELECCIÓN DE FOTOGRAFÍA
        if(requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK){
            Picasso.with(PostActivity.this).load(mPhotoPath).into(mImageViewPost1);
        }

    }

    private void clearForm() {
        mTextInputTitle.setText("");
        mTextInputDescription.setText("");
        mImageViewPost1.setImageResource(R.drawable.upload_image);
        mImageViewPost2.setImageResource(R.drawable.upload_image);
        mTitle = "";
        mTextInputCategory.setText("");
        mDescription = "";
        mImageFile = null;
        mImageFile2 = null;
    }
}