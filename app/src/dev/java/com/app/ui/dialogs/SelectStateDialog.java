package com.app.ui.dialogs;

import android.app.Dialog;
import android.view.WindowManager;

import com.app.model.StateModel;
import com.app.ui.dialogs.selection.DataDialog;
import com.utilities.DeviceScreenUtil;

public class SelectStateDialog extends DataDialog<StateModel> {

    public SelectStateDialog() {
        setTitle("SELECT STATE");
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
        wlmp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlmp.width = DeviceScreenUtil.getInstance().getWidth(0.90f);
        wlmp.dimAmount = 0.8f;
    }
}
