package com.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;


/**
 * Created by bitu on 15/8/17.
 */

public abstract class BaseFragment extends Fragment
        implements View.OnClickListener {

    FragmentManager childFm;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        childFm = getChildFragmentManager();
        setupFragmentViewByResource(inflater, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeComponent();

    }

    public void setupFragmentViewByResource(LayoutInflater inflater, @Nullable ViewGroup container) {

        view = inflater.inflate(getLayoutResourceId(), container, false);
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    public boolean isFinishing() {
        return getActivity() == null || getActivity().isFinishing();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public boolean handleOnBackPress() {
        if (childFm.getBackStackEntryCount() > 0) {
            childFm.popBackStackImmediate();
            return true;
        }
        return false;
    }

    public abstract int getLayoutResourceId();

    public abstract void initializeComponent();


    public int getFragmentContainerResourceId(BaseFragment baseFragment) {
        return -1;
    }


    public void viewCreateFromBackStack() {

    }

    public FragmentManager getChildFm() {
        return childFm;
    }

    public FragmentTransaction getNewChildFragmentTransaction() {
        return childFm.beginTransaction();
    }

    public BaseFragment getLatestFragment(int resId) {

        Fragment fragment = childFm.findFragmentById(resId);
        if (fragment != null && fragment instanceof BaseFragment) {
            return ((BaseFragment) fragment);
        }

        return null;
    }

    public BaseFragment getFragmentByTag(String tag) {
        Fragment fragment = childFm.findFragmentByTag(tag);
        if (fragment != null && fragment instanceof BaseFragment) {
            return ((BaseFragment) fragment);
        }
        return null;
    }

    public int getBackStackEntryCount() {
        return childFm.getBackStackEntryCount();
    }

    public void clearFragmentBackStack() {
        clearFragmentBackStack(0);
    }

    public void clearFragmentBackStack(int pos) {
        if (childFm.getBackStackEntryCount() > pos) {
            try {
                childFm.popBackStack(childFm.getBackStackEntryAt(pos).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } catch (IllegalStateException e) {
            }
        }
    }


    public void changeFragment(BaseFragment f, boolean addBackStack,
                               boolean clearAll, int pos, boolean isReplace) {

        if (clearAll) {
            clearFragmentBackStack(pos);
        }
        if (getFragmentContainerResourceId(f) == -1) return;
        if (f != null) {
            try {
                FragmentTransaction ft = getNewChildFragmentTransaction();
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


    public void changeFragment(BaseFragment f, boolean addBackStack,
                               boolean clearAll, int pos, int enterAnim, int exitAnim,
                               int enterAnimBackStack, int exitAnimBackStack, boolean isReplace) {

        if (clearAll) {
            clearFragmentBackStack(pos);
        }
        if (getFragmentContainerResourceId(f) == -1) return;
        if (f != null) {
            try {
                FragmentTransaction ft = getNewChildFragmentTransaction();
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


    public boolean isValidActivity() {
        return getActivity() != null;
    }

    public boolean isValidString(String data) {
        if (getActivity() == null) return false;
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).isValidString(data);
        }
        return false;
    }


    public void printLog(String tag, String msg) {
        if (getActivity() == null) return;
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).printLog(tag, msg);
        }
    }

    public synchronized void hideKeyboard() {
        if (getActivity() == null) return;
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideKeyboard();
        }
    }

    public synchronized void hideKeyboard(View view) {
        if (getActivity() == null) return;
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).hideKeyboard(view);
        }
    }

    public String getValidDecimalFormat(String value) {
        if (getActivity() == null) return "";
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getValidDecimalFormat(value);
        }
        return "";
    }

    public String getValidDecimalFormat(double value) {
        if (getActivity() == null) return "";
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getValidDecimalFormat(value);
        }
        return "";
    }


    public LinearLayoutManager getVerticalLinearLayoutManager() {
        if (getActivity() == null) return null;
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getVerticalLinearLayoutManager();
        }
        return null;

    }

    public LinearLayoutManager getFullHeightLinearLayoutManager() {
        if (getActivity() == null) return null;
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getFullHeightLinearLayoutManager();
        }
        return null;
    }

    public LinearLayoutManager getGridLayoutManager(int column) {
        if (getActivity() == null) return null;
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getGridLayoutManager(column);
        }
        return null;
    }

    public void showCustomToast(String message) {
        if (getActivity() == null) return;
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showCustomToast(message);
        }
    }

    public void updateViewVisibitity(View view, int visibility) {
        if (getActivity() == null) return;
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).updateViewVisibitity(view, visibility);
        }
    }


}
