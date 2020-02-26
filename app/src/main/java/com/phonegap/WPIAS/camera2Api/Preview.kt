//package com.phonegap.WPIAS.camera2Api
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.ImageFormat
//import android.graphics.SurfaceTexture
//import android.hardware.camera2.CameraAccessException
//import android.hardware.camera2.CameraCaptureSession
//import android.hardware.camera2.CameraCharacteristics
//import android.hardware.camera2.CameraDevice
//import android.hardware.camera2.CameraManager
//import android.hardware.camera2.CameraMetadata
//import android.hardware.camera2.CaptureRequest
//import android.hardware.camera2.TotalCaptureResult
//import android.media.Image
//import android.media.ImageReader
//import android.os.Handler
//import android.os.HandlerThread
//import android.util.Log
//import android.util.Size
//import android.util.SparseIntArray
//import android.view.Surface
//import android.view.TextureView
//import android.widget.Button
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//
//import java.io.File
//import java.io.FileNotFoundException
//import java.io.FileOutputStream
//import java.io.IOException
//import java.io.OutputStream
//import java.text.SimpleDateFormat
//import java.util.ArrayList
//import java.util.Arrays
//import java.util.Date
//import java.util.concurrent.Semaphore
//
///**
// * Created by BRB_LAB on 2016-06-07.
// * Modified by JS on 2019-04-03.
// */
//class Preview(
//    private val mContext: Context,
//    private val mTextureView: TextureView,
//    private val mCameraCaptureButton: Button
//) : Thread() {
//
//    private var file : File? = null
//    private var mPreviewSize: Size? = null
//    private var mCameraDevice: CameraDevice? = null
//    private var mPreviewBuilder: CaptureRequest.Builder? = null
//    private var mPreviewSession: CameraCaptureSession? = null
//    private var mCameraId = "0"
//    private var mSensorOrientation = 0
//
//    private val mSurfaceTextureListener = object : TextureView.SurfaceTextureListener {
//
//        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
//            // TODO Auto-generated method stub
//            Log.e(TAG, "onSurfaceTextureAvailable, width=$width, height=$height")
//            openCamera()
//        }
//
//        override fun onSurfaceTextureSizeChanged(
//            surface: SurfaceTexture,
//            width: Int, height: Int
//        ) {
//            // TODO Auto-generated method stub
//            Log.e(TAG, "onSurfaceTextureSizeChanged")
//        }
//
//        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
//            // TODO Auto-generated method stub
//            return false
//        }
//
//        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
//            // TODO Auto-generated method stub
//        }
//    }
//
//    private val mStateCallback = object : CameraDevice.StateCallback() {
//
//        override fun onOpened(camera: CameraDevice) {
//            // TODO Auto-generated method stub
//            Log.e(TAG, "onOpened")
//            mCameraDevice = camera
//            startPreview()
//        }
//
//        override fun onDisconnected(camera: CameraDevice) {
//            // TODO Auto-generated method stub
//            Log.e(TAG, "onDisconnected")
//        }
//
//        override fun onError(camera: CameraDevice, error: Int) {
//            // TODO Auto-generated method stub
//            Log.e(TAG, "onError")
//        }
//
//    }
//
//    private val mCameraOpenCloseLock = Semaphore(1)
//
//    init {
//
//        mCameraCaptureButton.setOnClickListener { takePicture() }
//
//    }
//
//    private fun getBackFacingCameraId(cManager: CameraManager): String? {
//        try {
//            for (cameraId in cManager.cameraIdList) {
//                val characteristics = cManager.getCameraCharacteristics(cameraId)
//                val cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING)!!
//                if (cOrientation == CameraCharacteristics.LENS_FACING_BACK) return cameraId
//            }
//        } catch (e: CameraAccessException) {
//            e.printStackTrace()
//        }
//
//        return null
//    }
//
//    fun openCamera() {
//        val manager = mContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
//        Log.e(TAG, "openCamera E")
//        try {
//            val cameraId = getBackFacingCameraId(manager)
//            val characteristics = manager.getCameraCharacteristics(mCameraId)
//            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
//            mPreviewSize = map!!.getOutputSizes(SurfaceTexture::class.java)[0]
//
//            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
//
//            val permissionCamera =
//                ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
//            if (permissionCamera == PackageManager.PERMISSION_DENIED) {
//                ActivityCompat.requestPermissions(
//                    mContext as Activity,
//                    arrayOf(Manifest.permission.CAMERA),
//                    CameraActivity.REQUEST_CAMERA
//                )
//            } else {
//                manager.openCamera(mCameraId, mStateCallback, null)
//            }
//        } catch (e: CameraAccessException) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        }
//
//        Log.e(TAG, "openCamera X")
//    }
//
//    protected fun startPreview() {
//        // TODO Auto-generated method stub
//        if (null == mCameraDevice || !mTextureView.isAvailable || null == mPreviewSize) {
//            Log.e(TAG, "startPreview fail, return")
//        }
//
//        val texture = mTextureView.surfaceTexture
//        if (null == texture) {
//            Log.e(TAG, "texture is null, return")
//            return
//        }
//
//        texture.setDefaultBufferSize(mPreviewSize!!.width, mPreviewSize!!.height)
//        val surface = Surface(texture)
//
//        try {
//            mPreviewBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
//        } catch (e: CameraAccessException) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        }
//
//        mPreviewBuilder!!.addTarget(surface)
//
//        try {
//            mCameraDevice!!.createCaptureSession(
//                Arrays.asList(surface),
//                object : CameraCaptureSession.StateCallback() {
//
//                    override fun onConfigured(session: CameraCaptureSession) {
//                        // TODO Auto-generated method stub
//                        mPreviewSession = session
//                        updatePreview()
//                    }
//
//                    override fun onConfigureFailed(session: CameraCaptureSession) {
//                        // TODO Auto-generated method stub
//                        Toast.makeText(mContext, "onConfigureFailed", Toast.LENGTH_LONG).show()
//                    }
//                },
//                null
//            )
//        } catch (e: CameraAccessException) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        }
//
//    }
//
//    protected fun updatePreview() {
//        // TODO Auto-generated method stub
//        if (null == mCameraDevice) {
//            Log.e(TAG, "updatePreview error, return")
//        }
//
//        mPreviewBuilder!!.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
//        val thread = HandlerThread("CameraPreview")
//        thread.start()
//        val backgroundHandler = Handler(thread.looper)
//
//        try {
//            mPreviewSession!!.setRepeatingRequest(
//                mPreviewBuilder!!.build(),
//                null,
//                backgroundHandler
//            )
//        } catch (e: CameraAccessException) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        }
//
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    protected fun takePicture() {
//        if (null == mCameraDevice) {
//            Log.e(TAG, "mCameraDevice is null, return")
//            return
//        }
//
//        try {
//            var jpegSizes: Array<Size>? = null
//            val cameraManager = mContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
//            val characteristics = cameraManager.getCameraCharacteristics(mCameraId)
//            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
//
//            if (map != null) {
//                jpegSizes = map.getOutputSizes(ImageFormat.JPEG)
//                //                Log.d("TEST", "map != null " + jpegSizes.length);
//            }
//            var width = 640
//            var height = 480
//            if (jpegSizes != null && jpegSizes.isNotEmpty()) {
//                //                for (int i = 0 ; i < jpegSizes.length; i++) {
//                //                    Log.d("TEST", "getHeight = " + jpegSizes[i].getHeight() + ", getWidth = " + jpegSizes[i].getWidth());
//                //                }
//                width = jpegSizes[0].width
//                height = jpegSizes[0].height
//            }
//
//            val reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
//            val outputSurfaces = ArrayList<Surface>(2)
//            outputSurfaces.add(reader.surface)
//            outputSurfaces.add(Surface(mTextureView.surfaceTexture))
//
//            val captureBuilder =
//                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
//            captureBuilder.addTarget(reader.surface)
//            //            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
//
//            captureBuilder.set(
//                CaptureRequest.CONTROL_AF_TRIGGER,
//                CameraMetadata.CONTROL_AF_TRIGGER_START
//            )
//
//            // Orientation
//            val rotation = (mContext as Activity).windowManager.defaultDisplay.rotation
//            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation))
//
//            val date = Date()
//            val dateFormat = SimpleDateFormat("yyyyMMddhhmmss")
//
//            file = File(
//                mContext.getExternalFilesDir(null)!!.absolutePath,
//                "pic_" + dateFormat.format(date) + ".jpg"
//            )
//
//            val readerListener = object : ImageReader.OnImageAvailableListener {
//                override fun onImageAvailable(reader: ImageReader) {
//                    var image: Image? = null
//                    try {
//                        image = reader.acquireLatestImage()
//                        val buffer = image!!.planes[0].buffer
//                        val bytes = ByteArray(buffer.capacity())
//                        buffer.get(bytes)
//                        save(bytes)
//                    } catch (e: FileNotFoundException) {
//                        e.printStackTrace()
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    } finally {
//                        if (image != null) {
//                            image.close()
//                            reader.close()
//                        }
//                    }
//                }
//
//                @Throws(IOException::class)
//                private fun save(bytes: ByteArray) {
//                    var output: OutputStream? = null
//                    try {
//                        output = FileOutputStream(file)
//                        output.write(bytes)
//                    } finally {
//                        output?.close()
//                    }
//                }
//            }
//
//            val thread = HandlerThread("CameraPicture")
//            thread.start()
//            val backgroudHandler = Handler(thread.looper)
//            reader.setOnImageAvailableListener(readerListener, backgroudHandler)
//
//            val captureListener = object : CameraCaptureSession.CaptureCallback() {
//                override fun onCaptureCompleted(
//                    session: CameraCaptureSession,
//                    request: CaptureRequest, result: TotalCaptureResult
//                ) {
//                    super.onCaptureCompleted(session, request, result)
//                    var intent = Intent()
//                    intent.putExtra("path", (mContext as CameraActivity).mPreview!!.imageFilePath())
//                    mContext.setResult(Activity.RESULT_OK, intent)
//                    mContext.finish()
//                }
//
//            }
//
//            mCameraDevice!!.createCaptureSession(
//                outputSurfaces,
//                object : CameraCaptureSession.StateCallback() {
//                    override fun onConfigured(session: CameraCaptureSession) {
//                        try {
//                            session.capture(
//                                captureBuilder.build(),
//                                captureListener,
//                                backgroudHandler
//                            )
//                        } catch (e: CameraAccessException) {
//                            e.printStackTrace()
//                        }
//
//                    }
//
//                    override fun onConfigureFailed(session: CameraCaptureSession) {
//
//                    }
//                },
//                backgroudHandler
//            )
//
//        } catch (e: CameraAccessException) {
//            e.printStackTrace()
//        }
//
//    }
//
//    fun setSurfaceTextureListener() {
//        mTextureView.surfaceTextureListener = mSurfaceTextureListener
//    }
//
//    fun onResume() {
//        Log.d(TAG, "onResume")
//        setSurfaceTextureListener()
//    }
//
//    fun onPause() {
//        // TODO Auto-generated method stub
//        Log.d(TAG, "onPause")
//        try {
//            mCameraOpenCloseLock.acquire()
//            if (null != mCameraDevice) {
//                mCameraDevice!!.close()
//                mCameraDevice = null
//                Log.d(TAG, "CameraDevice Close")
//            }
//        } catch (e: InterruptedException) {
//            throw RuntimeException("Interrupted while trying to lock camera closing.")
//        } finally {
//            mCameraOpenCloseLock.release()
//        }
//    }
//
//    fun imageFilePath() : String{
//        return file!!.absolutePath
//    }
//
//    private fun getOrientation(rotation: Int): Int {
//        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
//        // We have to take that into account and rotate JPEG properly.
//        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
//        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
//        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360
//    }
//
//    companion object {
//        private val TAG = "Preview : "
//
//        private val ORIENTATIONS = SparseIntArray(4)
//
//        init {
//            ORIENTATIONS.append(Surface.ROTATION_0, 90)
//            ORIENTATIONS.append(Surface.ROTATION_90, 0)
//            ORIENTATIONS.append(Surface.ROTATION_180, 270)
//            ORIENTATIONS.append(Surface.ROTATION_270, 180)
//        }
//    }
//
//}