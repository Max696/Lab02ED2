package com.example.rog.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LZW extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_SOTARGE= 1000;
    private static final int RQS_OPEN_DOCUMENT_TREE = 2;
    private static final int READ_REQUEST_CODE= 42;
    Button save,Encode,Decode;
    EditText levels;
    Switch enDe;
    String totalName ="";
    String toPrint ="";
    SDES sdes= new SDES();
    String s ="";
    String cifrado ="";
    String decifrado ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)

        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_SOTARGE);

        }
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)

        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);

        }

        save = (Button)findViewById(R.id.save);
        Encode= (Button)findViewById(R.id.encode);
        levels = (EditText) findViewById(R.id.niveles);
        enDe =(Switch)findViewById(R.id.enDecode);
        enDe.setShowText(true);

        Encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((Intent.ACTION_OPEN_DOCUMENT_TREE));
                startActivityForResult(intent,RQS_OPEN_DOCUMENT_TREE);

            }
        });




    }
    private String  readText(Uri uri) throws IOException {

        InputStream input = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder stringBuilder = new StringBuilder();
        String Line;
        while ((Line = reader.readLine()) != null) {
            stringBuilder.append(Line);
        }
        input.close();
        reader.close();
        return stringBuilder.toString();
    }
    private void performFileSearch()
    {
        Intent inten = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        inten.addCategory(Intent.CATEGORY_OPENABLE);
        inten.setType("*/*");

        startActivityForResult(inten,READ_REQUEST_CODE);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            Uri uri = data.getData();
            totalName = uri.getLastPathSegment();
            String [] qw =totalName.split("/");
            toPrint += qw[qw.length-1]+"\n";
            int niveles = 0;
            niveles = Integer.valueOf(levels.getText().toString());


            try {


                if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                    String sUri = "";
                    sUri = readText(uri);
                    s = sUri;

                    if (enDe.isChecked()) {


                        cifrado = SDES.encrypt(s, niveles);

                    }
                    else {


                        decifrado = SDES.decrypt(s,niveles);
                    }

                }


                if (resultCode == Activity.RESULT_OK && requestCode == RQS_OPEN_DOCUMENT_TREE&&levels!=null  ) {

                    Uri uriTree = data.getData();
                    String path = "";
                    DocumentFile documentFile = DocumentFile.fromTreeUri(this, uriTree);
                    uriTree = documentFile.getUri();
                    Uri docUri = DocumentsContract.buildDocumentUriUsingTree(uriTree,
                            DocumentsContract.getTreeDocumentId(uriTree));
                    path = getPath(this,docUri);




                    if (enDe.isChecked()) {

                        saveTextAsFile("Cifrado",cifrado, path);
                        Toast.makeText(this, "Guardado en:"+path, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        saveTextAsFile("Decifrado",decifrado, path);
                        Toast.makeText(this, "Guardado en:"+path, Toast.LENGTH_SHORT).show();
                    }

                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
    private void saveTextAsFile(String filename, String content ,String path) throws FileNotFoundException {
        boolean hol = enDe.isChecked();
        String fileName="";
        if(!hol)
        {  fileName = filename+".txt";
            toPrint+= fileName+"\n"+path;
        }
        else
        {fileName = filename+".scif";
            toPrint+= fileName+"\n"+path;
        }


        File file = new File(path,fileName);

        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
            Toast.makeText(this, "guardado",Toast.LENGTH_SHORT).show();


        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Archivo no encontrado",Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar",Toast.LENGTH_SHORT).show();
        }


    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PERMISSION_REQUEST_SOTARGE)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Permission not granted",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    public class MinMaxFilter implements InputFilter {

        private int mIntMin, mIntMax;

        public MinMaxFilter(int minValue, int maxValue) {
            this.mIntMin = minValue;
            this.mIntMax = maxValue;
        }

        public MinMaxFilter(String minValue, String maxValue) {
            this.mIntMin = Integer.parseInt(minValue);
            this.mIntMax = Integer.parseInt(maxValue);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(mIntMin, mIntMax, input))
                    return null;
            } catch (NumberFormatException nfe) { }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}

