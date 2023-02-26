package com.example.vk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;

import com.example.vk.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    int offIconColor;
    int onIconColor;
    Drawable cameraOnIcon;
    Drawable cameraOffIcon;
    Drawable micOffIcon;
    Drawable micOnIcon;
    Drawable handDefault;
    final int animationDuration = 500;
    boolean isCameraOn = true;
    boolean isMicroOn = true;
    boolean isAnimationGoing = false;
    boolean isMembersChanged = false;

    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        offIconColor = R.color.white;
        onIconColor = R.color.icon_def;

        cameraOffIcon = getResources().getDrawable(R.drawable.round_videocam_off_20, null);
        cameraOnIcon = getResources().getDrawable(R.drawable.round_videocam_on_20, null);
        micOffIcon = getResources().getDrawable(R.drawable.round_mic_off_20, null);
        micOnIcon = getResources().getDrawable(R.drawable.round_mic_on_20, null);
        handDefault = getResources().getDrawable(R.drawable.round_front_hand_20, null);
        micOffIcon.setTint(ContextCompat.getColor(this, R.color.black));
        cameraOffIcon.setTint(ContextCompat.getColor(this, R.color.black));

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.camera.setOnClickListener(v -> clickOnCameraAction());
        binding.micro.setOnClickListener(v -> clickOnMicroAction());
        binding.hand.setOnClickListener(v -> helloMessage());
        binding.group.setOnClickListener(v -> goToContactList());
        binding.phone.setOnClickListener(v -> this.finishAffinity());
        binding.message.setOnClickListener(v -> goToMessage());
        binding.view.setOnClickListener(v -> clickOnViewChangeIconAction());
    }

    private void clickOnViewChangeIconAction(){
        if(!isAnimationGoing){
            changeViewWithAnimation();
        }
    }

    private void goToMessage(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
        startActivity(intent);
    }

    private void clickOnCameraAction(){
        changeIcon(binding.cameraIcon, cameraOffIcon, cameraOnIcon, binding.camera, isCameraOn);
        isCameraOn = !isCameraOn;
    }

    private void clickOnMicroAction(){
        changeIcon(binding.microIcon, micOffIcon, micOnIcon, binding.micro, isMicroOn);
        if(isMicroOn){
            binding.member1Name.setCompoundDrawablesWithIntrinsicBounds
                    (null, null, ContextCompat.getDrawable
                            (this,R.drawable.round_mic_off_20), null);
        }
        else{
            binding.member1Name.setCompoundDrawablesWithIntrinsicBounds
                    (null, null, null, null);
        }
        isMicroOn = !isMicroOn;
    }

    private void helloMessage(){
        new AlertDialog.Builder(this)
                .setMessage(R.string.Hi_massage)
                .setPositiveButton(R.string.close, (dialog, which) -> {})
                .show();
    }

    private void goToContactList(){
        Intent intent = new Intent(this, ContactListActivity.class);
        startActivity(intent);
    }

    private void moveView(float start, float end, View view){
        view.animate()
                .setDuration(animationDuration)
                .y(end)
                .withEndAction(() -> {
                    view.setY(start);
                });
    }

    private void changeViewWithAnimation(){
        isAnimationGoing = true;
        float a = binding.member2Icon.getY();
        moveView(binding.member1.getY(), binding.member2.getY(), binding.member1);
        moveView(binding.member1Name.getY(), binding.member2Name.getY(), binding.member1Name);
        moveView(binding.member1Icon.getY(), binding.member2Icon.getY(), binding.member1Icon);
        binding.member1.bringToFront();
        binding.member1Name.bringToFront();
        binding.member1Icon.bringToFront();
        moveView(binding.member2.getY(), binding.member1.getY(), binding.member2);
        moveView(binding.member2Name.getY(), binding.member1Name.getY(), binding.member2Name);
        //moveView(binding.member2Icon.getY(), binding.member1Icon.getY(), binding.member2Icon);
        binding.member2Icon.animate()
                .setDuration(animationDuration)
                .y(binding.member1Icon.getY())
                .withEndAction(() -> {
                    binding.member2Icon.setY(a);
                    changeView();
                    isAnimationGoing = false;
                });
    }

    private void changeView(){
        ConstraintLayout.LayoutParams member2LayoutParams =
                (ConstraintLayout.LayoutParams)binding.member2.getLayoutParams();
        ConstraintLayout.LayoutParams member1LayoutParams =
                (ConstraintLayout.LayoutParams)binding.member1.getLayoutParams();
        ConstraintLayout.LayoutParams marginPanelLayoutParams =
                (ConstraintLayout.LayoutParams)binding.marginPanel.getLayoutParams();

        if(isMembersChanged){
            //Member2
            member2LayoutParams.topToBottom = binding.member1.getId();
            member2LayoutParams.bottomToTop = binding.marginPanel.getId();
            member2LayoutParams.topMargin =
                    (int)getResources().getDimension(R.dimen.memberPanelsSpaceBetween);

            //Member1
            member1LayoutParams.topToBottom = binding.group.getId();
            member1LayoutParams.bottomToTop = binding.member2.getId();
            member1LayoutParams.topMargin =
                    (int)getResources().getDimension(R.dimen.member1_top_margin);

            //MarginPanel
            marginPanelLayoutParams.topToBottom = binding.member2.getId();
        }
        else{
            //Member2
            member2LayoutParams.topToBottom = binding.group.getId();
            member2LayoutParams.bottomToTop = binding.member1.getId();
            member2LayoutParams.topMargin =
                    (int)getResources().getDimension(R.dimen.member1_top_margin);

            //Member1
            member1LayoutParams.topToBottom = binding.member2.getId();
            member1LayoutParams.bottomToTop = binding.marginPanel.getId();
            member1LayoutParams.topMargin =
                    (int)getResources().getDimension(R.dimen.memberPanelsSpaceBetween);

            //MarginPanel
            marginPanelLayoutParams.topToBottom = binding.member1.getId();
        }

        isMembersChanged = !isMembersChanged;
        binding.member2.setLayoutParams(member2LayoutParams);
        binding.member1.setLayoutParams(member1LayoutParams);
        binding.marginPanel.setLayoutParams(marginPanelLayoutParams);
    }

    private void changeIcon(View viewIcon,
                            Drawable offIcon, Drawable onIcon, View backgroundView,
                            boolean isOn){
        Drawable newBackground;
        int backgroundColor;
        if(isOn){
            newBackground = offIcon;
            backgroundColor = offIconColor;

        }
        else{
            newBackground = onIcon;
            backgroundColor = onIconColor;
        }
        viewIcon.setBackground(newBackground);
        backgroundView.getBackground()
                .setTint(ContextCompat.getColor(this, backgroundColor));
    }
}