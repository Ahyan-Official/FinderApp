package com.visualsearch.finder.vs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.visualsearch.finder.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainFirstActivity extends AppCompatActivity {





    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 122;
    public  static final int PERMISSIONS_MULTIPLE_REQUESTSMALL = 128;

    private static  final int GALLERY_REQUEST = 1;
    private static  final int CAMERA_REQUEST = 60;

    Intent galleryIntent;

    File ff;
    FirebaseFirestore db;
    ProgressDialog mProgress;
    Uri imageUriP;

    ImageView camera,gallery,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_first);

        mProgress = new ProgressDialog(MainFirstActivity.this);

        image = findViewById(R.id.image);
        camera = findViewById(R.id.camera);
        gallery = findViewById(R.id.gallery);

        db = FirebaseFirestore.getInstance();

        final String dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/Folder/";
        File newdir = new File(dir);
        newdir.mkdirs();


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                ImageButtom();

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //capturarFoto();
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });


    }


    public void ImageButtom(){



        if(Build.VERSION.SDK_INT >=16 ) {

            if (isReadStorageAllowed() && isWriteStorageAllowed()) {


                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Choose"), GALLERY_REQUEST);

            } else {

                if(Build.VERSION.SDK_INT >=23 ) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_MULTIPLE_REQUEST);

                }
            }
        }

        if(Build.VERSION.SDK_INT <=15 ) {

            if (isWriteStorageAllowed()) {

                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"Choose"), GALLERY_REQUEST);

            } else {

                if(Build.VERSION.SDK_INT >=23) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_MULTIPLE_REQUESTSMALL);
                }
            }
        }

    }


    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }
    private boolean isWriteStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean readPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(readPermission && writePermission) {

                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(galleryIntent,"Choose"), GALLERY_REQUEST);


                    } else {

                    }
                }
                break;
            case PERMISSIONS_MULTIPLE_REQUESTSMALL:
                if (grantResults.length > 0) {
                    boolean writePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(writePermission) {

                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(galleryIntent, "Choose"), GALLERY_REQUEST);


                    } else {

                    }
                }
                break;
        }
    }




    Uri UriGlobal;

    class MyTaskOSU extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }

        @Override
        protected Void doInBackground(Void... params) {

            uploadImageSingle(UriGlobal);

            return null;
        }

        void uploadImageSingle(Uri imageUri){

            Log.e("hello", "uploadImageSingle: 11111" );

            if (imageUri != null) {


                Log.e("hello", "uploadImageSingle: asdasdadd" );
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String  UID =auth.getCurrentUser().getUid();

                StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("requests").child(UID);


                final String imageName = imageUri.getLastPathSegment() + randomString(30);
                StorageReference filePath = mStorage.child(imageName);






                UploadTask uploadTask = filePath.putFile(imageUri);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return task.getResult().getStorage().getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri download = task.getResult();


                            //String suri = taskSnapshot.getDownloadUrl().getEncodedPath();
                            ///Toast.makeText(getApplicationContext()," "+suri+" ",Toast.LENGTH_LONG).show();
                            final Uri downloadUri = download;
                            final String downloadUrlStr = download.toString();
                            final String imageToken = download.getQuery();


                            if (!downloadUrlStr.isEmpty() && downloadUrlStr != null) {

                                if (imageName!=null && !imageName.isEmpty() && imageToken!=null && !imageToken.isEmpty()) {






                                    final DocumentReference RequestDoc = db.collection("requests").document();
                                    Map<String, Object> request = new HashMap<>();

                                    request.put("product_image",downloadUrlStr);
                                    request.put("product_image_name",imageName);
                                    request.put("product_image_token",imageToken);
                                    request.put("_id",RequestDoc.getId());

                                    RequestDoc.set(request, SetOptions.merge() ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                if(mProgress!=null) {

                                                    mProgress.dismiss();

                                                }





                                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                intent.putExtra("link",downloadUrlStr);


                                                startActivity(intent);
                                                intent = null;
                                                finish();


                                            }else{

                                                if(mProgress!=null) {
                                                    mProgress.dismiss();

                                                }

                                            }
                                        }
                                    });





                                }
                            }




                        }else if(!task.isSuccessful()){

                            Runnable run4 = new Runnable() {
                                @Override
                                public void run() {
                                    if(mProgress!=null) {
                                        mProgress.dismiss();

                                    }

                                }
                            };
                            runOnUiThread(run4);




                        }
                    };




                });






            }else{
                Log.e("aaaaa", "uploadImageSingle: cccccc" );
            }




        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {


            UriGlobal = null;


            Uri de = data.getData();
            imageUriP = de;


            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(de);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bmp = BitmapFactory.decodeStream(imageStream);


            int xx = bmp.getWidth();
            int yy = bmp.getHeight();

            Log.e("imagedataReal=w", String.valueOf(xx));
            Log.e("imagedataReal=h", String.valueOf(yy));

            //Glide.with(getApplicationContext()).load(imageUri1).into(imageBtnGallery);
            File f = new File(Environment.getExternalStorageDirectory().toString() + "/" + "Afindr" + "/" + "Afindr Images");
            if (!f.exists()) {
                f.mkdirs();
            }
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String esd = path.toString();
            ff = new File(esd, "Image.PNG");


            UCrop.Options vv = new UCrop.Options();

            vv.setCompressionFormat(Bitmap.CompressFormat.PNG);
            vv.setCompressionQuality(90);
            UCrop.of(de, Uri.fromFile(ff))
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(1500, 1500)
                    .start(MainFirstActivity.this);





        }else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {


            UriGlobal = null;

            Log.e("Hellow", "hellow");

            UriGlobal = UCrop.getOutput(data);
            Uri aa = UCrop.getOutput(data);

            Picasso.with(getApplicationContext()).load(aa).into(image);
//            Glide.with(getApplicationContext()).load(aa).into(image);


            mProgress.setMessage("Uploading Image");
            mProgress.show();
            MyTaskOSU myTaskOSU = new MyTaskOSU();
            myTaskOSU.execute();


        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.e("Hellowerror", cropError.getMessage());

        }

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {


            UriGlobal = null;


//            Uri de = data.getData();
//            imageUriP = de;
//            UriGlobal = de;

            InputStream imageStream = null;
//            try {
//                //imageStream = getContentResolver().openInputStream(de);
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

            //Bitmap image = (Bitmap) data.getExtras().get("data");
            Bitmap bmp = (Bitmap) data.getExtras().get("data");


            int xx = bmp.getWidth();
            int yy = bmp.getHeight();

            Log.e("imagedataReal=w", String.valueOf(xx));
            Log.e("imagedataReal=h", String.valueOf(yy));

            Glide.with(getApplicationContext()).load(bmp).into(image);
            File f = new File(Environment.getExternalStorageDirectory().toString() + "/" + "Afindr" + "/" + "Afindr Images");
            if (!f.exists()) {
                f.mkdirs();
            }
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String esd = path.toString();
            ff = new File(esd, "Image.PNG");


            try (FileOutputStream out = new FileOutputStream(ff)) {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (IOException e) {
                e.printStackTrace();
            }

            UriGlobal = Uri.fromFile(ff);
            mProgress.setMessage("Uploading Image");
            mProgress.show();

            MyTaskOSU myTaskOSU = new MyTaskOSU();
            myTaskOSU.execute();



//            UCrop.Options vv = new UCrop.Options();
//
//            vv.setCompressionFormat(Bitmap.CompressFormat.PNG);
//            vv.setCompressionQuality(90);
//            UCrop.of(de, Uri.fromFile(ff))
//                    .withAspectRatio(1, 1)
//                    .withMaxResultSize(1500, 1500)
//                    .start(MainFirstActivity.this);





        }



    }



    String AB;
    SecureRandom sr = new SecureRandom();
    String randomString(int len){
        AB = "QWERTYUIOPLKJHGFDSAZXCVBNM1234567890";
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(sr.nextInt(AB.length())));
        return sb.toString();

    }

    private void capturarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        Uri imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, CAMERA_REQUEST);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}