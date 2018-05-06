package com.kuppa.web3sample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.WalletUtils;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @Author: Sekhar Kuppa
 * This class contains a simple ethereum wallet creation using web3j in your own device/emulator
 */

public class MainActivity extends AppCompatActivity {

    private File walletPathFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        synchronized (this) {
            checkPermissions();
        }

        synchronized (this) {
            String path = Environment.getDataDirectory().getAbsolutePath().toString() + "/storage/emulated/0/appFolder";
            File mFolder = new File(path);
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }
            walletPathFile = new File("/sdcard/EthWallet/");
            walletPathFile.mkdirs();
        }
    }

    private void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 101);
            }
        }
    }

    public void createWallet(View view) throws CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        String walletPath = WalletUtils.generateFullNewWalletFile("yourownpassword",walletPathFile);
        Toast.makeText(this, "Wallet created successfully.", Toast.LENGTH_SHORT).show();
    }
}
