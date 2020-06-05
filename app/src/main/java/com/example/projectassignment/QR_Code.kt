package com.example.projectassignment


/// Here Imports all the necessary libraries
import java.io.File
import android.net.Uri
import android.os.Build
import android.os.Bundle
import java.lang.Exception
import java.io.OutputStream
import android.widget.Toast
import android.view.Gravity
import android.widget.Button
import android.os.Environment
import android.os.StrictMode
import android.content.Intent
import android.graphics.Color
import android.graphics.Bitmap
import android.widget.EditText
import android.widget.ImageView
import android.content.Context
import android.app.Notification
import java.io.FileOutputStream
import android.app.PendingIntent
import com.google.zxing.BarcodeFormat
import android.app.NotificationChannel
import android.app.NotificationManager
import com.google.zxing.WriterException
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import com.google.zxing.MultiFormatWriter
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.graphics.drawable.BitmapDrawable
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.google.zxing.integration.android.IntentIntegrator


private const val PERMISSION_REQUEST = 10


class QR_Code : AppCompatActivity() {

    // Initialize App Components
    lateinit var btnShare: Button
    lateinit var btnGenrateQR: Button
    lateinit var imageQR: ImageView
    lateinit var txtString: EditText
    lateinit var btnSaveImage:Button
    lateinit var txtFilename:EditText
    lateinit var btnCamera: Button

    // Declare Variable for Validation Purpose
    var Qr_Genrated = false
    var storageDirectory = ""
    var QR_Saved = false

    //declaring variables for Notification Event
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    val channelId = "com.example.new_activity\n"
    val description = "Notification Genrated by QR_Code App Created by Sahil Sheikh"


    // Main method which will load when components load's successfully
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code)
        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        btnShare = findViewById(R.id.btnShare) as Button
        btnGenrateQR = findViewById(R.id.btnGenrateQR) as Button
        imageQR = findViewById(R.id.imageQR) as ImageView
        txtString = findViewById(R.id.txtString) as EditText
        btnSaveImage = findViewById(R.id.btnSaveImage) as Button
        txtFilename = findViewById(R.id.txtFilename) as EditText
        btnCamera = findViewById(R.id.btnScanCode) as Button



        btnGenrateQR.setOnClickListener {
            // Checking whether user enter string data or not if yes then will call generate QR code method
            if (txtString.text.toString() != "") {
                genrate_QR()
            } else {
                val toast = Toast.makeText(this, "Please Provide Text to Encode", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }




        btnSaveImage.setOnClickListener {
            // Checking whether user enter file name or not if yes then will call Save file  method
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),100)
                }
                else{
                    saveImageToStorage()
                }
            }else{
                saveImageToStorage()
            }
        }




        btnShare.setOnClickListener {
            // Checking whether user enter genrated QR code or not if yes then it will share image to other app
            if (Qr_Genrated == false) {
                Toast.makeText(this, "Please Genrate QR code first", Toast.LENGTH_SHORT).show()
            } else {
                //share image
                saveImageToStorage()
                if (QR_Saved==true) {
                    try {
                        val file = File(storageDirectory, "${txtFilename.text.toString()}.png")
                        val intent = Intent(android.content.Intent.ACTION_SEND)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                        intent.type = "image/png"
                        startActivity(Intent.createChooser(intent, "Share image via"))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "Error Occur due to\n${e.printStackTrace()} ", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }



        // Used to initialize QR code scanner function
        btnCamera.setOnClickListener {
            initCameraScan()
        }
    }




    private fun initCameraScan() {
        IntentIntegrator(this).initiateScan()
    } // End of InitCameraScan()




    //// Used to Genrate Image
    private fun genrate_QR() {
        val multiFormatWritter = MultiFormatWriter()
        try {
            val bitMatrix = multiFormatWritter.encode(txtString.text.toString(), BarcodeFormat.QR_CODE, 300, 300)
            val barcodeEncoder = BarcodeEncoder()
            val QR_bitmap = barcodeEncoder.createBitmap(bitMatrix)
            imageQR.setImageBitmap(QR_bitmap)
            Qr_Genrated = true
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }// End of gerate_QR()





    //// This function will Save Image to Storage
    private fun saveImageToStorage() {
        if (Qr_Genrated==false){
            Toast.makeText(this,"Please Genrate QR code first", Toast.LENGTH_SHORT).show()
        }else {
            if (txtFilename.text.toString() == "") {
                Toast.makeText(this, "Please Enter Filename", Toast.LENGTH_SHORT).show()
            } else {
                val externalStorageState = Environment.getExternalStorageState()
                if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
                    storageDirectory = Environment.getExternalStorageDirectory().toString()
                    val file = File(storageDirectory, "${txtFilename.text.toString()}.png")

                    try {
                        val stream: OutputStream = FileOutputStream(file)
                        //                val drawable = ContextCompat.getDrawable(applicationContext,R.drawable.logo)
                        val drawable = imageQR.drawable
                        val bitmap = (drawable as BitmapDrawable).bitmap
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        stream.flush()
                        stream.close()
                        QR_Saved = true
                        try {
                            Notify("Saved Successfully","Saved at ${Uri.parse(file.absolutePath)}")
                        }catch (e: Exception){
                            Toast.makeText(this, "Exception $e", Toast.LENGTH_SHORT).show()
                        }

                        Toast.makeText(this, "Image Saved ${Uri.parse(file.absolutePath)}", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(this, "Unable to access Storage", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }// End of SaveImageStorage()






    // This Function Used to create notification after the image saved successfully
    private fun Notify(tittle:String,not_desc:String)

    {

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, QR_Code::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this,channelId)
                .setContentTitle(tittle)
                .setContentText(not_desc)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
        }
        else {
            builder = Notification.Builder(this)
                .setContentTitle(tittle)
                .setContentText(not_desc)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
        }
        builder.setDefaults(Notification.DEFAULT_SOUND);
        notificationManager.notify(0,builder.build())
    }// End of Notify()




    ///// Used to Check Storage Grant Permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode==100){
            if (grantResults.size>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){

            }else{
                Toast.makeText(this,"Permission not Granted",Toast.LENGTH_SHORT).show()
            }
        }
    }


    //Used to check QR code scanner result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result!=null){
            if (result.contents==null){
                Toast.makeText(this,"Data is Empty",Toast.LENGTH_SHORT).show()
            }else{
                txtString.setText(result.contents.toString())
                Toast.makeText(this,result.contents.toString(),Toast.LENGTH_SHORT).show()
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }




}/// End of Class QR_Code


