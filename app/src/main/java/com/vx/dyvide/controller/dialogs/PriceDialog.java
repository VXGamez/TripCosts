package com.vx.dyvide.controller.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vx.dyvide.R;

public class PriceDialog {

    private static PriceDialog sManager;
    private Object mutex = new Object();

    private Context mContext;
    private Dialog mDialog;

    private TextView tvTitle;
    private TextView tvSubtitle;
    private ImageView ivIcon;
    private ImageView ivLike;
    private ImageView ivFollow;
    private Button btnAccept;

    public static PriceDialog getInstance(Context context) {
        if (sManager == null) {
            sManager = new PriceDialog(context);
        }
        return sManager;
    }

    public PriceDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext);
    }

    public void showInform(String title, String message) {
        mDialog.setContentView(R.layout.dialog_error);
        mDialog.setCanceledOnTouchOutside(false);

        tvTitle = (TextView) mDialog.findViewById(R.id.dialog_error_title);
        tvTitle.setText(title);
        tvSubtitle = (TextView) mDialog.findViewById(R.id.dialog_error_subtitle);
        tvSubtitle.setText(message);

        btnAccept = (Button) mDialog.findViewById(R.id.dialog_error_button);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        mDialog.show();
    }
}
