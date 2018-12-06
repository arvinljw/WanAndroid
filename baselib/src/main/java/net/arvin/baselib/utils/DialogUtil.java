package net.arvin.baselib.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by arvinljw on 2018/11/2 15:15
 * Function：
 * Desc：
 */
public class DialogUtil {
    private Context context;

    private ProgressDialog progressDialog;

    public DialogUtil(Context context) {
        this.context = context;
    }

    public void showProgressDialog(String message) {
        if (context == null) {
            return;
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
        }
        progressDialog.setMessage(message != null ? message : "");
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
