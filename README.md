# Android web3j example of ethereum wallet creation, Connecting ethereum client and transfering funds to another accouns


First, add the following dependencies to your project(app/build.gradle):

implementation 'org.web3j:core:3.3.1-android'

Then use WalletUtils to create a new ethereum wallet into your devie

        String walletPath = WalletUtils.generateFullNewWalletFile("yourownpassword",FilepathToStoretheWalletJson);


