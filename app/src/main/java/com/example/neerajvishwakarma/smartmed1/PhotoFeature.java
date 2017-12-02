package com.example.neerajvishwakarma.smartmed1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PhotoFeature extends AppCompatActivity
{

    private static int PICK_IMAGE_REQUEST = 1;
    private static int CAMERA_REQUEST = 1888;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    public static Bitmap im, bitmap, croppedBitmap;
    public static int width;
    public static int height;
    public static boolean isImageSelected = false;

    private String inputNodeName = "conv_input_input";
    String mCurrentPhotoPath;
    private String outputNodeName = "main_output/Softmax";
    String result;
    Intent cameraIntent;

    MedicineDB data;
    MedicineAccess access;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_feature);
        data = new MedicineDB(getApplicationContext());
        access = MedicineAccess.getInstance(getApplicationContext());
    }

    public void cameraClicked(View view)
    {
        Log.e("Tag----------", "OK1");
        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
        Log.e("Tag----------", "OK1");

        /*File photoFile = null;
        try
        {
            photoFile = createImageFile();
        }
        catch(IOException ioe)
        {

        }
        if (photoFile != null)
        {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        }*/
    }

    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public void galleryClicked(View view)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri uri = data.getData();
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                result = classifyMedicine(bitmap);
                Log.e("Test------------", result);

            }
            catch (IOException e)
            {
                System.out.println("Error 00");
                e.printStackTrace();
                isImageSelected = false;
            }
        }
        else if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK)
        {
            Log.e("Tag----------", "OK1");
            Bundle extras = data.getExtras();
            Log.e("Tag----------", "OK1");
            bitmap = (Bitmap) extras.get("data");
            Log.e("Tag----------", "OK2");
            isImageSelected = true;
            Log.e("Tag----------", "OK3");
            result = classifyMedicine(bitmap);
            Log.e("Tag----------", "OK4");
            Log.e("Test------------", result);

            Intent i = new Intent(PhotoFeature.this, MedInfo.class);
            i.putExtra("name", result);
            startActivity(i);
        }
        else
        {
            isImageSelected = false;
        }
    }

    public String classifyMedicine(Bitmap image)
    {
        width = image.getWidth();
        height = image.getHeight();
        //isImageSelected = true;

        String outputNames[] = new String[]{outputNodeName};
        croppedBitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);

        frameToCropTransform = getTransformationMatrix(width, height, 64, 64, 0, true);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(image, frameToCropTransform, null);

        int[] pixels = new int[64 * 64];
        float[] floatValues = new float[64 * 64 * 3];
        Log.e("Test-----", String.valueOf(croppedBitmap.getWidth()) + " " + String.valueOf(croppedBitmap.getHeight()));
        croppedBitmap.getPixels(pixels, 0, 64, 0, 0, 64, 64);

        for (int i = 0; i < pixels.length; ++i)
        {
            final int val = pixels[i];
            floatValues[i * 3 + 0] = ((val >> 16) & 0xFF);
            floatValues[i * 3 + 1] = ((val >> 8) & 0xFF);
            floatValues[i * 3 + 2] = (val & 0xFF);
        }

        TensorFlowInferenceInterface tensorflow = new TensorFlowInferenceInterface(getAssets(), "file:///android_asset/frozen_protobuf1.pb");
        tensorflow.feed("conv_input_input", floatValues, 1, 64, 64, 3);
        tensorflow.run(outputNames, false);

        float[] output = new float[8];

        tensorflow.fetch("main_output/Softmax", output);
        for(int i = 0 ; i < 8 ; i++)
        {
            if(output[i] == 1)
            {
                switch (i)
                {
                    case 0: return "Alkazar";
                    case 1: return "Alkof";
                    case 2: return "Cipladine";
                    case 3: return "Gubapentin";
                    case 4: return "Nicotex";
                    case 5: return "Nuroforte";
                    case 6: return "Nurokind";
                    case 7: return "Xykaa";
                    default: break;
                }
            }
        }

        return null;
    }

    public static Matrix getTransformationMatrix(
            final int srcWidth,
            final int srcHeight,
            final int dstWidth,
            final int dstHeight,
            final int applyRotation,
            final boolean maintainAspectRatio) {
        final Matrix matrix = new Matrix();

        if (applyRotation != 0) {
            if (applyRotation % 90 != 0) {

            }

            // Translate so center of image is at origin.
            matrix.postTranslate(-srcWidth / 2.0f, -srcHeight / 2.0f);

            // Rotate around origin.
            matrix.postRotate(applyRotation);
        }

        // Account for the already applied rotation, if any, and then determine how
        // much scaling is needed for each axis.
        final boolean transpose = (Math.abs(applyRotation) + 90) % 180 == 0;

        final int inWidth = transpose ? srcHeight : srcWidth;
        final int inHeight = transpose ? srcWidth : srcHeight;

        // Apply scaling if necessary.
        if (inWidth != dstWidth || inHeight != dstHeight) {
            final float scaleFactorX = dstWidth / (float) inWidth;
            final float scaleFactorY = dstHeight / (float) inHeight;

            if (maintainAspectRatio) {
                // Scale by minimum factor so that dst is filled completely while
                // maintaining the aspect ratio. Some image may fall off the edge.
                final float scaleFactor = Math.max(scaleFactorX, scaleFactorY);
                matrix.postScale(scaleFactor, scaleFactor);
            } else {
                // Scale exactly to fill dst from src.
                matrix.postScale(scaleFactorX, scaleFactorY);
            }
        }

        if (applyRotation != 0) {
            // Translate back from origin centered reference to destination frame.
            matrix.postTranslate(dstWidth / 2.0f, dstHeight / 2.0f);
        }

        return matrix;
    }
}
