package com.example.hpand.mysearchapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final int MY_PERMISSION_REQUEST =1;

    ArrayList<String> arrayList;

    ListView listView;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE )){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new  String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new  String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);

            }
        } else{

            doStuff();
        }
    }

    public void doStuff(){
        listView = (ListView) findViewById(R.id.listView);
        arrayList= new ArrayList<>();
        getImage();
        adapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    public  void  getImage(){
        ContentResolver contentResolver = getContentResolver();
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor imageCursor= contentResolver.query(imageUri,null,null,null,null);

        if(imageCursor != null && imageCursor.moveToFirst()){
            int imageTitle = imageCursor.getColumnIndex(MediaStore.Images.Media.TITLE);
            //int imageTitle = imageCursor.getColumnIndex(MediaStore.Images.Media.);

            do{
                String  currentTitle= imageCursor.getString(imageTitle);
                arrayList.add(currentTitle);
            }while (imageCursor.moveToNext());
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
       switch (requestCode){
           case  MY_PERMISSION_REQUEST: {
               if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                   if (ContextCompat.checkSelfPermission(MainActivity.this,
                           Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                       Toast.makeText(this,"PERMISSION GRANTED", Toast.LENGTH_SHORT).show();

                       doStuff();

                   }
               }else {
                   Toast.makeText(this,"No permission granted",Toast.LENGTH_SHORT ).show();
                   finish();
               }
               return;

           }
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> templist = new ArrayList<>();

                for(String temp : arrayList){
                    if (temp.toLowerCase().contains(newText.toLowerCase())){
                        templist.add(temp);
                    }
                }
                ArrayAdapter<String> adapter1=new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_list_item_1,templist);
                listView.setAdapter(adapter1);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }


        });


        return super.onCreateOptionsMenu(menu);
    }
}
