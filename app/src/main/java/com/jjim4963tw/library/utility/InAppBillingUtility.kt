package com.jjim4963tw.library.utility

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import com.android.billingclient.api.*
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class InAppBillingUtility(val context: Context) {
    companion object {
        val TAG: String? = InAppBillingUtility::class.simpleName
        const val BASE_64_ENCODED_PUBLIC_KEY = ""
    }

    enum class PurchaseType {
        INAPP, SUBS
    }

    interface IABHelperListener {
        fun onProductListResponse(productList: ArrayList<ProductDetails>)
        fun onPurchaseHistoryResponse(purchaseItems: List<Purchase>?)
        fun onPurchaseVerify(purchaseList: List<Purchase>)
        fun onPurchaseCompleted(result: BillingResult, others: Any?)
        fun onPurchaseError(status: Int)
    }

    private lateinit var billingClient: BillingClient
    private lateinit var iabHelperListener: IABHelperListener
    lateinit var productIDList: MutableList<Any>


    /**
     * To instantiate the object
     * @param context           It will be used to get an application context to bind to the in-app billing service.
     * @param iabHelperListener Your listener to get the response for your query.
     * @param productIDList     product id, key is BillingClient.SkuType.INAPP / SUBS
     */
    constructor(context: Context, iabHelperListener: IABHelperListener, productIDList: MutableList<Any>) : this(context) {
        this.iabHelperListener = iabHelperListener
        this.productIDList = productIDList

        this.billingClient = BillingClient.newBuilder(context).setListener(purchasesUpdatedListener)
            .enablePendingPurchases().build()
    }

    /**
     * To establish the connection with play library
     * It will be used to notify that setup is complete and the billing
     * client is ready. You can query whatever you want.
     */
    fun startConnection() {
        if (!billingClient.isReady) {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    val connectionStatus = billingResult.responseCode

                    AccessLogUtility.showInfoMessage(false, TAG, "[INFO | ${TAG}.startConnection] connection google play billing service status : $connectionStatus", null)

                    if (connectionStatus == BillingClient.BillingResponseCode.OK) {
                        getHistoryPurchasedList()
                        queryProductList()
                    }
                }

                override fun onBillingServiceDisconnected() {
                    AccessLogUtility.showErrorMessage(false, TAG, "[ERROR | ${TAG}.startConnection] disconnection google play billing", null)
                }
            })
        }
    }

    /**
     * Call this method once you are done with this BillingClient reference.
     */
    fun endConnection() {
        if (billingClient.isReady) {
            billingClient.endConnection()
        }
    }

    /**
     * Get purchases details for all the items bought within your app.
     */
    fun getHistoryPurchasedList() {
        if (!billingClient.isReady) {
            Log.e(TAG, "queryPurchases: BillingClient is not ready")
        }

        billingClient.queryPurchasesAsync(QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()) { billingResult, purchaseList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (purchaseList.isNotEmpty()) {
                    iabHelperListener.onPurchaseHistoryResponse(purchaseList)
                }
            } else {
                Log.e(TAG, billingResult.debugMessage)
            }
        }
    }

    /**
     * Perform a network query to get SKU details and return the result asynchronously.
     */
    fun queryProductList() {
        val params = QueryProductDetailsParams.newBuilder()

        val productIDMap = HashMap<PurchaseType, List<QueryProductDetailsParams.Product>>().apply {
            val subscriptList = arrayListOf<QueryProductDetailsParams.Product>()
            val inAppList = arrayListOf<QueryProductDetailsParams.Product>()

            productIDList.forEach { product ->
//                if (product.isSubscribe) {
//                    subscriptList.add(
//                        QueryProductDetailsParams.Product.newBuilder()
//                            .setProductId(product.productId.toLowerCase())
//                            .setProductType(BillingClient.ProductType.SUBS)
//                            .build()
//                    )
//                } else {
//                    inAppList.add(
//                        QueryProductDetailsParams.Product.newBuilder()
//                            .setProductId(product.productId.toLowerCase())
//                            .setProductType(BillingClient.ProductType.INAPP)
//                            .build()
//                    )
//                }
            }

            if (subscriptList.size > 0) {
                this[PurchaseType.SUBS] = subscriptList
            } else if (inAppList.size > 0) {
                this[PurchaseType.INAPP] = inAppList
            }
        }

        if (productIDMap.containsKey(PurchaseType.INAPP)) {
            params.setProductList(productIDMap[PurchaseType.INAPP]!!)
        } else if (productIDMap.containsKey(PurchaseType.SUBS)) {
            params.setProductList(productIDMap[PurchaseType.SUBS]!!)
        }

        billingClient.queryProductDetailsAsync(params.build()) { result, skuDetails ->
            AccessLogUtility.showInfoMessage(false, TAG, "[INFO | ${TAG}.querySkuDetailsAsync] query products details : ${result.responseCode}", null)

            when (result.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    val productDetailsList = ArrayList<ProductDetails>()
                    if (productDetailsList.isNotEmpty()) {
                        skuDetails.forEach {
                            productDetailsList.add(it)
                        }
                    }

                    iabHelperListener.onProductListResponse(productDetailsList)
                }
                else -> {
                    iabHelperListener.onPurchaseError(result.responseCode)
                    AccessLogUtility.showErrorMessage(false, TAG, "[ERROR | ${TAG}.querySkuDetailsAsync] ${result.debugMessage}", null)
                }
            }
        }
    }

    /**
     * Initiate the billing flow for an in-app purchase or subscription.
     *
     * @param productDetail SkuDetails of the product to be purchased
     *                   Developer console.
     */
    fun launchBillingFlow(productDetail: ProductDetails, accountID: String) {
        if (billingClient.isReady) {
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setProductDetails(productDetail)
                    .setOfferToken(productDetail.oneTimePurchaseOfferDetails.toString()).build()))
                .setObfuscatedAccountId(accountID).build().run {
                    billingClient.launchBillingFlow(context as Activity, this)
                }
        }
    }

    /**
     * Your listener to get the response for purchase updates which happen when, the user buys
     * something within the app or by initiating a purchase from Google Play Store.
     */
    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        val status = billingResult.responseCode
        if (status == BillingClient.BillingResponseCode.OK && purchases != null) {
            iabHelperListener.onPurchaseVerify(purchases)
        } else if (status == BillingClient.BillingResponseCode.USER_CANCELED) {
            AccessLogUtility.showErrorMessage(false, TAG, "[ERROR | ${TAG}.purchasesUpdatedListener] user cancel purchase", null)
        } else if (status == BillingClient.BillingResponseCode.SERVICE_DISCONNECTED) {
            AccessLogUtility.showErrorMessage(false, TAG, "[ERROR | ${TAG}.purchasesUpdatedListener] service disconnected", null)
            startConnection()
        } else {
            iabHelperListener.onPurchaseError(status)
        }
    }


    fun acknowledgePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (isSignatureValid(purchase)) {
                // 非消耗性購買
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken).build()
                billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                    AccessLogUtility.showInfoMessage(false, TAG, "[INFO | ${TAG}.acknowledgePurchase] purchase acknowledged", null)
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        iabHelperListener.onPurchaseCompleted(billingResult, null)
                    }
                }
            } else {
                // status 10 is signature invalid
                iabHelperListener.onPurchaseError(10)
            }
        }
    }

    fun consumePurchase(purchase: Purchase) {
        // 一次性購買
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (isSignatureValid(purchase)) {
                // 續約消費
                val consumeParams = ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken).build()
                billingClient.consumeAsync(consumeParams) { billingResult, outToken ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        iabHelperListener.onPurchaseCompleted(billingResult, outToken)
                    }

                    AccessLogUtility.showInfoMessage(false, TAG, "[INFO | ${TAG}.acknowledgePurchase] billingResult : " + billingResult.responseCode + "outToken : " + outToken, null)
                }
            } else {
                // status 10 is signature invalid
                iabHelperListener.onPurchaseError(10)
            }
        }
    }

    //region Security Verify : application verify (less-secure validation), can used server verify (ServicePortal)
    private fun isSignatureValid(purchase: Purchase): Boolean {
        return verifyPurchase(BASE_64_ENCODED_PUBLIC_KEY, purchase.originalJson, purchase.signature)
    }

    /**
     * Verifies that the data was signed with the given signature, and returns
     * the verified purchase. The data is in JSON format and signed
     * with a private key. The data also contains the {@link Purchase.PurchaseState}
     * and product ID of the purchase.
     * @param base64PublicKey the base64-encoded public key to use for verifying.
     * @param jsonData the signed JSON string (signed, not encrypted)
     * @param signature the signature for the data, signed with the private key
     */
    private fun verifyPurchase(base64PublicKey: String, jsonData: String, signature: String): Boolean {
        if (TextUtils.isEmpty(jsonData) || TextUtils.isEmpty(base64PublicKey)) {
            AccessLogUtility.showErrorMessage(false, TAG, "[ERROR | ${TAG}.verifyPurchase] purchase verification failed: missing data. ", null)
            return false
        }

        return this.generatePublicKey(base64PublicKey).run {
            verify(this, jsonData, signature)
        }
    }

    /**
     * Generates a PublicKey instance from a string containing the
     * Base64-encoded public key.
     *
     * @param encodedPublicKey Base64-encoded public key
     * @throws IllegalArgumentException if encodedPublicKey is invalid
     */
    private fun generatePublicKey(encodedPublicKey: String): PublicKey {
        try {
            val key = Base64.decode(encodedPublicKey, Base64.DEFAULT)
            return KeyFactory.getInstance("RSA").run {
                this.generatePublic(X509EncodedKeySpec(key))
            }
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeySpecException) {
            throw IllegalArgumentException(e)
        }
    }

    /**
     * Verifies that the signature from the server matches the computed
     * signature on the data.  Returns true if the data is correctly signed.
     *
     * @param publicKey public key associated with the developer account
     * @param jsonData signed data from server
     * @param signature server signature
     * @return true if the data and signature match
     */
    private fun verify(publicKey: PublicKey, jsonData: String, signature: String): Boolean {
        try {
            val signatureBytes = Base64.decode(signature, Base64.DEFAULT)
            return Signature.getInstance("SHA1withRSA").apply {
                initVerify(publicKey)
                update(jsonData.toByteArray())
            }.run {
                if (!this.verify(signatureBytes)) {
                    Log.e(TAG, "Signature verification failed.")
                    false
                }
                true
            }
        } catch (e: IllegalArgumentException) {
            AccessLogUtility.showErrorMessage(false, TAG, "[ERROR | ${TAG}.verify] Base64 decoding failed. ", null)
        } catch (e: NoSuchAlgorithmException) {
            AccessLogUtility.showErrorMessage(false, TAG, "[ERROR | ${TAG}.verify] No Such Algorithm Exception. ", null)
        } catch (e: InvalidKeySpecException) {
            AccessLogUtility.showErrorMessage(false, TAG, "[ERROR | ${TAG}.verify] Invalid KeySpec Exception. ", null)
        } catch (e: SignatureException) {
            AccessLogUtility.showErrorMessage(false, TAG, "[ERROR | ${TAG}.verify] Signature exception. ", null)
        }
        return false
    }
    //endregion
}