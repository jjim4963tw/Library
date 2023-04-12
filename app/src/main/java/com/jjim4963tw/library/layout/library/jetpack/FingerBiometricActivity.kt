package com.jjim4963tw.library.layout.library.jetpack

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat

class FingerBiometricActivity: AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    private val availableCodes = listOf(
            BiometricManager.BIOMETRIC_SUCCESS,
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
    )

    companion object {
        val TAG: String? = FingerBiometricActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        canAuthenticateWithBiometrics().let {
            if (it) {
                showBiometricPrompt()
            } else {
                Toast.makeText(this, "無法使用指紋辨識", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun canAuthenticateWithBiometrics(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            BiometricManager.from(this).let {
                return availableCodes.contains(it.canAuthenticate(
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or
                            BiometricManager.Authenticators.BIOMETRIC_WEAK or
                            BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                )
            }
        } else {
            val fingerprintManagerCompat = FingerprintManagerCompat.from(this)
            fingerprintManagerCompat.hasEnrolledFingerprints() && fingerprintManagerCompat.isHardwareDetected
        }
    }

    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("指紋驗證")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .setDescription("描述")
            .build()

        BiometricPrompt(this, ContextCompat.getMainExecutor(this), object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)

                Toast.makeText(this@FingerBiometricActivity, "Authentication errorString : $errString", Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

                Toast.makeText(this@FingerBiometricActivity, "Success", Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()

                Toast.makeText(this@FingerBiometricActivity, "Authentication failed for an unknown reason", Toast.LENGTH_LONG).show()
            }
        }).authenticate(promptInfo)
    }
}