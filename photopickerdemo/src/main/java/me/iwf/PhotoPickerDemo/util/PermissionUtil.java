package me.iwf.PhotoPickerDemo.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import me.iwf.PhotoPickerDemo.BuildConfig;

/**
 * 检查运行时权限工具类
 * Created by johnwatson on 11/17/15.
 */
public class PermissionUtil {

  private static final String TAG = PermissionUtil.class.getSimpleName();

  /**
   * 如果需要说明请求权限的原因，将回调该接口。
   */
  public interface ShowRationaleCallback {
    /**
     * Call this method if we should show request permission rationale.
     */
    void needShow(final int requestCode);
  }

  /**
   * 检查权限是否已获取
   *
   * @param activity 请求权限要求必须获取 @link{android.app.Activity} 对象
   * @param permission 目标权限
   * @param requestCode 请求权限的请求码
   * @param callback 当需要给用户显示提示信息时回调函数
   * @param needRequestPermission 当需要给用户显示提示信息时，是否直接帮开发者请求权限。
   * @return true if granted
   */
  public static boolean checkPermissionGranted(final Activity activity, final String permission,
      final int requestCode, final ShowRationaleCallback callback,
      final boolean needRequestPermission) {
    boolean granted = true;

    // Here, thisActivity is the current activity
    if (ContextCompat.checkSelfPermission(activity, permission)
        != PackageManager.PERMISSION_GRANTED) {
      granted = false;

      boolean shouldShowRationale = false;

      // Should we show an explanation?
      if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
        // Show an expanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.

        if (callback != null) {
          callback.needShow(requestCode);
        }

        shouldShowRationale = true;
      } else {
        // No explanation needed, we can request the permission.
        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
        // app-defined int constant. The callback method gets the
        // result of the request.
      }

      if (BuildConfig.DEBUG) {
        Log.d(TAG, "shouldShowRequestPermissionRationale = " + shouldShowRationale);
      }

      if (!shouldShowRationale || needRequestPermission) {
        ActivityCompat.requestPermissions(activity, new String[] { permission }, requestCode);
      }
    }

    return granted;
  }

  /**
   * 检查权限请求返回的结果
   *
   * @param grantResults 请求结果
   * @return true if granted
   */
  public static boolean isGrantedForResult(int[] grantResults) {
    return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
  }
}