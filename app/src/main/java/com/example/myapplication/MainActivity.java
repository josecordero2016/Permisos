package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    ImageView ivMostrarFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> permisos = new ArrayList<String>();
        permisos.add(Manifest.permission.CAMERA);
        permisos.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permisos.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permisos.add(Manifest.permission.WRITE_CALENDAR);
        getPermission(permisos);


        ivMostrarFoto = (ImageView)this.findViewById(R.id.ivMostrarFoto);
        Button btnCamara = (Button) this.findViewById(R.id.btnCamara);
        btnCamara.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    public void getPermission(ArrayList<String> permisosSolicitados){

        ArrayList<String> listPermisosNOAprob = getPermisosNoAprobados(permisosSolicitados);
        if (listPermisosNOAprob.size()>0)
             if (Build.VERSION.SDK_INT >= 23)
                  requestPermissions(listPermisosNOAprob.toArray(new String[listPermisosNOAprob.size()]), 1);

    }


    public ArrayList<String> getPermisosNoAprobados(ArrayList<String>  listaPermisos) {
        ArrayList<String> list = new ArrayList<String>();
        for(String permiso: listaPermisos) {
            if (Build.VERSION.SDK_INT >= 23)
                 if(checkSelfPermission(permiso) != PackageManager.PERMISSION_GRANTED)
                     list.add(permiso);

        }
        return list;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String s="";
        if(requestCode==1)    {
            for(int i =0; i<permissions.length;i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    s=s + "OK " + permissions[i] + "\n";
                else
                    s=s + "NO  " + permissions[i] + "\n";
            }
            Toast.makeText(this.getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }

    public void MostrarDescargas(View view){

        Intent intent = new Intent();
        intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(intent);

    }
    public void BajarDoc(View view){
        String url = "https://www.blyx.com/public/wireless/redesInalambricas.pdf";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("PDF");
        request.setTitle("Pdf");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "filedownload.pdf");
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            manager.enqueue(request);
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(),"Error: "  + e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int codigoPeticion, int codigoResultado, Intent data) {
        super.onActivityResult(codigoPeticion, codigoResultado, data);
        if (codigoPeticion == CAMERA_REQUEST && codigoResultado == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ivMostrarFoto.setImageBitmap(photo);
        }
    }


}