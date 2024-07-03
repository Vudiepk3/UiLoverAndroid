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

        // Ánh xạ các thành phần giao diện
        updateButton = findViewById(R.id.updateButton);
        updateUrlImage = findViewById(R.id.updateUrlImage);
        updateNameImage = findViewById(R.id.updateNameImage);
        updateLinkWeb = findViewById(R.id.updateLinkWeb);
        updateNoteImage = findViewById(R.id.updateNoteImage);

        // Đăng ký để nhận kết quả từ ActivityResultLauncher
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
                            Toast.makeText(UpdateActivity.this, "Không có hình ảnh được chọn", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Lấy dữ liệu từ Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Glide.with(UpdateActivity.this).load(bundle.getString("UrlImage")).into(updateUrlImage);
            updateNameImage.setText(bundle.getString("NameImage"));
            updateLinkWeb.setText(bundle.getString("LinkWeb"));
            updateNoteImage.setText(bundle.getString("NoteImage"));
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("Image");
        }

        // Thiết lập sự kiện click cho ImageView để chọn hình ảnh mới
        updateUrlImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        // Thiết lập sự kiện click cho nút cập nhật
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveData(); // Gọi phương thức lưu dữ liệu
                }catch (Exception e){
                    Toast.makeText(UpdateActivity.this, "Lỗi trong quá trình cập nhật thông tin", Toast.LENGTH_SHORT).show();
                }
                finish(); // Đóng activity sau khi cập nhật
            }
        });
    }

    // Phương thức lưu dữ liệu
    public void saveData(){
        // Lưu hình ảnh vào Firebase Storage và cập nhật dữ liệu vào Firebase Realtime Database
        storageReference = FirebaseStorage.getInstance().getReference().child("Slide Image").child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Đưa hình ảnh lên Firebase Storage
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                updateData(); // Gọi phương thức cập nhật dữ liệu
                dialog.dismiss(); // Đóng dialog sau khi tải xong
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss(); // Đóng dialog khi có lỗi xảy ra
            }
        });
    }

    // Phương thức cập nhật dữ liệu lên Firebase Realtime Database
    public void updateData(){
        nameImage = updateNameImage.getText().toString().trim();
        linkWeb = updateLinkWeb.getText().toString().trim();
        noteImage = updateNoteImage.getText().toString().trim();

        // Tạo đối tượng ImageModel mới và cập nhật dữ liệu
        ImageModel dataClass = new ImageModel(imageUrl, nameImage, linkWeb, noteImage);
        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    // Xóa hình ảnh cũ từ Firebase Storage sau khi cập nhật thành công
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                    reference.delete();
                    Toast.makeText(UpdateActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng activity sau khi cập nhật thành công
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
