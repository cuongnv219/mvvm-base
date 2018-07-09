package com.base.ui.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * ka
 * 21/02/2018
 */

public class OnFragmentEvent {

    public static class NextFragment {
        private Class<? extends Fragment> clazz;
        private Bundle bundle;
        private boolean addToBackStack;

        public NextFragment(Class<? extends Fragment> clazz, Bundle bundle, boolean addToBackStack) {
            this.clazz = clazz;
            this.bundle = bundle;
            this.addToBackStack = addToBackStack;
        }

        public NextFragment(Class<? extends Fragment> clazz, boolean addToBackStack) {
            this.clazz = clazz;
            this.addToBackStack = addToBackStack;
        }

        public Class<? extends Fragment> getClazz() {
            return clazz;
        }

        public Bundle getBundle() {
            return bundle;
        }

        public boolean isAddToBackStack() {
            return addToBackStack;
        }
    }
}
