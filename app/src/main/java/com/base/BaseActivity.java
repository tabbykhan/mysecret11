package com.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.R;
import com.utilities.DeviceScreenUtil;
import com.utilities.FullHeightLinearLayoutManager;

import java.util.Locale;


/**
 * Created by bitu on 15/8/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private FragmentManager fm;
    private Toast toast;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getSupportFragmentManager();
        beforeSetContentView();
        setContentView(getLayoutResourceId());
        initializeComponent();
    }

    public void beforeSetContentView () {

    }

    public abstract int getLayoutResourceId ();

    public int getFragmentContainerResourceId (BaseFragment baseFragment) {
        return -1;
    }

    public abstract void initializeComponent ();

    public void onCreateViewFragment (BaseFragment baseFragment) {
    }


    public void onDestroyViewFragment (BaseFragment baseFragment) {
    }


    @Override
    public void onBackPressed () {
        super.onBackPressed();
    }

    public int getColorFromStyle (int attrId) {
        TypedValue typedValue = new TypedValue();
        boolean found = getTheme().resolveAttribute(attrId, typedValue, true);
        if (found) {
            return typedValue.data;
        }
        return 0xff000000;
    }


    @Override
    public void onClick (View v) {

    }

    public FragmentManager getFm () {
        return fm;
    }


    public FragmentTransaction getNewFragmentTransaction () {
        return fm.beginTransaction();
    }

    public BaseFragment getLatestFragment (int resId) {
        Fragment fragment = fm.findFragmentById(resId);
        if (fragment != null && fragment instanceof BaseFragment) {
            return ((BaseFragment) fragment);
        }

        return null;
    }

    public BaseFragment getFragmentByTag (String tag) {
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment != null && fragment instanceof BaseFragment) {
            return ((BaseFragment) fragment);
        }
        return null;
    }

    public int getBackStackEntryCount () {
        return fm.getBackStackEntryCount();
    }

    public void clearFragmentBackStack () {
        clearFragmentBackStack(0);
    }


    public void clearFragmentBackStack (int pos) {
        if (fm.getBackStackEntryCount() > pos) {
            try {
                fm.popBackStack(fm.getBackStackEntryAt(pos).getId(),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }


    public void changeFragment (BaseFragment f, boolean addBackStack,
                                boolean clearAll, int pos, int enterAnim, int exitAnim,
                                int enterAnimBackStack, int exitAnimBackStack, boolean isReplace) {
        if (clearAll) {
            clearFragmentBackStack(pos);
        }
        if (getFragmentContainerResourceId(f) == -1) return;
        if (f != null) {
            try {
                FragmentTransaction ft = getNewFragmentTransaction();
                ft.setCustomAnimations(enterAnim, exitAnim, enterAnimBackStack, exitAnimBackStack);
                if (isReplace) {
                    ft.replace(getFragmentContainerResourceId(f), f, f.getClass().getSimpleName());
                } else {
                    ft.add(getFragmentContainerResourceId(f), f, f.getClass().getSimpleName());
                }
                if (addBackStack) {
                    ft.addToBackStack(f.getClass().getSimpleName());
                }
                ft.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void changeFragment (BaseFragment f, boolean addBackStack,
                                boolean clearAll, int pos, boolean isReplace) {
        if (clearAll) {
            clearFragmentBackStack(pos);
        }
        if (getFragmentContainerResourceId(f) == -1) return;
        if (f != null) {
            try {
                FragmentTransaction ft = getNewFragmentTransaction();
                if (isReplace) {
                    ft.replace(getFragmentContainerResourceId(f), f, f.getClass().getSimpleName());
                } else {
                    ft.add(getFragmentContainerResourceId(f), f, f.getClass().getSimpleName());
                }
                if (addBackStack) {
                    ft.addToBackStack(f.getClass().getSimpleName());
                }
                ft.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public boolean isValidString (String data) {
        return data != null && !data.trim().isEmpty();
    }

    public void printLog (String tag, String msg) {
        if (BaseApplication.instance.isDebugBuild() && msg != null) {
            if (tag == null || tag.trim().isEmpty()) {
                tag = getClass().getSimpleName();
            }
            Log.e(tag, msg);
        }
    }

    public synchronized void hideKeyboard () {
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        hideKeyboard(view);
    }

    public synchronized void hideKeyboard (View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null && view.getWindowToken() != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public String getValidDecimalFormat (String value) {
        if (!isValidString(value)) {
            return "0.00";
        }
        double netValue = Double.parseDouble(value);
        return getValidDecimalFormat(netValue);
    }

    public String getValidDecimalFormat (double value) {
        return String.format(Locale.ENGLISH, "%.2f", value);
    }

    public LinearLayoutManager getVerticalLinearLayoutManager () {
        return new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
    }

    public FullHeightLinearLayoutManager getFullHeightLinearLayoutManager () {
        return new FullHeightLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }
    public LinearLayoutManager getHorizentalLinearLayoutManager () {
        return new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
    }

    public LinearLayoutManager getGridLayoutManager (int column) {
        return new GridLayoutManager(this, column);
    }


    public void showCustomToast (String message) {

        if (!isFinishing()) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast_layout, null);
            TextView toastTextView = layout.findViewById(R.id.tv_toast);
            toastTextView.setText(message);
            if (toast != null)
                toast.cancel();
            toast = new Toast(this);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0,
                    DeviceScreenUtil.getInstance().convertDpToPixel(70));
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }

    public void updateViewVisibitity (View view, int visibility) {
        if (view == null) return;
        if (view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    public String bundle2string (Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }
}
