package com.example.testapp13

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.JavaCamera2View
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.CvException
import org.opencv.core.KeyPoint
import org.opencv.core.Mat
import org.opencv.core.MatOfKeyPoint
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfRect
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.features2d.SimpleBlobDetector
import org.opencv.features2d.SimpleBlobDetector_Params
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.abs
import kotlin.math.sqrt

class CameraActivity : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    companion object {
        private const val TAG = "CameraActivity"
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
    private lateinit var switchCameraButton: Button
    private lateinit var cameraBridgeViewBase: CameraBridgeViewBase
    private lateinit var javaCamera2View: JavaCamera2View
    private var  face: Rect? = null
    private var leftEye: Rect? = null
    private var rightEye: Rect?= null
    private var leftPupil: Mat? = null
    private var rightPupil: Mat? = null
    private var leftGlint: Point? = null
    private var rightGlint: Point? = null
    private var minExpectedEyeArea = 100 // Adjust this value based on your image size and eye size
    private var maxExpectedEyeArea = 1000 // Adjust this value based on your image size and eye size
    private var maxEyeVerticalOffset = 50 // Adjust this value based on your face detection accuracy
    private var currentCameraId = CameraBridgeViewBase.CAMERA_ID_BACK // Default to back camera
    private enum class DetectionStage {
        FACE, EYES, PUPILS, GLINTS, ANALYSIS
    }
    private var currentStage = DetectionStage.FACE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        supportActionBar?.hide()
        javaCamera2View = findViewById(R.id.javaCam2View)

        switchCameraButton = findViewById(R.id.switchCameraButton)

        switchCameraButton.setOnClickListener {
            switchCamera()
        }
        // Copy cascade files
        for (cascadeFile in cascadeFiles) {
            copyCascadeFile(cascadeFile)
        }

