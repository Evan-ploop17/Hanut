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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.example.hanut.R;
import com.example.hanut.models.Post;
import com.example.hanut.models.User;
import com.example.hanut.providers.AuthProvider;
import com.example.hanut.providers.ImageProvider;
import com.example.hanut.providers.UserProvider;
import com.example.hanut.utils.FileUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView mCircleImageViewProfile;
    ImageView mImageViewCover;
    TextInputEditText mTextInputUsername;
    TextInputEditText mTextInputPhone;
    Button mButtonEditProfile;

    AlertDialog.Builder mBuilderSelector;
    CharSequence options[];
    private final int GALLERY_REQUEST_CODE_PROFILE = 1;
    private final int GALLERY_REQUEST_CODE_COVER = 2;
    private final int PHOTO_REQUEST_CODE_PROFILE = 3;
    private final int PHOTO_REQUEST_CODE_COVER = 4;

    // FOTO 1
    String mAbsolutePhotoPath;
    String mPhotoPath;
    File mPhotoFile;

    // FOTO 2
    String mAbsolutePhotoPath2;
    String mPhotoPath2;
    File mPhotoFile2;

    File mImageFile;
    File mImageFile2;

    String mUsername = "";
    String mPhone = "";

    AlertDialog mDialog;

    ImageProvider mImageProvider;
    UserProvider mUsersProvider;
    AuthProvider mAuthProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mCircleImageViewProfile = findViewById(R.id.circleImageProfile);
        mImageViewCover = findViewById(R.id.imageViewCover);
        mTextInputUsername = findViewById(R.id.textInputUsername);
        mTextInputPhone = findViewById(R.id.textInputPhone);
        mButtonEditProfile = findViewById(R.id.btnEditProfile);

        mBuilderSelector = new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Selecciona una opcion");
        options = new CharSequence[] {"Imagen de galeria", "Tomar foto"};

        mImageProvider = new ImageProvider();
        mUsersProvider = new UserProvider();
        mAuthProvider = new AuthProvider();

        mDialog = new SpotsDialog.Builder().setContext(this).setMessage("Espere un momento").setCancelable(false).build();

        mButtonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickEditProfile();
            }
        });

        mCircleImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionImage(1);
            }
        });

        mImageViewCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionImage(2);
            }
        });

    }

    private void clickEditProfile() {
        mUsername = mTextInputUsername.getText().toString();
        mPhone = mTextInputPhone.getText().toString();
        if (!mUsername.isEmpty() && !mPhone.isEmpty()) {
            if (mImageFile != null && mImageFile2 != null ) {
                saveImage(mImageFile, mImageFile2);
            }
            // TOMO LAS DOS FOTOS DE LA CAMARA
            else if (mPhotoFile != null && mPhotoFile2 != null) {
                saveImage(mPhotoFile, mPhotoFile2);
            }
            else if (mImageFile != null && mPhotoFile2 != null) {
                saveImage(mImageFile, mPhotoFile2);
            }
            else if (mPhotoFile != null && mImageFile2 != null) {
                saveImage(mPhotoFile, mImageFile2);
            }
            else {
                Toast.makeText(this, "Debes seleccionar una imagen", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Ingrese el nombre de usuario y el telefono", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage(File imageFile1, final File imageFile2) {
        mDialog.show();
        mImageProvider.save(EditProfileActivity.this, imageFile1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String urlProfile = uri.toString();

                            mImageProvider.save(EditProfileActivity.this, imageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if (taskImage2.isSuccessful()) {
                                        mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                                String urlCover = uri2.toString();
                                                User user = new User();
                                                user.setUserName(mUsername);
                                                user.setPhone(mPhone);
                                                user.setImageProfile(urlProfile);
                                                user.setImageCover(urlCover);
                                                user.setId(mAuthProvider.getUid());
                                                mUsersProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        mDialog.dismiss();
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(EditProfileActivity.this, "La informacion se actualizo correctamente", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else {
                                                            Toast.makeText(EditProfileActivity.this, "La informacion no se pudo actualizar", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            }
                                        });
                                    }
                                    else {
                                        mDialog.dismiss();
                                        Toast.makeText(EditProfileActivity.this, "La imagen numero 2 no se pudo guardar", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Hubo error al almacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void selectOptionImage(final int numberImage) {

        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    if (numberImage == 1) {
                        openGallery(GALLERY_REQUEST_CODE_PROFILE);
                    }
                    else if (numberImage == 2) {
                        openGallery(GALLERY_REQUEST_CODE_COVER);
                    }
                }
                else if (i == 1){
                    if (numberImage == 1) {
                        takePhoto(PHOTO_REQUEST_CODE_PROFILE);
                    }
                    else if (numberImage == 2) {
                        takePhoto(PHOTO_REQUEST_CODE_COVER);
                    }
                }
            }
        });

        mBuilderSelector.show();

    }

    private void takePhoto(int requestCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createPhotoFile(requestCode);
            } catch(Exception e) {
                Toast.makeText(this, "Hubo un error con el archivo " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(EditProfileActivity.this, "com.optic.socialmediagamer", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    private File createPhotoFile(int requestCode) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
                new Date() + "_photo",
                ".jpg",
                storageDir
        );
        if (requestCode == PHOTO_REQUEST_CODE_PROFILE) {
            mPhotoPath = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath = photoFile.getAbsolutePath();
        }
        else if (requestCode == PHOTO_REQUEST_CODE_COVER) {
            mPhotoPath2 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath2 = photoFile.getAbsolutePath();
        }
        return photoFile;
    }

    private void openGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * SELECCION DE IMAGEN DESDE LA GALERIA
         */
        if (requestCode == GALLERY_REQUEST_CODE_PROFILE && resultCode == RESULT_OK) {
            try {
                mPhotoFile = null;
                mImageFile = FileUtil.from(this, data.getData());
                mCircleImageViewProfile.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            } catch(Exception e) {
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE_COVER && resultCode == RESULT_OK) {
            try {
                mPhotoFile2 = null;
                mImageFile2 = FileUtil.from(this, data.getData());
                mImageViewCover.setImageBitmap(BitmapFactory.decodeFile(mImageFile2.getAbsolutePath()));
            } catch(Exception e) {
                Log.d("ERROR", "Se produjo un error " + e.getMessage());
                Toast.makeText(this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        /**
         * SELECCION DE FOTOGRAFIA
         */
        if (requestCode == PHOTO_REQUEST_CODE_PROFILE && resultCode == RESULT_OK) {
            mImageFile = null;
            mPhotoFile = new File(mAbsolutePhotoPath);
            Picasso.with(EditProfileActivity.this).load(mPhotoPath).into(mCircleImageViewProfile);
        }

        /**
         * SELECCION DE FOTOGRAFIA
         */
        if (requestCode == PHOTO_REQUEST_CODE_COVER && resultCode == RESULT_OK) {
            mImageFile2 = null;
            mPhotoFile2 = new File(mAbsolutePhotoPath2);
            Picasso.with(EditProfileActivity.this).load(mPhotoPath2).into(mImageViewCover);
        }
    }

}
