package com.nasirbashak007.canteenui;

import android.graphics.Bitmap;

public interface OnProfilePictureUploadedListener {
    void onUploaded(FirebaseObject object, Bitmap image);
}
