package com.example.nutriapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

public class AccountFragment extends Fragment {

    private ImageView imageViewProfile;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private final int PERMISSION_REQUEST_CODE = 100;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private SharedPreferences sharedPreferences;
    private TextView displayEmail;
    private TextView displayLocation;
    private TextView settingsTextView;
    private TextView changePasswordTextView;
    private TextView editPersonalInfoTextView;
    private TextView workoutHistoryTextView;
    private boolean isFragmentAttached = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        displayEmail = view.findViewById(R.id.displayEmail);
        displayLocation = view.findViewById(R.id.displayLocation);
        TextView logoutButton = view.findViewById(R.id.logoutBack);
        editPersonalInfoTextView = view.findViewById(R.id.EditPersonalInfo);
        changePasswordTextView = view.findViewById(R.id.ChangePasswordLiink);
        settingsTextView = view.findViewById(R.id.settings);
        workoutHistoryTextView = view.findViewById(R.id.workoutHistory);

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        loadUserProfile();

        imageViewProfile.setOnClickListener(v -> checkPermissionAndSelectImage());
        editPersonalInfoTextView.setOnClickListener(v -> editPersonalInfo(v));
        changePasswordTextView.setOnClickListener(v -> changePassword(v));
        settingsTextView.setOnClickListener(v ->  accountSettings(v));
        workoutHistoryTextView.setOnClickListener(v -> workoutHistory(v));

        logoutButton.setOnClickListener(v -> showLogoutConfirmationDialog());

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        isFragmentAttached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isFragmentAttached = false;
    }

    private void loadUserProfile() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            displayEmail.setText(currentUser.getEmail());
            String savedImageUrl = sharedPreferences.getString("profileImageUrl", null);
            if (savedImageUrl != null && isFragmentAttached) {
                Glide.with(this).load(savedImageUrl).placeholder(R.drawable.person).into(imageViewProfile);
            } else {
                databaseReference.child("Users").child(currentUser.getUid()).child("profileImageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && isFragmentAttached) {
                            String imageUrl = snapshot.getValue(String.class);
                            saveImageUrlToPersistentStorage(imageUrl);
                            Glide.with(AccountFragment.this).load(imageUrl).placeholder(R.drawable.person).into(imageViewProfile);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        if (isFragmentAttached) {
                            Toast.makeText(getContext(), "Failed to load user profile image.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            // Update display location if it's available
            String updatedLocation = getActivity().getIntent().getStringExtra("updatedLocation");
            if (updatedLocation != null && !updatedLocation.isEmpty()) {
                displayLocation.setText(updatedLocation);
            }
        }
    }

    private void saveImageUrlToPersistentStorage(String imageUrl) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profileImageUrl", imageUrl);
        editor.apply();
    }

    private void checkPermissionAndSelectImage() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            openImageChooser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openImageChooser();
        } else {
            Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                Bitmap optimizedBitmap = getResizedBitmap(bitmap, 500); // Example resize to max 500px
                imageViewProfile.setImageBitmap(optimizedBitmap);
                uploadImage(optimizedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void uploadImage(Bitmap bitmap) {
        if (bitmap != null && currentUser != null) {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            String imageName = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("profile_images/" + imageName);

            UploadTask uploadTask = ref.putBytes(data);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Profile Image Uploaded!!", Toast.LENGTH_SHORT).show();
                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    saveImageUrlToDatabase(downloadUrl);
                    saveImageUrlToPersistentStorage(downloadUrl);
                });
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded " + (int) progress + "%");
            });
        }
    }

    private void saveImageUrlToDatabase(String imageUrl) {
        if (currentUser != null) {
            databaseReference.child("Users").child(currentUser.getUid()).child("profileImageUrl").setValue(imageUrl)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getContext(), "Failed to save image URL.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    clearSavedData();
                    FirebaseAuth.getInstance().signOut();
                    redirectToLogin();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void clearSavedData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void redirectToLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void editPersonalInfo(View view) {
        startActivity(new Intent(getContext(), EditActivity.class));
    }

    public void changePassword(View view) {
        startActivity(new Intent(getContext(), PasswordChangedActivity.class));
    }

    public void accountSettings(View view) {
        startActivity(new Intent(getContext(), SettingActivity.class));
    }

    public void workoutHistory(View view) {
        startActivity(new Intent(getContext(), WorkoutHistoryActivity.class));
    }
}
