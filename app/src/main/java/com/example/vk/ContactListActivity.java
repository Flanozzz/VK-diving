package com.example.vk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.vk.databinding.ActivityContactListBinding;
import android.content.Intent;

public class ContactListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        ActivityContactListBinding binding =
                ActivityContactListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}