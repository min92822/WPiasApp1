package com.phonegap.WPIAS.camera2Api

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.*
import android.media.ExifInterface
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.TextureView
import android.util.SparseIntArray
import android.view.Surface
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CameraActivity : RootActivity() {

    var Orientations = SparseIntArray()

    var cameraId = ""
    var cameraDevice: CameraDevice? = null
    var cameraCaptureSession: CameraCaptureSession? = null
    var captureRequestBuilder: CaptureRequest.Builder? = null
    var imageDimension: Size? = null
    var imageReader: ImageReader? = null

    var file: File? = null
    val CAMERA_PERMISSION = 200
    var backGroundHandler: Handler? = null
    var backgroundThread: HandlerThread? = null

    var textureListener = object : TextureView.SurfaceTextureListener{

        override fun onSurfaceTextureSizeChanged(
            surface: SurfaceTexture?,
            width: Int,
            height: Int
        ) {
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            return false
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            openCamera()
        }
    }

    var stateCallback = object : CameraDevice.StateCallback() {

        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            createCameraPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice?.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            cameraDevice?.close()
            cameraDevice = null
        }
    }

    init {

        Orientations.append(Surface.ROTATION_0, 90)
        Orientations.append(Surface.ROTATION_90, 0)
        Orientations.append(Surface.ROTATION_180, 270)
        Orientations.append(Surface.ROTATION_270, 180)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        SetDarkBar()

        initActivity()

    }

    // 액티비티 시작 설정
    fun initActivity() {

        cameraTextureView.surfaceTextureListener = textureListener

        capture.setOnClickListener { takePicture() }

    }


    @SuppressLint("SimpleDateFormat")
    fun takePicture() {

        if (cameraDevice == null) {
            return
        }

        var manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {

            var cameraCharacteristics = manager.getCameraCharacteristics(cameraDevice!!.id)
            var jpegSize: Array<Size>

            jpegSize =
                cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
                    .getOutputSizes(ImageFormat.JPEG)

            var width = 1024
            var height = 1024

//            if (jpegSize != null && jpegSize.isNotEmpty()) {
//
//                width = jpegSize[0].width
//                height = jpegSize[0].height
//
//            }

            var reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
            var outputSurface = ArrayList<Surface>()

            outputSurface.add(reader.surface)
            outputSurface.add(Surface(cameraTextureView.surfaceTexture))

            var captureBuilder =
                cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder?.addTarget(reader.surface)
            captureBuilder?.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            captureBuilder?.set(CaptureRequest.JPEG_QUALITY, 50.toByte())

            var rotation = windowManager.defaultDisplay.rotation

            captureBuilder?.set(CaptureRequest.JPEG_ORIENTATION, Orientations.get(rotation))

            file = File(
                this.getExternalFilesDir(null)!!.absolutePath + "/" + SimpleDateFormat("yyyyMMddHHmmss").format(
                    Date()
                ) + ".jpg"
            )

            var readerListener = ImageReader.OnImageAvailableListener {

                var image: Image? = null

                try {

                    image = reader.acquireLatestImage()

                    var buffer = image.planes[0].buffer
                    var byte = ByteArray(buffer.capacity())

                    buffer.get(byte)
                    save(byte)

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    image?.close()
                }

            }

            reader.setOnImageAvailableListener(readerListener, backGroundHandler)
            var captureListener = object : CameraCaptureSession.CaptureCallback() {

                override fun onCaptureCompleted(
                    session: CameraCaptureSession,
                    request: CaptureRequest,
                    result: TotalCaptureResult
                ) {
                    super.onCaptureCompleted(session, request, result)
//                    Toast.makeText(this@CameraActivity, "Saved $file", Toast.LENGTH_LONG).show()
//                    createCameraPreview()
                    var intent = Intent()
                    intent.putExtra("path", file?.absolutePath)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }

            }

            cameraDevice?.createCaptureSession(
                outputSurface,
                object : CameraCaptureSession.StateCallback() {

                    override fun onConfigured(session: CameraCaptureSession) {
                        try {
                            session.capture(
                                captureBuilder!!.build(),
                                captureListener,
                                backGroundHandler
                            )
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                    }

                },
                backGroundHandler
            )

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    @Throws(IOException::class)
    fun save(byte: ByteArray) {

        var out: OutputStream? = null
        var byteStream = ByteArrayOutputStream()

        try {

            out = FileOutputStream(file!!)
            var bitmap = BitmapFactory.decodeByteArray(byte, 0, byte.size)
            bitmap = imageResizing(bitmap)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream)
            bitmap.recycle()

            out.write(byteStream.toByteArray())

        } finally {
            out?.close()
        }

    }

    //사진 1:1비율로 만드는 펑션
    fun imageResizing(bitmap : Bitmap) : Bitmap?{

        var matrix = Matrix()

        return if(bitmap.width > bitmap.height) {
            //가로가 짧은 사진이 들어오는 곳
            matrix.postRotate(90f)
            Bitmap.createScaledBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true), 1200, 1200, true)
        } else {
            //가로가 긴 사진이 들어와야하는데 안들어옴
            Bitmap.createScaledBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true), 1200, 1200, true)
        }
    }

    private fun createCameraPreview() {
        try {
            var texture = cameraTextureView.surfaceTexture
            texture.setDefaultBufferSize(imageDimension!!.width, imageDimension!!.height)
            var surface = Surface(texture)
            captureRequestBuilder =
                cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            captureRequestBuilder?.addTarget(surface)
            cameraDevice?.createCaptureSession(
                listOf(surface),
                object : CameraCaptureSession.StateCallback() {

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                    }

                    override fun onConfigured(session: CameraCaptureSession) {
                        if (cameraDevice == null) {
                            return
                        }
                        cameraCaptureSession = session
                        updatePreview()
                    }

                },
                null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun updatePreview() {
        if (cameraDevice == null)
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        captureRequestBuilder?.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO)
        try {
            cameraCaptureSession?.setRepeatingRequest(
                captureRequestBuilder!!.build(),
                null,
                backGroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == CAMERA_PERMISSION){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "권한이 허용되지 않았습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun openCamera() {

        var manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            cameraId = manager.cameraIdList[0]
            var characteristics = manager.getCameraCharacteristics(cameraId)
            var map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            imageDimension = map!!.getOutputSizes(SurfaceTexture::class.java)[0]

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    CAMERA_PERMISSION
                )
                return
            }

            manager.openCamera(cameraId, stateCallback, null)

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()
        if(cameraTextureView.isAvailable){
            openCamera()
        }else{
            cameraTextureView.surfaceTextureListener = textureListener
        }
    }

    override fun onPause() {
        stopBackgroundThread()
        super.onPause()
    }

    private fun stopBackgroundThread() {
        backgroundThread?.quitSafely()
        try{
            backgroundThread?.join()
            backgroundThread = null
            backGroundHandler = null
        }catch (e : InterruptedException){
            e.printStackTrace()
        }
    }

    private fun startBackgroundThread() {
        backgroundThread = object : HandlerThread("Camera Background"){}
        backgroundThread?.start()
        backGroundHandler = object :Handler(backgroundThread!!.looper){}
    }

}
