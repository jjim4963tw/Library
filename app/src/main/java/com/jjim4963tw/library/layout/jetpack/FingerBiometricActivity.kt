package com.jjim4963tw.library.layout.jetpack

import android.hardware.biometrics.BiometricManager.Authenticators.*
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class FingerBiometricActivity: AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    private val availableCodes = listOf(
            android.hardware.biometrics.BiometricManager.BIOMETRIC_SUCCESS,
            android.hardware.biometrics.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
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
            this.getSystemService(android.hardware.biometrics.BiometricManager::class.java)?.let {
                return availableCodes.contains(it.canAuthenticate(BIOMETRIC_STRONG or BIOMETRIC_WEAK))
            } ?: false
        } else {
            androidx.biometric.BiometricManager.from(this).let {
                it.canAuthenticate() == androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
            }
        }
    }

    private fun showBiometricPrompt() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            showBiometricPromptOfUpperAndroidP()
        } else {
            showBiometricPromptOfLowerAndroidP()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun showBiometricPromptOfUpperAndroidP() {
        val biometricPrompt = android.hardware.biometrics.BiometricPrompt.Builder(this).apply {
            this.setTitle("指紋驗證")
            this.setDescription("描述")
            this.setNegativeButton("取消", mainExecutor, { _, _ -> Toast.makeText(this@FingerBiometricActivity, "Cancel", Toast.LENGTH_LONG).show() })
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                this.setAllowedAuthenticators(BIOMETRIC_STRONG or BIOMETRIC_WEAK)
            }
        }.build()

        val cancellationSignal = CancellationSignal().apply {
            this.setOnCancelListener {
                Toast.makeText(this@FingerBiometricActivity, "Cancel", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        biometricPrompt.authenticate(cancellationSignal, mainExecutor, object : android.hardware.biometrics.BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)

                Toast.makeText(this@FingerBiometricActivity, "Authentication errorString : $errString", Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationSucceeded(result: android.hardware.biometrics.BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)

                Toast.makeText(this@FingerBiometricActivity, "Success", Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()

                Toast.makeText(this@FingerBiometricActivity, "Authentication failed for an unknown reason", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showBiometricPromptOfLowerAndroidP() {
        val promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("指紋驗證")
                .setDescription("描述")
                .setDeviceCredentialAllowed(true)
                .build()

        androidx.biometric.BiometricPrompt(this, ContextCompat.getMainExecutor(this), object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)

                Toast.makeText(this@FingerBiometricActivity, "Authentication errorString : $errString", Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
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