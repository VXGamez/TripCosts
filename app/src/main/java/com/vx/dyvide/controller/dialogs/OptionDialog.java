package com.vx.dyvide.controller.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vx.dyvide.R;
import com.vx.dyvide.controller.callbacks.DialogCallback;

public class OptionDialog {

    private static OptionDialog sManager;
    private Object mutex = new Object();

    private Context mContext;
    private Dialog mDialog;

    private TextView tvTitle;
    private TextView tvSubtitle;
    private ImageView ivIcon;
    private Button btnAccept;
    private Button btnDelete;
    private DialogCallback dialogCallback;

    public static OptionDialog getInstance(Context context) {
        if (sManager == null) {
            sManager = new OptionDialog(context);
        }
        return sManager;
    }

    public OptionDialog(Context context) {
        mContext = context;
        mDialog = new Dialog(mContext);
        dialogCallback = (DialogCallback) context;
    }


    public void cancelDialog() {
        mDialog.cancel();
    }

    public void showConfirmationDialog(String message) {
        mDialog.setContentView(R.layout.dialog_option);
        mDialog.setCanceledOnTouchOutside(false);

        tvTitle = (TextView) mDialog.findViewById(R.id.dialog_title);
        tvSubtitle = (TextView) mDialog.findViewById(R.id.dialog_error_subtitle);
        tvTitle.setText("Careful!");
        tvSubtitle.setText(message);
        btnAccept = (Button) mDialog.findViewById(R.id.dialog_edit);
        btnAccept.setText("Confirm");
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallback.onAccept();
            }
        });

        btnDelete = (Button) mDialog.findViewById(R.id.dialog_delete);
        btnDelete.setText("Cancel");
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallback.onCancel();
            }
        });
        mDialog.show();
    }

}