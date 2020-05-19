package fr.epf.moov

import android.Manifest.permission.CAMERA
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.google.zxing.Result
import fr.epf.moov.data.AppDatabase
import fr.epf.moov.data.StationDao
import me.dm7.barcodescanner.zxing.ZXingScannerView


class QRCodeActivity  : AppCompatActivity(), ZXingScannerView.ResultHandler  {
    private var scannerView : ZXingScannerView? = null
    private var nameStation : String? = null
    private var stationDao: StationDao? = null
    private val REQUEST_CAMERA : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkPermission()){

                Toast.makeText(this,"Permission valide", Toast.LENGTH_LONG).show()
            }
            else{
                requestPermission()
            }
        }

        val database =
            Room.databaseBuilder(this, AppDatabase::class.java, "listStations")
                .build()
        stationDao = database.getStationDao()

    }
    private fun checkPermission(): Boolean{
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission(){
        val permission : Array<String> = arrayOf(CAMERA)
        ActivityCompat.requestPermissions(this, permission, REQUEST_CAMERA )
    }

    fun onRequestPermissionResult(requestCode : Int, permission : Array<String>, grantResults : List<Int>){
        when (requestCode) {
            REQUEST_CAMERA -> {
                if (grantResults.size>0){
                    val cameraAccepted : Boolean = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                    } else{
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                            if (shouldShowRequestPermissionRationale(CAMERA)){
                                displayAlertMessage("You need to allow access for both permissions", DialogInterface.OnClickListener(){ _, _ ->
                                    fun onClick (dialogInterface : DialogInterface, i: Int){
                                        val permission : Array<String> = arrayOf(CAMERA)
                                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                            requestPermissions(permission, REQUEST_CAMERA)
                                        }

                                    }
                                })
                                return

                            }
                        }
                    }
                }
            }

        }
    }

    fun displayAlertMessage(message : String, listener : DialogInterface.OnClickListener){
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", listener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    override fun onResume() {
        super.onResume()
               scannerView?.setResultHandler(this)
                scannerView?.startCamera()


    }

    override fun onDestroy() {
        super.onDestroy()
        scannerView!!.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {
        nameStation = rawResult?.text
        Log.d("CCC", nameStation)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Scan Result")
        builder.setPositiveButton("OK") { _, _ ->
                scannerView!!.resumeCameraPreview(this)
            }

        builder.setNeutralButton("Voir les horaires") { _, _ ->
            Log.d("CCC", "Voir les horaires")
                val intent = Intent(this, MainActivity :: class.java)
                this.startActivity(intent)

        }

        builder.setMessage("Station : ${nameStation}")
        val alert: AlertDialog = builder.create()
        alert.show()

        }



    }
