package com.practicecoding.sallonapp.appui.screens.MainScreens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.spec.PKCS8EncodedKeySpec
import java.text.SimpleDateFormat
import java.security.PrivateKey
import java.security.Signature

@Composable
fun PaymentScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        UpiPaymentButton(
            context = context,
            payeeAddress = "9630346709kota@axl", // Replace with actual UPI ID
            payeeName = "SHIVENDRA SINGH JAROLIYA",
            amount = "1.00", // Amount
            transactionRefId = "TXN12345654321", // Transaction Reference ID
            // Replace with your actual Base64 encoded private key
        )
    }
}

@Composable
fun UpiPaymentButton(
    context: Context,
    payeeAddress: String,
    payeeName: String,
    amount: String,
    transactionRefId: String,
) {
    Button(onClick = {
        val upiUri = createUPIUriWithSignature(
            pa = payeeAddress,
            pn = payeeName,
            mc = "1234", // Merchant category code
            tid = "txn12345", // Transaction ID
            tr = transactionRefId,
            am = amount,
            cu = "INR", // Currency
            privateKeyStr = "MIIEpAIBAAKCAQEA1FP+Ob4YsLkwgZnpI0SbcGElXLo9z9vO0vMIq9hTrI57D2k" +
                    "Z89q8+jyB/WJ3q07H1oXIO8zCfNUrRVH8kx2iM7uHFSk8DlExgNgokWuoGGzvzn" +
                    "iXDvBbjy1WAsx6z9hPx5pKQn0EEztAaGuLcO6Ldy/Za2BhTOXRBQ/R7XWRUZrvB" +
                    "QZqGQdHwKJ2Z+b7QcVrjAf7+gWzg2ocd+K7/x2ijOHkrDmyVHKaGlq+eiT60I4J" +
                    "CY4Zl9vR9vHTYJvgp/etEVqPH7k70bPaxVzH1x2jz8NQ9K3E0VJw3Aoybik3KhY" +
                    "MfzGx37XoQIDAQABAoIBAHqqC3QH4yiUEqEr5SxcXlINu6Bkq7Bt8hEPZrJlj70l" +
                    "E29nGmM9y0EqT1XvJkNBBX+taAT2RCcVjeRQO5I9AFNyTifNWtW29cmhtPT1tK1o" +
                    "4IZ18Hbc0l5F5TKUxLfOhZ+/0bEF1RFz6unSCNdE48ngKDD2sNm6QMeBbjvZ2UM" +
                    "5v/dj48xfD2rk74y+v7JQj4tZRLc44cc8ejJxHzeDKpQ8a0OJ1cKsSHM5nB8bCQa" +
                    "n9pY3+nLX9RJnD+5KvPGlW+kgzHeAZHtAFKmW1cQ7cvogWAmx6O77FErDRj1ieAm" +
                    "VsNsFFNclvUU8Lm1+8HGR0IuHZECgYEA7dS8mT1+q/ThtP/qkV9rntQXX9ovCtW"+
                    "8G7TKiTQyxJnKLCnq1DH+V+tqTp3c7b6IX6NuU6DCu8NhDP0zLDvMcXCM3UWRkO" +
                    "RKGJfrBwL+f3ELEmxrCzO5nbOlAbyV+nUWQlWtC1ZEdG95kl4e7J4sDxvSOlZnA" +
                    "UZRXg/jtMikCgYEA5JoznrvE75ffzL4mpUyVRimOJlTh+WYGvQpwe3X9yc7El5n" +
                    "AYHoA4OlSqGdKZSDvz3MNyfTZlscE7U1pRY3l+w4BwQhFpN5AorXGug7hoygxbR" +
                    "rvikZ/Dqf5OXaJp6Sv3E1Jm7flHnXnp9H2T8R8hAgDsWlZB8qEwD4NkCgYAcMJvT" +
                    "BQlzxPw+UpzCqRJrruW1yH2qR15CzxkBvQeZW5VnLRKQUAYLq7L9SlQJb9eO5LM" +
                    "zHGNH9+tM0Z9FGypuZdVjBYZKZBmyrfhYLm7FT0aT41x7Km7lvDyyd9P7K2uQ6I" +
                    "1P5ei1+4wEmgtSMkA1T/vq2B6NmqbyRdtU67QQKBgQDNV+2/nIhRf4cFqTqclGg" +
                    "fdXQv9EW8E1drl68bJYkE4u3lC9t8pF8iHr3sirFmHf6hCFz+9z5MvzddR1eDO+" +
                    "fz5sIzI9UCkA9qLpNCqFvCgP+OBt8KHV/g5kiwxgQUJBHqlxlO5sWb15gUX9KsN" +
                    "C1/CNvawxTJpFt/K+Mz6WjIfRw=="

        )
        initiateUPIPayment(context, upiUri)
    }) {
        Text("Pay with UPI", fontSize = 18.sp)
    }
}

fun generateUPISignature(
    privateKeyStr: String,
    concatenatedParams: String
): String {
    val keyFactory = KeyFactory.getInstance("RSA")
    val privateKeyBytes = Base64.decode(privateKeyStr, Base64.DEFAULT)
    val keySpec = PKCS8EncodedKeySpec(privateKeyBytes)
    val privateKey = keyFactory.generatePrivate(keySpec)

    val signature = Signature.getInstance("SHA256withRSA")
    signature.initSign(privateKey)
    signature.update(concatenatedParams.toByteArray())

    val signedData = signature.sign()
    return Base64.encodeToString(signedData, Base64.NO_WRAP)
}

fun createUPIUriWithSignature(
    pa: String,
    pn: String,
    mc: String,
    tid: String,
    tr: String,
    am: String,
    cu: String,
    mode:String="04",
    orgid: String = "000000",
    privateKeyStr: String
): Uri {
    val concatenatedParams = "$pa|$pn|$mc|$tid|$tr|$am|$cu|$mode|$orgid"
    val sign = generateUPISignature(privateKeyStr, concatenatedParams)

    return Uri.Builder()
        .scheme("upi")
        .authority("pay")
        .appendQueryParameter("pa", pa)
        .appendQueryParameter("pn", pn)
        .appendQueryParameter("mc", mc)
        .appendQueryParameter("tid", tid)
        .appendQueryParameter("tr", tr)
        .appendQueryParameter("am", am)
        .appendQueryParameter("cu", cu)
        .appendQueryParameter("orgid", orgid)
        .appendQueryParameter("sign", sign)
        .build()
}

fun initiateUPIPayment(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW, uri)
    val chooser = Intent.createChooser(intent, "Pay with")

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(chooser)
    } else {
        Toast.makeText(context, "No UPI app found", Toast.LENGTH_SHORT).show()
    }
}
