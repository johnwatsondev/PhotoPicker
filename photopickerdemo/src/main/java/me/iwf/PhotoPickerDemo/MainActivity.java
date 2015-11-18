package me.iwf.PhotoPickerDemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import me.iwf.PhotoPickerDemo.util.PermissionUtil;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

public class MainActivity extends AppCompatActivity {

  private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE_REQUEST_CODE_1 = 1;
  private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE_REQUEST_CODE_2 = 2;
  private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE_REQUEST_CODE_3 = 3;
  private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE_REQUEST_CODE_4 = 4;

  RecyclerView recyclerView;
  PhotoAdapter photoAdapter;

  ArrayList<String> selectedPhotos = new ArrayList<>();

  public final static int REQUEST_CODE = 1;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    photoAdapter = new PhotoAdapter(this, selectedPhotos);

    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
    recyclerView.setAdapter(photoAdapter);

    findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (PermissionUtil.checkPermissionGranted(MainActivity.this,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            MY_PERMISSIONS_READ_EXTERNAL_STORAGE_REQUEST_CODE_1,
            new PermissionUtil.ShowRationaleCallback() {

              @Override public void needShow(int requestCode) {
                showToast("1 我们需要读取您的照片，以便上传！请到设置中打开读取本机存储权限！");
              }
            }, true)) {
          launchPickPhoto();
        }
      }
    });

    findViewById(R.id.button_no_camera).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (PermissionUtil.checkPermissionGranted(MainActivity.this,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            MY_PERMISSIONS_READ_EXTERNAL_STORAGE_REQUEST_CODE_2,
            new PermissionUtil.ShowRationaleCallback() {

              @Override public void needShow(int requestCode) {
                showToast("2 我们需要读取您的照片，以便上传！请到设置中打开读取本机存储权限！");
              }
            }, false)) {
          launchPickPhotoWithoutCamera();
        }
      }
    });

    findViewById(R.id.button_one_photo).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (PermissionUtil.checkPermissionGranted(MainActivity.this,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            MY_PERMISSIONS_READ_EXTERNAL_STORAGE_REQUEST_CODE_3,
            new PermissionUtil.ShowRationaleCallback() {

              @Override public void needShow(int requestCode) {
                showToast("3 我们需要读取您的照片，以便上传！请到设置中打开读取本机存储权限！");
              }
            }, true)) {
          launchPickSinglePhoto();
        }
      }
    });

    findViewById(R.id.button_photo_gif).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (PermissionUtil.checkPermissionGranted(MainActivity.this,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            MY_PERMISSIONS_READ_EXTERNAL_STORAGE_REQUEST_CODE_4,
            new PermissionUtil.ShowRationaleCallback() {

              @Override public void needShow(int requestCode) {
                showToast("4 我们需要读取您的照片，以便上传！请到设置中打开读取本机存储权限！");
              }
            }, false)) {
          launchPickPhotosShowGif();
        }
      }
    });
  }

  private void launchPickPhoto() {
    PhotoPickerIntent intent = new PhotoPickerIntent(MainActivity.this);
    intent.setPhotoCount(9);
    startActivityForResult(intent, REQUEST_CODE);
  }

  private void launchPickPhotoWithoutCamera() {
    PhotoPickerIntent intent = new PhotoPickerIntent(MainActivity.this);
    intent.setPhotoCount(7);
    intent.setShowCamera(false);
    startActivityForResult(intent, REQUEST_CODE);
  }

  private void launchPickSinglePhoto() {
    PhotoPickerIntent intent = new PhotoPickerIntent(MainActivity.this);
    intent.setPhotoCount(1);
    intent.setShowCamera(true);
    startActivityForResult(intent, REQUEST_CODE);
  }

  private void launchPickPhotosShowGif() {
    PhotoPickerIntent intent = new PhotoPickerIntent(MainActivity.this);
    intent.setPhotoCount(4);
    intent.setShowCamera(true);
    intent.setShowGif(true);
    startActivityForResult(intent, REQUEST_CODE);
  }

  public void previewPhoto(Intent intent) {
    startActivityForResult(intent, REQUEST_CODE);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    List<String> photos = null;
    if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
      if (data != null) {
        photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
      }
      selectedPhotos.clear();

      if (photos != null) {

        selectedPhotos.addAll(photos);
      }
      photoAdapter.notifyDataSetChanged();
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    switch (requestCode) {
      case MY_PERMISSIONS_READ_EXTERNAL_STORAGE_REQUEST_CODE_1: {
        // If request is cancelled, the result arrays are empty.
        if (PermissionUtil.isGrantedForResult(grantResults)) {
          // permission was granted, yay! Do the
          // contacts-related task you need to do.
          launchPickPhoto();
        } else {
          // permission denied, boo! Disable the
          // functionality that depends on this permission.
          showToast("您禁止了读取本机图片的权限，无法获取照片！");
        }
        break;
      }

      case MY_PERMISSIONS_READ_EXTERNAL_STORAGE_REQUEST_CODE_2: {
        if (PermissionUtil.isGrantedForResult(grantResults)) {
          launchPickPhotoWithoutCamera();
        } else {
          showToast("您禁止了读取本机图片的权限，无法获取照片！");
        }
        break;
      }

      case MY_PERMISSIONS_READ_EXTERNAL_STORAGE_REQUEST_CODE_3: {
        if (PermissionUtil.isGrantedForResult(grantResults)) {
          launchPickSinglePhoto();
        } else {
          showToast("您禁止了读取本机图片的权限，无法获取照片！");
        }
        break;
      }

      case MY_PERMISSIONS_READ_EXTERNAL_STORAGE_REQUEST_CODE_4: {
        if (PermissionUtil.isGrantedForResult(grantResults)) {
          launchPickPhotosShowGif();
        } else {
          showToast("您禁止了读取本机图片的权限，无法获取照片！");
        }
        break;
      }

      // other 'case' lines to check for other
      // permissions this app might request
    }
  }

  private void showToast(String text) {
    if (!TextUtils.isEmpty(text)) {
      Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
  }
}