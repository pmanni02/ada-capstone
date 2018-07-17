package com.example.phoebemanning.capstone.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phoebemanning.capstone.BuildConfig;
import com.example.phoebemanning.capstone.Models.Search_Models.Item;
import com.example.phoebemanning.capstone.Models.Search_Models.List;
import com.example.phoebemanning.capstone.Models.Search_Models.ResponseData;
import com.example.phoebemanning.capstone.Models.User;
import com.example.phoebemanning.capstone.R;
import com.example.phoebemanning.capstone.Apis.UsdaApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText upcEditText;
    TextView userName;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int REQUEST_CODE = 1;

    String mCurrentPhotoPath;
    private Uri photoURI;
    File photoFile = null;

    public void submitOnClick(View view) {

//      Hide the Keyboard after submit
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(mgr).hideSoftInputFromWindow(upcEditText.getWindowToken(), 0);

        if (upcEditText.length() != 12){
            Toast.makeText(this, "Invalid UPC", Toast.LENGTH_LONG).show();
        } else {
            getProductCode(upcEditText.getText().toString());
        }
    }

    public void scanBtnClick(View view){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            } else {
                Toast.makeText(this, "You already have permission", Toast.LENGTH_SHORT).show();
                takePicIntent();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                takePicIntent();
            } else {
                Toast.makeText(this, "Permission NOT granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void takePicIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = createImageFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovidercapstone",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }

    private File createImageFile() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        String imageFileName = "JPEG_" + format + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bitmap rotatedImg = null;
            try {
                rotatedImg = rotateImage(photoFile.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

//            Log.i("PHOTO_PATH", mCurrentPhotoPath);
//            Log.i("EXTRAS", photoURI.getPath());

//            barcodeRecognitionURI(photoURI);
            barcodeRecognitionBitmap(rotatedImg);
        }
    }

    public Bitmap rotateImage(String file) throws IOException{

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(file, opts);

        int rotationAngle = getCameraPhotoOrientation(MainActivity.this, photoURI, file);

        Matrix matrix = new Matrix();
        matrix.postRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        FileOutputStream fos=new FileOutputStream(file);
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();

        return rotatedBitmap;
    }

    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    rotate = 0;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    private void barcodeRecognitionBitmap(Bitmap rotatedImg) {

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(rotatedImg);

        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                                FirebaseVisionBarcode.FORMAT_UPC_A,
                                FirebaseVisionBarcode.FORMAT_UPC_E)
                        .build();

        FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
                .getVisionBarcodeDetector(options);

        Task<java.util.List<FirebaseVisionBarcode>> result = detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<java.util.List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(java.util.List<FirebaseVisionBarcode> barcodes) {
                        for (FirebaseVisionBarcode barcode: barcodes) {
                            Rect bounds = barcode.getBoundingBox();
                            Point[] corners = barcode.getCornerPoints();

                            String rawValue = barcode.getRawValue();

                            Toast.makeText(MainActivity.this, rawValue, Toast.LENGTH_SHORT).show();
//                            resultText.setText(rawValue);

//                          TODO: check that upc code is valid
                            getProductCode(rawValue);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getProductCode(final String upcCode){
//        Log.i("UPC TEXT: ", upcEditText.getText().toString());
        Call<ResponseData> call = UsdaApi.getClient().getResponse(upcCode, BuildConfig.ApiKey);

        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if (response.isSuccessful()){
                    if (response.body() != null){

                        List list = response.body().getList();

                        if (list != null){
                            java.util.List<Item> item = list.getItem();
                            String name = item.get(0).getName();
                            String ndbno = item.get(0).getNdbno();

                            Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                            intent.putExtra("name",name);
                            intent.putExtra("ndbno", ndbno);
                            if(upcEditText.getText().toString().equals("")){
                                intent.putExtra("upc", upcCode);
                            } else {
                                intent.putExtra("upc", upcEditText.getText().toString());
                            }

                            startActivity(intent);

                        } else {
                            Toast.makeText(MainActivity.this, "Your search resulted in zero results. Change your parameters and try again", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        setContentView(R.layout.activity_main);

        upcEditText = findViewById(R.id.upcEditText);
        userName = findViewById(R.id.userName);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String current_id = mUser.getUid();
        final DatabaseReference databaseReference = database.getReference().child("Users").child(current_id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String fullName = null;
                if (user != null) {
                    fullName = "Welcome " + user.getFirstName() + " " + user.getLastName() + "!";
                }
                userName.setText(fullName);
//                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.no_favorite_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logout:
                if(mUser !=null && mAuth != null){
                    mAuth.signOut();
                    Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
                return true;

            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
                finish();
                return true;
                
            case R.id.action_information:
                startActivity(new Intent(MainActivity.this, InfoActivity.class));
                finish();
                return true;

            case R.id.action_user_scans:
                startActivity(new Intent(MainActivity.this, UserScansActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
