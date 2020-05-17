package fr.epf.moov

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView


class QRCodeActivity  : AppCompatActivity(), ZXingScannerView.ResultHandler  {
    private var scannerView : ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)

    }

    override fun onResume() {
        super.onResume()
        scannerView?.setResultHandler(this)
        scannerView?.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scannerView?.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {
        Log.v("QR Code", rawResult?.text) // Prints scan results
        Log.v("QR Code", rawResult?.barcodeFormat.toString()) // Prints the scan format (qrcode, pdf417 etc.)

        scannerView?.resumeCameraPreview(this)

    }
}