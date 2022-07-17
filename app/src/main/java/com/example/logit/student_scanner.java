package com.example.logit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Size;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.ExecutionException;

public class student_scanner extends AppCompatActivity {

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_scanner);

        previewView = findViewById(R.id.cameraPreview);



        // checking for camera permissions
        if(ContextCompat.checkSelfPermission(student_scanner.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                init();
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
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                    bindImageAnalysis(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },ContextCompat.getMainExecutor(student_scanner.this));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(student_scanner.this, "Permissions Granted", Toast.LENGTH_SHORT).show();
            init();
        }else {
            Toast.makeText(student_scanner.this, "Permissions Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void bindImageAnalysis(ProcessCameraProvider processCameraProvider){


        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(student_scanner.this), new ImageAnalysis.Analyzer() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void analyze(@NonNull @NotNull ImageProxy image) {
                Image mediaImage = image.getImage();

                if(mediaImage != null){
                    InputImage image2 = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());

                    BarcodeScanner scanner = BarcodeScanning.getClient();
                    Task<List<Barcode>> results = scanner.process(image2);



                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar c = Calendar.getInstance();
                    String date = sdf.format(c.getTime());

                    results.addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {

                            for(Barcode barcode: barcodes){
                                final String getValue = barcode.getRawValue();
                                if(getValue.equals("test")) {
                                    Toast.makeText(student_scanner.this, "Logged in " + date, Toast.LENGTH_SHORT).show();
                                }
                            }

                            image.close();
                            mediaImage.close();
                        }
                    });


                }
            }
        });

        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        processCameraProvider.bindToLifecycle(student_scanner.this, cameraSelector, imageAnalysis, preview);
    }
}