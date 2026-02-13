package com.simats.prepace;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        UserProfileManager userManager = UserProfileManager.getInstance(this);

        android.widget.EditText etName = findViewById(R.id.etName);
        android.widget.EditText etEmail = findViewById(R.id.etEmail);
        android.widget.ImageView ivProfileImage = findViewById(R.id.ivProfileImage);
        
        // Load Data
        etName.setText(userManager.getName());
        etEmail.setText(userManager.getEmail());
        
        String savedUri = userManager.getAvatarUri();
        if (savedUri != null) {
            ivProfileImage.setImageURI(android.net.Uri.parse(savedUri));
        }

        // Image Picker Launcher
        androidx.activity.result.ActivityResultLauncher<String> mGetContent = registerForActivityResult(new androidx.activity.result.contract.ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    try {
                        // Copy image to internal storage to ensure persistent access
                        java.io.InputStream inputStream = getContentResolver().openInputStream(uri);
                        java.io.File file = new java.io.File(getFilesDir(), "profile_image.jpg");
                        java.io.FileOutputStream outputStream = new java.io.FileOutputStream(file);
                        
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                        
                        outputStream.close();
                        inputStream.close();
                        
                        // Save and Display
                        String filePath = android.net.Uri.fromFile(file).toString();
                        userManager.saveAvatarUri(filePath);
                        ivProfileImage.setImageURI(android.net.Uri.parse(filePath));
                        Toast.makeText(this, "Photo updated!", Toast.LENGTH_SHORT).show();
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        // Trigger Picker
        findViewById(R.id.cvAvatar).setOnClickListener(v -> mGetContent.launch("image/*"));

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String avatarUri = userManager.getAvatarUri();

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Name and Email are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save Local Data Only (No Server Sync)
            userManager.saveProfile(name, email, ""); // Phone is explicitly empty
            
            Toast.makeText(EditProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
