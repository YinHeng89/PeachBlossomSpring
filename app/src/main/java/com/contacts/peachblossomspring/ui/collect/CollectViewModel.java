package com.contacts.peachblossomspring.ui.collect;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CollectViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CollectViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("收藏夹功能待开发");
    }

    public LiveData<String> getText() {
        return mText;
    }
}