        // Request camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            initializeCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeCamera()
        } else {
            // Handle permission denied
        }
    }

    private fun initializeCamera() {
        // Initialize OpenCV
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "Unable to load OpenCV")
        } else {
            Log.d(TAG, "OpenCV loaded successfully")
            cameraBridgeViewBase = javaCamera2View
            cameraBridgeViewBase.setCameraIndex(currentCameraId)
            cameraBridgeViewBase.visibility = SurfaceView.VISIBLE
            cameraBridgeViewBase.setCvCameraViewListener(this)
            cameraBridgeViewBase.setCameraPermissionGranted()
            cameraBridgeViewBase.enableView()
        }
    }
    override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback)
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!")
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    override fun onPause() {
        super.onPause()
        cameraBridgeViewBase.disableView()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraBridgeViewBase.disableView()
    }


    // Implement CvCameraViewListener2 methods (onCameraViewStarted, onCameraViewStopped, onCameraFrame)
    override fun onCameraViewStarted(width: Int, height: Int) {
        // ... (Optional: Initialize resources here) ...
    }

    override fun onCameraViewStopped() {
        // ... (Optional: Release resources here) ...
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {
        val frame = inputFrame.rgba()
        Log.d(TAG, "Frame size: ${frame.cols()}x${frame.rows()}")
        // Rotate image 90 degrees clockwise (if needed)
        if (currentCameraId == CameraBridgeViewBase.CAMERA_ID_FRONT) {
            Core.rotate(frame, frame, Core.ROTATE_180) // Rotate 180 degrees
        }
        val rotatedFrame = Mat()
        Core.rotate(frame, rotatedFrame, Core.ROTATE_90_CLOCKWISE)

        // Perform Detection for each frame
        processFrame(rotatedFrame)

        // Update visualizations based on the latest detection
        visualizeResults(rotatedFrame)

        return rotatedFrame
    }

    private val cascadeFiles = listOf(
        "haarcascade_frontalface_alt.xml",
        "haarcascade_eye.xml",
        "haarcascade_eye_tree_eyeglasses.xml",
        "haarcascade_lefteye_2splits.xml",
        "haarcascade_righteye_2splits.xml"
    )


    private fun processFrame(frame: Mat) {
        when (currentStage) {
            DetectionStage.FACE -> {
                detectFace(frame)
                if (face != null) {
                    currentStage = DetectionStage.EYES
                }
            }
            DetectionStage.EYES -> {
                detectEyes(frame)
                if ((leftEye != null && leftPupil != null) || (rightEye != null && rightPupil != null)) {
                    currentStage = DetectionStage.PUPILS
                }
            }
            DetectionStage.PUPILS -> {
                detectPupils(frame)
                if ((leftPupil != null && leftGlint != null) || (rightPupil != null && rightGlint != null)) {
                    currentStage = DetectionStage.GLINTS
                }
            }
            DetectionStage.GLINTS -> {
                detectGlints(frame)
                if ((leftGlint != null && rightGlint != null) && (leftPupil != null && rightPupil != null)) {
                    currentStage = DetectionStage.ANALYSIS
                }
            }
            DetectionStage.ANALYSIS -> {
                analyzeGlints()
                // You might want to reset to FACE detection after analysis, or
                // implement a mechanism to only re-analyze when there's a significant change
                // in glint positions.
                // currentStage = DetectionStage.FACE
            }
        }
    }

    private fun detectFace(frame: Mat) {
        val grayFrame = Mat()
        try {
            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY)
        } catch (e: CvException) {
            Log.e(TAG, "Error converting frame to grayscale: ${e.message}")
            return // Exit the function if there's an error
        }
        val faces = MatOfRect()
        val faceCascade = CascadeClassifier(File(filesDir, "haarcascade_frontalface_alt.xml").absolutePath)
        faceCascade.detectMultiScale(grayFrame, faces)

        if (faces.toArray().isNotEmpty()) {
            face = faces.toArray()[0]
            currentStage = DetectionStage.EYES
        }
    }

    private fun detectEyes(frame: Mat) {
        if (face == null) return
        val grayFrame = Mat()
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY)
        val eyes = MatOfRect()

        // Use more specific cascade files for individual eye detection
        for (cascadeFile in listOf("haarcascade_lefteye_2splits.xml", "haarcascade_righteye_2splits.xml")) {
            val eyeCascade = CascadeClassifier(File(filesDir, cascadeFile).absolutePath)
            eyeCascade.detectMultiScale(grayFrame.submat(face!!), eyes)
            // If we find at least one eye with a specific cascade, proceed to logic below
            if (eyes.toArray().isNotEmpty()) break
        }

        when (eyes.toArray().size) {
            2 -> {
                val eyeRects = eyes.toArray()

                // Determine left and right eyes from the person's perspective (not the camera's):
                leftEye = if (eyeRects[0].x > eyeRects[1].x) eyeRects[0] else eyeRects[1] // Left eye is on the right side of the image
                rightEye = if (eyeRects[0].x < eyeRects[1].x) eyeRects[0] else eyeRects[1] // Right eye is on the left side of the image

                // Adjust eye coordinates relative to the face region:
                leftEye?.x = leftEye?.x?.plus(face!!.x)
                leftEye?.y = leftEye?.y?.plus(face!!.y)
                rightEye?.x = rightEye?.x?.plus(face!!.x)
                rightEye?.y = rightEye?.y?.plus(face!!.y)
                currentStage = DetectionStage.PUPILS
            }
            1 -> {
                // Handle single eye detection with caution
                val eyeRect = eyes.toArray()[0]

                // Additional checks to reduce false positives (optional):
                // 1. Check if eye size is within an expected range
                val eyeArea = eyeRect.area()
                if (eyeArea < minExpectedEyeArea || eyeArea > maxExpectedEyeArea) return

                // 2. Check if eye is roughly centered vertically within the face
                val faceCenterY = face!!.y + face!!.height / 2
                val eyeCenterY = eyeRect.y + eyeRect.height / 2
                if (abs(eyeCenterY - faceCenterY) > maxEyeVerticalOffset) return

                // Assign left or right eye based on x-position
                if (eyeRect.x < face!!.width / 2) {
                    leftEye = eyeRect
                } else {
                    rightEye = eyeRect
                }

                // Скорректировать координаты глаза относительно области лица:
                if (leftEye != null) {
                    leftEye?.x = leftEye?.x?.plus(face!!.x)
                    leftEye?.y = leftEye?.y?.plus(face!!.y)
                } else if (rightEye != null) {
                    rightEye?.x = rightEye?.x?.plus(face!!.x)
                    rightEye?.y = rightEye?.y?.plus(face!!.y)
                }

                currentStage = DetectionStage.PUPILS
            }
            else -> {
                // Handle cases with no eyes or more than two eyes detected
                Log.w(TAG, "Unexpected number of eyes detected: ${eyes.toArray().size}")
                // You might want to reset the detection stage or implement other logic here
            }
        }
    }

    private fun detectPupils(frame: Mat) {
        if (leftEye == null && rightEye == null) return

        val grayFrame = Mat()
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY)

        if (leftEye != null) {
            leftPupil = detectPupilContours(grayFrame.submat(leftEye!!))
        }

        if (rightEye != null) {
            rightPupil = detectPupilContours(grayFrame.submat(rightEye!!))
        }

        if ((leftEye != null && leftPupil != null) || (rightEye != null && rightPupil != null)) {
            currentStage = DetectionStage.GLINTS
        }
    }

    private fun detectGlints(frame: Mat) {
        if (leftPupil == null && rightPupil == null) return

        val leftPupilCopy = leftPupil?.clone()  // Create a copy to avoid mutability issues
        if (leftPupilCopy != null) {
            val glint = detectGlintBlob(leftPupilCopy)
            if (glint != null) {
                leftGlint = glint
                val center = Point(glint.x + leftEye!!.x, glint.y + leftEye!!.y)
                Imgproc.circle(frame, center, 3, Scalar(255.0, 0.0, 255.0), -1)
            }
        }

        val rightPupilCopy = rightPupil?.clone() // Create a copy to avoid mutability issues
        if (rightPupilCopy != null) {
            val glint = detectGlintBlob(rightPupilCopy)
            if (glint != null) {
                rightGlint = glint
                val center = Point(glint.x + rightEye!!.x, glint.y + rightEye!!.y)
                Imgproc.circle(frame, center, 3, Scalar(255.0, 0.0, 255.0), -1)
            }
        }

        if ((leftPupil != null && leftGlint != null) || (rightPupil != null && rightGlint != null)) {
            currentStage = DetectionStage.ANALYSIS
        }
    }

    private fun analyzeGlints() {
        if (leftGlint == null || rightGlint == null || leftPupil == null || rightPupil == null) return

        // Calculate pupil centers
        val leftPupilCenter = Point(
            leftPupil!!.cols() / 2.0 + leftEye!!.x, // divide by 2.0 to get Double
            leftPupil!!.rows() / 2.0 + leftEye!!.y // divide by 2.0 to get Double
        )
        val rightPupilCenter = Point(
            rightPupil!!.cols() / 2.0 + rightEye!!.x, // divide by 2.0 to get Double
            rightPupil!!.rows() / 2.0 + rightEye!!.y // divide by 2.0 to get Double
        )

        // Calculate relative glint positions
        val leftRelativeGlintX = leftGlint!!.x - leftPupilCenter.x
        val leftRelativeGlintY = leftGlint!!.y - leftPupilCenter.y
        val rightRelativeGlintX = rightGlint!!.x - rightPupilCenter.x
        val rightRelativeGlintY = rightGlint!!.y - rightPupilCenter.y

        // Calculate distances from center and normalized distances
        val leftDistanceFromCenter = sqrt(leftRelativeGlintX * leftRelativeGlintX + leftRelativeGlintY * leftRelativeGlintY)
        val rightDistanceFromCenter = sqrt(rightRelativeGlintX * rightRelativeGlintX + rightRelativeGlintY * rightRelativeGlintY)
        val leftPupilRadius = leftPupil!!.cols() / 2.0 // divide by 2.0 to get Double
        val rightPupilRadius = rightPupil!!.cols() / 2.0 // divide by 2.0 to get Double
        val leftNormalizedDistance = leftDistanceFromCenter / leftPupilRadius
        val rightNormalizedDistance = rightDistanceFromCenter / rightPupilRadius

        // Approximate angles using Hirschberg test principle
        val leftAngle = approximateAngle(leftNormalizedDistance)
        val rightAngle = approximateAngle(rightNormalizedDistance)

        // Determine strabismus type and display/store results
        val strabismusType = when {
            leftAngle > 15 && rightAngle > 15 -> "Esotropia (Convergent strabismus)"
            leftAngle < -15 && rightAngle < -15 -> "Exotropia (Divergent strabismus)"
            leftAngle > 15 && rightAngle < -15 -> "Hypertropia (Vertical strabismus)"
            else -> "No significant strabismus detected"
        }

        Log.d(TAG, "Strabismus Analysis: $strabismusType")
        // ... (Display or store results as needed) ...
    }

    private fun approximateAngle(normalizedDistance: Double): Double {
        return when {
            normalizedDistance < 0.2 -> 0.0   // Glint near center: approximately straight
            normalizedDistance < 0.4 -> 15.0  // Glint slightly off-center
            normalizedDistance < 0.6 -> 25.0  // Glint closer to the edge of the pupil
            normalizedDistance < 0.8 -> 35.0  // Glint near the edge of the pupil
            else -> 45.0                     // Glint very far off-center (large angle)
        }
    }

    private fun detectPupilContours(eye: Mat): Mat? {
        val contours = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(
            eye,
            contours,
            hierarchy,
            Imgproc.RETR_EXTERNAL,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        if (contours.isEmpty()) {
            Log.w(TAG, "No contours found in eye region")
            return null
        }

        // Поиск контура с наибольшей площадью (предположительно зрачок)
        var largestContour: MatOfPoint? = null
        var largestArea = 0.0
        for (contour in contours) {
            val area = Imgproc.contourArea(contour)
            if (area > largestArea) {
                largestArea = area
                largestContour = contour
            }
        }

        if (largestContour == null) {
            Log.w(TAG, "Could not find a suitable contour for pupil")
            return null
        }

        val boundingRect = Imgproc.boundingRect(largestContour)
        return eye.submat(boundingRect) // Возвращаем подобласть зрачка
    }


    private fun detectGlintBlob(grayPupil: Mat): Point? {
        if (grayPupil.empty()) {
            Log.w(TAG, "Empty grayPupil Mat passed to detectGlintBlob")
            return null
        }

        val params = SimpleBlobDetector_Params()
        // Настройка параметров (размер, круглость, выпуклость и т.д.)
        // ...

        val detector = SimpleBlobDetector.create(params)
        val keypoints = MatOfKeyPoint()
        detector.detect(grayPupil, keypoints)

        if (keypoints.toArray().isEmpty()) {
            Log.w(TAG, "No blobs detected in pupil region")
            return null
        }

        // Выбор ключевой точки с наибольшим откликом (предположительно блик)
        var largestResponse = 0.0
        var largestKeypoint: KeyPoint? = null
        for (keypoint in keypoints.toArray()) {
            if (keypoint.response.toDouble() > largestResponse) {
                largestResponse = keypoint.response.toDouble()
                largestKeypoint = keypoint
            }
        }

        return largestKeypoint?.pt?.let {
            Point(it.x, it.y) // convert to Double before creating Point
        } // Возвращаем точку блика, если найден
    }

    private fun visualizeResults(frame: Mat) {
        if (face != null) {
            Imgproc.rectangle(frame, face!!.tl(), face!!.br(), Scalar(255.0, 255.0, 0.0), 2) // Yellow rectangle for face
        } else {
            Imgproc.putText(frame, "Лица не обнаружено",
                Point(10.0, 50.0), Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, Scalar(255.0, 0.0, 0.0), 2)
        }

        if (leftEye != null) {
            Imgproc.rectangle(frame, leftEye!!.tl(), leftEye!!.br(), Scalar(0.0, 0.0, 255.0), 2) // Blue rectangle for left eye
            Imgproc.putText(frame, "L", Point(leftEye!!.x - 10.0, leftEye!!.y - 10.0), Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, Scalar(0.0, 0.0, 255.0), 2)
        }

        if (rightEye != null) {
            Imgproc.rectangle(frame, rightEye!!.tl(), rightEye!!.br(), Scalar(0.0, 0.0, 255.0), 2) // Blue rectangle for right eye
            Imgproc.putText(frame, "R", Point(rightEye!!.x - 10.0, rightEye!!.y - 10.0), Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, Scalar(0.0, 0.0, 255.0), 2)
        }

        if ((leftEye == null || leftPupil == null) && (rightEye == null || rightPupil == null)) {
            Imgproc.putText(frame, "Глаз не обнаружено", Point(10.0, 100.0), Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, Scalar(255.0, 0.0, 0.0), 2)
        }

        if (leftPupil != null) {
            val center = Point(leftPupil!!.cols() / 2.0 + leftEye!!.x, leftPupil!!.rows() / 2.0 + leftEye!!.y)
            Imgproc.circle(frame, center, leftPupil!!.cols() / 2, Scalar(0.0, 255.0, 0.0), 2) // Green circle for left pupil
        }

        if (rightPupil != null) {
            val center = Point(rightPupil!!.cols() / 2.0 + rightEye!!.x, rightPupil!!.rows() / 2.0 + rightEye!!.y)
            Imgproc.circle(frame, center, rightPupil!!.cols() / 2, Scalar(0.0, 255.0, 0.0), 2) // Green circle for right pupil
        }

        if ((leftPupil == null || leftGlint == null) && (rightPupil == null || rightGlint == null)) {
            Imgproc.putText(frame, "Зрачки не найдены", Point(10.0, 150.0), Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, Scalar(255.0, 0.0, 0.0), 2)
        }

        if (leftGlint != null) {
            val center = Point(leftGlint!!.x + leftEye!!.x, leftGlint!!.y + leftEye!!.y)
            Imgproc.circle(frame, center, 3, Scalar(255.0, 0.0, 255.0), -1) // Pink circle for left glint
        }

        if (rightGlint != null) {
            val center = Point(rightGlint!!.x + rightEye!!.x, rightGlint!!.y + rightEye!!.y)
            Imgproc.circle(frame, center, 3, Scalar(255.0, 0.0, 255.0), -1) // Pink circle for right glint
        }
    }

    private val mLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    Log.i(TAG, "OpenCV loaded successfully")
                    cameraBridgeViewBase.enableView()
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    private fun copyCascadeFile(cascadeFileName: String) {
        val cascadeFile = File(filesDir, cascadeFileName)
        if (!cascadeFile.exists()) {
            try {
                val resourceId = when (cascadeFileName) {
                    "haarcascade_eye.xml" -> R.raw.haarcascade_eye
                    "haarcascade_eye_tree_eyeglasses.xml" -> R.raw.haarcascade_eye_tree_eyeglasses
                    "haarcascade_lefteye_2splits.xml" -> R.raw.haarcascade_lefteye_2splits
                    "haarcascade_righteye_2splits.xml" -> R.raw.haarcascade_righteye_2splits
                    "haarcascade_frontalface_alt.xml" -> R.raw.haarcascade_frontalface_alt
                    else -> throw IllegalArgumentException("Unknown cascade file: $cascadeFileName")
                }
                val inputStream = resources.openRawResource(resourceId)
                val outputStream = FileOutputStream(cascadeFile)
                val buffer = ByteArray(4096)
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }
                inputStream.close()
                outputStream.close()
                Log.d(TAG, "Cascade file copied: $cascadeFileName")
            } catch (e: IOException) {
                Log.e(TAG, "Error copying cascade file: $e")
            }
        }
    }
    private fun switchCamera() {
        currentCameraId = if (currentCameraId == CameraBridgeViewBase.CAMERA_ID_BACK) {
            CameraBridgeViewBase.CAMERA_ID_FRONT
        } else {
            CameraBridgeViewBase.CAMERA_ID_BACK
        }
        cameraBridgeViewBase.disableView()
        cameraBridgeViewBase.setCameraIndex(currentCameraId)
        cameraBridgeViewBase.enableView()
    }
}


