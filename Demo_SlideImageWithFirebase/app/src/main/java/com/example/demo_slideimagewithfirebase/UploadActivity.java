package com.example.demo_slideimagewithfirebase;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.demo_slideimagewithfirebase.model.ImageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class UploadActivity extends AppCompatActivity {

    ImageView uploadUrlImage;
    Button saveButton;
    EditText uploadNameImage, uploadLinkWeb, uploadNoteImage;
    String imageURL;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Ánh xạ các thành phần giao diện
        uploadUrlImage = findViewById(R.id.uploadUrlImage);
        uploadNameImage = findViewById(R.id.uploadNameImage);
        uploadLinkWeb = findViewById(R.id.uploadLinkWeb);
        uploadNoteImage = findViewById(R.id.uploadNoteImage);
        saveButton = findViewById(R.id.saveButton);

        // Đăng ký để nhận kết quả từ ActivityResultLauncher
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadUrlImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UploadActivity.this, "Hãy chọn ảnh", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Thiết lập sự kiện click cho ImageView để chọn hình ảnh từ thư viện
        uploadUrlImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        // Thiết lập sự kiện click cho nút lưu
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveData(); // Gọi phương thức lưu dữ liệu
                }catch (Exception e){
                    Toast.makeText(UploadActivity.this, "Lỗi trong quá trình cập nhật thông tin", Toast.LENGTH_SHORT).show();
                }
                finish(); // Đóng activity sau khi lưu dữ liệu
            }
        });
    }

    // Phương thức lưu dữ liệu lên Firebase Storage và Firebase Realtime Database
    public void saveData(){

        // Tham chiếu đến Firebase Storage để lưu ảnh
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Slide Images")
                .child(uri.getLastPathSegment());

        // Hiển thị dialog progress
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Đưa ảnh lên Firebase Storage
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // Lấy đường dẫn URL của ảnh từ Firebase Storage
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();

                // Gọi phương thức uploadData() để lưu dữ liệu vào Firebase Realtime Database
                uploadData();
                dialog.dismiss(); // Đóng dialog sau khi tải xong
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss(); // Đóng dialog khi có lỗi xảy ra
            }
        });
    }

    // Phương thức lưu dữ liệu vào Firebase Realtime Database
    public void uploadData(){

        // Lấy dữ liệu từ các trường nhập liệu
        String nameImage = uploadNameImage.getText().toString();
        String linkWeb = uploadLinkWeb.getText().toString();
        String noteImage = uploadNoteImage.getText().toString();

        // Xử lý trường hợp các trường nhập liệu rỗng
        nameImage = nameImage.isEmpty() ? "No Name Image" : nameImage;
        linkWeb = linkWeb.isEmpty() ? "No Link Web" : linkWeb;
        noteImage = noteImage.isEmpty() ? "No Note Image" : noteImage;

        // Tạo đối tượng ImageModel và cập nhật dữ liệu vào Firebase Realtime Database
        ImageModel dataClass = new ImageModel(imageURL, nameImage, linkWeb, noteImage);

        // Lấy thời gian hiện tại để làm key cho nút dữ liệu trong Firebase Realtime Database
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance().getReference("SlideImage").child(currentDate)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Hiển thị thông báo và đóng activity sau khi lưu thành công
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UploadActivity.this, "Lưu ảnh thành công", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }, 1000);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
