package fr.epf.moov.fragment

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.google.zxing.Result
import fr.epf.moov.AfficherHorairesActivity
import fr.epf.moov.R
import fr.epf.moov.data.AppDatabase
import fr.epf.moov.data.StationDao
import kotlinx.android.synthetic.main.fragment_qrcode.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

class QrCodeFragment :Fragment(), ZXingScannerView.ResultHandler  {
    private var scannerView : ZXingScannerView? = null
    private var nameStation : String? = null
    private var stationDao: StationDao? = null
    private val REQUEST_CAMERA : Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_qrcode, container, false)
        var qrCodeLayout = view.findViewById<RelativeLayout>(R.id.qrcode_layout)
        scannerView = ZXingScannerView(requireContext())
        qrCodeLayout.addView(scannerView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {

                Toast.makeText(requireContext(), "Permission validée", Toast.LENGTH_LONG).show()
            } else {
                requestPermission()
            }
        }

        val database =
            Room.databaseBuilder(requireContext(), AppDatabase::class.java, "listStations")
                .build()
        stationDao = database.getStationDao()

        return view
    }

    private fun checkPermission(): Boolean{
        return (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission(){
        val permission : Array<String> = arrayOf(Manifest.permission.CAMERA)
        ActivityCompat.requestPermissions(Activity(), permission, REQUEST_CAMERA )
    }

    fun onRequestPermissionResult(requestCode : Int, permission : Array<String>, grantResults : List<Int>){
        when (requestCode) {
            REQUEST_CAMERA -> {
                if (grantResults.size>0){
                    val cameraAccepted : Boolean = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted){
                        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_LONG).show()
                    } else{
                        Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_LONG).show()
                        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                                displayAlertMessage("You need to allow access for both permissions", DialogInterface.OnClickListener(){ _, _ ->
                                    fun onClick (dialogInterface : DialogInterface, i: Int){
                                        val permission : Array<String> = arrayOf(Manifest.permission.CAMERA)
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
        AlertDialog.Builder(requireContext())
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

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Résultat")
        builder.setPositiveButton("Recommencer") { _, _ ->
            scannerView!!.resumeCameraPreview(this)
        }

        builder.setNeutralButton("Voir les horaires") { _, _ ->
            Log.d("CCC", "Voir les horaires")
            val intent = Intent(requireContext(), AfficherHorairesActivity :: class.java)
            intent.putExtra("station", nameStation)
            this.startActivity(intent)

        }

        builder.setMessage(" Station trouvée : ${nameStation}")
        val alert: AlertDialog = builder.create()
        alert.show()

    }



}
