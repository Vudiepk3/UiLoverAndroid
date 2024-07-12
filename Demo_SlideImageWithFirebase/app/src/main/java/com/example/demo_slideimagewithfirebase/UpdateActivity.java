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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.demo_slideimagewithfirebase.model.ImageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateActivity extends AppCompatActivity {

    ImageView updateUrlImage;
    Button updateButton;
    EditText updateNameImage, updateLinkWeb, updateNoteImage;
    String nameImage, linkWeb, noteImage;
    String imageUrl;
    String key, oldImageURL;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Ánh xạ và thiết lập các thành phần giao diện
        updateButton = findViewById(R.id.updateButton);
        updateUrlImage = findViewById(R.id.updateUrlImage);
        updateNameImage = findViewById(R.id.updateNameImage);
        updateLinkWeb = findViewById(R.id.updateLinkWeb);
        updateNoteImage = findViewById(R.id.updateNoteImage);

        // Đăng ký ActivityResultLauncher để chọn hình ảnh từ thư viện
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            updateUrlImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UpdateActivity.this, "Không ảnh nào được chọn", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Lấy dữ liệu từ Intent và hiển thị lên giao diện
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Glide.with(UpdateActivity.this).load(bundle.getString("UrlImage")).into(updateUrlImage);
            updateNameImage.setText(bundle.getString("NameImage"));
            updateLinkWeb.setText(bundle.getString("LinkWeb"));
            updateNoteImage.setText(bundle.getString("NoteImage"));
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("UrlImage");
        }

        // Thay đổi hình ảnh khi click vào ImageView để chọn hình ảnh mới
        updateUrlImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        // Xử lý sự kiện khi click vào nút Update
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lưu dữ liệu và cập nhật vào Firebase
                if (uri != null) {
                    saveDataWithNewImage();
                } else {
                    saveDataWithoutNewImage();
                }
            }
        });

        // Tạo tham chiếu đến Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("SlideImage").child(key);
    }

    // Phương thức để lưu dữ liệu và upload hình ảnh lên Firebase Storage
    public void saveDataWithNewImage(){
        // Tạo tham chiếu đến thư mục trên Firebase Storage và hiển thị dialog tiến trình
        storageReference = FirebaseStorage.getInstance().getReference().child("Slide Image").child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Upload hình ảnh lên Firebase Storage
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Lấy đường dẫn URL của hình ảnh đã upload
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();

                // Sau khi upload hình ảnh, cập nhật dữ liệu trên Firebase Realtime Database
                updateData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    // Phương thức để lưu dữ liệu lên Firebase Realtime Database mà không thay đổi hình ảnh
    public void saveDataWithoutNewImage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        imageUrl = oldImageURL; // Giữ nguyên URL của hình ảnh cũ
        updateData();
    }

    // Phương thức để cập nhật dữ liệu lên Firebase Realtime Database
    public void updateData(){
        // Lấy các giá trị từ các EditText
        nameImage = updateNameImage.getText().toString().trim();
        linkWeb = updateLinkWeb.getText().toString().trim();
        noteImage = updateNoteImage.getText().toString().trim();

        // Tạo đối tượng ImageModel mới và cập nhật dữ liệu
        ImageModel dataClass = new ImageModel(imageUrl, nameImage, linkWeb, noteImage);
        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // Nếu có ảnh mới, xóa hình ảnh cũ từ Firebase Storage
                    if (uri != null) {
                        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                        reference.delete();
                    }

                    // Hiển thị thông báo cập nhật thành công và kết thúc Activity
                    Toast.makeText(UpdateActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
