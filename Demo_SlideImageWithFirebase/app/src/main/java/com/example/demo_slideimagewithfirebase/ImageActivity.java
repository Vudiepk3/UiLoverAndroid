package com.example.demo_slideimagewithfirebase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.demo_slideimagewithfirebase.databinding.ActivityImageBinding;
import com.example.demo_slideimagewithfirebase.model.BannerImageModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageActivity extends AppCompatActivity {

    // Khai báo các hằng số và biến cần thiết
    private static final String TAG = "ImageActivity";
    private static final String PREFS_NAME = "BannerPrefs";
    private static final String BANNER_ONE = "bannerOne";
    private static final String BANNER_TWO = "bannerTwo";
    private static final String BANNER_THREE = "bannerThree";
    private static final String BANNER_FOUR = "bannerFour";

    ActivityImageBinding binding;
    Uri bannerOne, bannerTwo, bannerThree, bannerFour;

    FirebaseDatabase database;
    FirebaseStorage storage;

    // Khai báo ActivityResultLauncher để xử lý kết quả trả về từ trình chọn ảnh
    private final ActivityResultLauncher<Intent> startForMediaPickerResult1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent intent = result.getData();
                if (intent != null && result.getResultCode() == RESULT_OK) {
                    bannerOne = intent.getData();
                    binding.imageOne.setImageURI(bannerOne);
                    uploadImage(bannerOne, "banner_image_one", BANNER_ONE);
                }
            }
    );

    private final ActivityResultLauncher<Intent> startForMediaPickerResult2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent intent = result.getData();
                if (intent != null && result.getResultCode() == RESULT_OK) {
                    bannerTwo = intent.getData();
                    binding.imageTwo.setImageURI(bannerTwo);
                    uploadImage(bannerTwo, "banner_image_two", BANNER_TWO);
                }
            }
    );

    private final ActivityResultLauncher<Intent> startForMediaPickerResult3 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent intent = result.getData();
                if (intent != null && result.getResultCode() == RESULT_OK) {
                    bannerThree = intent.getData();
                    binding.imageThree.setImageURI(bannerThree);
                    uploadImage(bannerThree, "banner_image_three", BANNER_THREE);
                }
            }
    );

    private final ActivityResultLauncher<Intent> startForMediaPickerResult4 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent intent = result.getData();
                if (intent != null && result.getResultCode() == RESULT_OK) {
                    bannerFour = intent.getData();
                    binding.imageFour.setImageURI(bannerFour);
                    uploadImage(bannerFour, "banner_image_four", BANNER_FOUR);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        // Thiết lập sự kiện khi người dùng nhấp vào các nút để tải ảnh lên
        binding.btnUploadImageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageOne();
            }
        });
        binding.btnUploadImageTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageTwo();
            }
        });
        binding.btnUploadImageThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageThree();
            }
        });
        binding.btnUploadImageFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFour();
            }
        });

        // Tải các ảnh banner đã lưu trước đó
        loadBannerImages();
    }

    // Hàm upload ảnh lên Firebase Storage và lưu URL vào SharedPreferences
    private void uploadImage(Uri imageUri, String firebasePath, String preferenceKey) {
        final StorageReference storageReference = storage.getReference().child("banner_image").child(firebasePath);

        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        saveBannerUri(preferenceKey, uri.toString());
                        BannerImageModel imageModel = new BannerImageModel();
                        switch (preferenceKey) {
                            case BANNER_ONE:
                                imageModel.setBannerImageOne(uri.toString());
                                break;
                            case BANNER_TWO:
                                imageModel.setBannerImageTwo(uri.toString());
                                break;
                            case BANNER_THREE:
                                imageModel.setBannerImageThree(uri.toString());
                                break;
                            case BANNER_FOUR:
                                imageModel.setBannerImageFour(uri.toString());
                                break;
                        }
                        database.getReference().child("banner_image").child(firebasePath).setValue(imageModel)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ImageActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ImageActivity.this, "Image Not Uploaded", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ImageActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ImageActivity.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm lưu URL ảnh vào SharedPreferences
    private void saveBannerUri(String key, String uri) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, uri);
        editor.apply();
        Log.d(TAG, "Saved URI " + key + ": " + uri);
    }

    // Hàm tải các ảnh banner từ Firebase Storage và hiển thị
    private void loadBannerImages() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Load banner one
        StorageReference bannerOneRef = storage.getReference().child("banner_image/banner_image_one");
        loadBannerImage(bannerOneRef, binding.imageOne);

        // Load banner two
        StorageReference bannerTwoRef = storage.getReference().child("banner_image/banner_image_two");
        loadBannerImage(bannerTwoRef, binding.imageTwo);

        // Load banner three
        StorageReference bannerThreeRef = storage.getReference().child("banner_image/banner_image_three");
        loadBannerImage(bannerThreeRef, binding.imageThree);

        // Load banner four
        StorageReference bannerFourRef = storage.getReference().child("banner_image/banner_image_four");
        loadBannerImage(bannerFourRef, binding.imageFour);
    }

    // Hàm tải một ảnh banner từ Firebase Storage và hiển thị lên ImageView
    private void loadBannerImage(StorageReference imageRef, ImageView imageView) {
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(ImageActivity.this)
                    .load(uri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Vô hiệu hóa bộ nhớ đệm đĩa
                    .skipMemoryCache(true) // Vô hiệu hóa bộ nhớ đệm trong RAM
                    .into(imageView);
        }).addOnFailureListener(e -> {
            Log.d(TAG, "Failed to load banner image: " + e.getMessage());
        });
    }

    // Các hàm để mở trình chọn ảnh cho từng ảnh banner
    private void pickImageOne() {
        pickImage(startForMediaPickerResult1);
    }

    private void pickImageTwo() {
        pickImage(startForMediaPickerResult2);
    }

    private void pickImageThree() {
        pickImage(startForMediaPickerResult3);
    }

    private void pickImageFour() {
        pickImage(startForMediaPickerResult4);
    }

    // Hàm chung để mở trình chọn ảnh
    private void pickImage(ActivityResultLauncher<Intent> launcher) {
        String[] mineType = {"image/*", "image/jpeg", "image/png"};
        ImagePicker.Companion.with(this)
                .saveDir(ImageActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
                .galleryOnly()
                .galleryMimeTypes(mineType)
                .crop(16f, 9f)
                .compress(512)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    launcher.launch(intent);
                    return null;
                });
    }
}
