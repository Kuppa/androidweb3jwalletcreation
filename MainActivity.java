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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.concurrent.ExecutionException;

/**
 * @Author: Sekhar Kuppa
 * This class contains a simple ethereum wallet creation using web3j in your own device/emulator
 */

public class MainActivity extends AppCompatActivity {

    private File walletPathFile;
    private static final Logger log = LoggerFactory.getLogger(MainActivity.class);
    private Web3j web3;
    private Credentials credentials;

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

    public void connectEth(View view) throws ExecutionException, InterruptedException {
        web3 = Web3jFactory.build(new HttpService("https://rinkeby.infura.io/t09WQ2e4ZkxRIzkFeyDH"));  // defaults to http://localhost:8545/
        Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
        String clientVersion = web3ClientVersion.getWeb3ClientVersion();
        log.info("Connected to Ethereum client version: "
                + clientVersion);
    }

    public void loadCredentials(View view) throws IOException, CipherException {
        credentials =
                WalletUtils.loadCredentials(
                        "Your wallet password",
                        "Your wallet stored path");
        log.info("Credentials loaded Address is::: "+ credentials.getAddress());
    }

    public void transferFunds(View view) {
        TransactionReceipt transactionReceipt = null;
        try {
            log.info("Sending 1 Wei ("
                    + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");

            transactionReceipt = Transfer.sendFunds(
                    web3, credentials, "0x19e03255f667bdfd50a32722df860b1eeaf4d635",
                    BigDecimal.valueOf(1.0), Convert.Unit.ETHER)
                    .send();
            log.info("Transaction complete, view it at https://rinkeby.etherscan.io/tx/"
                    + transactionReceipt.getBlockHash());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
