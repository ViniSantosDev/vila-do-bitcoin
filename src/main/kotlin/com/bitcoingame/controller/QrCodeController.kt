package com.bitcoingame.controller

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream

@RestController
class QrCodeController( @Value("\${app.lightning-address}") private val lightningAddress: String) {

    @GetMapping("/qrcode/lightning", produces = [MediaType.IMAGE_PNG_VALUE])
    fun generateLightningQrCode(): ByteArray {
        val lightningAddress = "lightning:$lightningAddress"
        val size = 160

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(lightningAddress, BarcodeFormat.QR_CODE, size, size)

        val outputStream = ByteArrayOutputStream()
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream)

        return outputStream.toByteArray()
    }
}