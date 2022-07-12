package com.example.logit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import org.jetbrains.annotations.NotNull;

public class student_scanner extends AppCompatActivity {

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_scanner);

        previewView = findViewById(R.id.cameraPreview);



        // checking for camera permissions
        if(ContextCompat.checkSelfPermission(student_scanner.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){

        }else {
            ActivityCompat.requestPermissions(student_scanner.this, new String[]{Manifest.permission.CAMERA},101);

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init(){
        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(student_scanner.this);

        cameraProviderListenableFuture.addListener(new Runnable() {
            @Override
            public void run() {

            }
        }.ContextCompat.getMainExecutor(student_scanner.this));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            init();
        }else {
            Toast.makeText(student_scanner.this, "Permissions Denied", Toast.LENGTH_SHORT).show();
        }
    }
}