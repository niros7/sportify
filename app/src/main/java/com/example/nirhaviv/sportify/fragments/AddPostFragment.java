package com.example.nirhaviv.sportify.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nirhaviv.sportify.R;
import com.example.nirhaviv.sportify.activities.AddPostActivity;
import com.example.nirhaviv.sportify.activities.ProfileActivity;
import com.example.nirhaviv.sportify.model.repositories.PostsRepository;
import com.example.nirhaviv.sportify.viewModels.PostViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileDescriptor;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class AddPostFragment extends Fragment {

    private final int RC_CHOOSE_PICTURE = 1;

    @BindView(R.id.post_text)
    EditText postText;

    @BindView(R.id.choose_picture)
    AppCompatButton choosePicture;

    @BindView(R.id.chosen_picture)
    ImageView chosenImage;
    private PostViewModel postViewModel;

    private Bitmap chosenPictureBitmap;

    public AddPostFragment() {
    }

    public static boolean isEmpty(EditText editText) {
        String input = editText.getText().toString().trim();
        return input.length() == 0;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        postViewModel = PostViewModel.instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.choose_picture)
    public void choosePictureClick(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, RC_CHOOSE_PICTURE);
    }

    @OnClick(R.id.add_post_btn)
    public void addPostClick(View view) {
        if (validateForm()) {
            postViewModel.savePost(postText.getText().toString(), chosenPictureBitmap);
            getActivity().finish();
//            Intent intent = new Intent(getContext(), ProfileActivity.class);
//            startActivity(intent);
        }
    }

    private boolean validateForm() {
        boolean isTextValid = false;
        boolean isPictureValid = false;

        if (isEmpty(postText)) {
            isTextValid = false;
            postText.setError("Post can't be empty");
        } else {
            isTextValid = true;
            postText.setError(null);
        }

        if (chosenImage.getVisibility() == View.GONE) {
            isPictureValid = false;
            Toast.makeText(getContext(), "Must choose an image", Toast.LENGTH_SHORT);
        } else {
            isPictureValid = true;
        }

        return isPictureValid && isTextValid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_CHOOSE_PICTURE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri imageUri = data.getData();

                try {
                    chosenImage.setBackground(getDrawableFromUri(imageUri));
                    choosePicture.setVisibility(View.GONE);
                    chosenImage.setVisibility(View.VISIBLE);
                    chosenImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            choosePictureClick(v);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private Drawable getDrawableFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        this.chosenPictureBitmap = image;
        parcelFileDescriptor.close();
        Drawable drawable = new BitmapDrawable(image);
        return drawable;
    }
}
