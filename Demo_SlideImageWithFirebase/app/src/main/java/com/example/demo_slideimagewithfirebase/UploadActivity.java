package com.example.demo_slideimagewithfirebase;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.demo_slideimagewithfirebase.databinding.ActivityUploadBinding;
import com.example.demo_slideimagewithfirebase.model.ImageModel;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.text.DateFormat;
import java.util.Calendar;

public class UploadActivity extends AppCompatActivity {

    private ActivityUploadBinding binding;
    private String imageURL;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::onActivityResult);

        binding.uploadImage.setOnClickListener(v -> {
            Intent photoPicker = new Intent(Intent.ACTION_PICK);
            photoPicker.setType("image/*");
            activityResultLauncher.launch(photoPicker);
        });

        binding.saveButton.setOnClickListener(v -> saveData());
    }

    private void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                uri = data.getData();
                binding.uploadImage.setImageURI(uri);
            }
        } else {
            Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveData() {
        if (uri == null) {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Slide Image").child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(uriTask -> {
                if (uriTask.isSuccessful()) {
                    imageURL = uriTask.getResult().toString();
                    uploadData();
                    dialog.dismiss();
                }
            });
        }).addOnFailureListener(e -> dialog.dismiss());
    }

    private void uploadData() {
        String nameImage = binding.uploadNameImage.getText().toString();
        String noteImage = binding.uploadNoteImage.getText().toString();
        String linkWeb = binding.uploadLinkWeb.getText().toString();

        nameImage = nameImage.isEmpty() ? "Null" : nameImage;
        noteImage = noteImage.isEmpty() ? "Null": noteImage;
        linkWeb = linkWeb.isEmpty() ? "Null" : linkWeb;

        ImageModel dataClass = new ImageModel(imageURL,nameImage,noteImage,linkWeb);
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance().getReference("SlideImage").child(currentDate)
                .setValue(dataClass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(e ->
                        Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
