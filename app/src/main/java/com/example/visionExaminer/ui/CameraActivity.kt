package com.example.visionExaminer.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.visionExaminer.R
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.JavaCamera2View
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.CvException
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.MatOfRect
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.core.RotatedRect
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class CameraActivity : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    companion object {
        private const val TAG = "CameraActivity"
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
    private lateinit var cameraBridgeViewBase: CameraBridgeViewBase
    private lateinit var javaCamera2View: JavaCamera2View
    private lateinit var switchCameraButton: FrameLayout
    private lateinit var refreshButton: FrameLayout
    private lateinit var strabismusResultTextView: TextView
    private var face: Rect? = null
    private var leftEye: Rect? = null
    private var rightEye: Rect?= null

    private var leftIris: RotatedRect? = null
    private var rightIris: RotatedRect? = null
    private var leftPupil: PupilData? = null
    private var rightPupil: PupilData? = null
    private var leftGlint: Point? = null
    private var rightGlint: Point? = null
    private var minExpectedEyeArea = 100 // Adjust this value based on your image size and eye size
    private var maxExpectedEyeArea = 1000 // Adjust this value based on your image size and eye size
    private var maxEyeVerticalOffset = 50 // Adjust this value based on your face detection accuracy
    private var currentCameraId = CameraBridgeViewBase.CAMERA_ID_BACK // Default to back camera
    private var strabismusAnalysisDone = false
    private var frameCounter = 0 // Add a frame counter

    private var isNightMode = true
    data class PupilData(
        var ellipse: RotatedRect,
        var lastSeenFrame: Int = 0,
        var smoothedCenter: Point? = null,
        var irisDiameter: Double? = null
    )

    private enum class DetectionStage {
        FACE, EYES, IRISES, PUPILS, GLINTS, ANALYSIS
    }
    private var currentStage = DetectionStage.FACE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isNightMode = intent.getBooleanExtra("isNightMode", true) // Получение isNightMode
        // Устанавливаем тему
        if (isNightMode) {
            setTheme(R.style.AppTheme_Night)
        } else {
            setTheme(R.style.AppTheme_Day)
        }
        // Устанавливаем цвет фона в зависимости от темы
        val backgroundColor = if (isNightMode) {
            ContextCompat.getColor(this, R.color.black)
        } else {
            ContextCompat.getColor(this, R.color.white)
        }
        setContentView(R.layout.activity_camera)
        supportActionBar?.hide()
        window.decorView.setBackgroundColor(backgroundColor)

        javaCamera2View = findViewById(R.id.javaCam2View)
        switchCameraButton = findViewById(R.id.switchCameraButton)
        refreshButton = findViewById(R.id.refreshButton)
        strabismusResultTextView = findViewById(R.id.strabismusResultTextView)

        switchCameraButton.setOnClickListener {
            switchCamera()
        }
        refreshButton.setOnClickListener {
            resetDetectionStage() // Reset the detection stage
            // You may also want to clear any visualizations here
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
        frameCounter++
        val frame = inputFrame.rgba()
        Log.d(TAG, "Frame size: ${frame.cols()}x${frame.rows()}")

        if (currentCameraId == CameraBridgeViewBase.CAMERA_ID_FRONT) {
            Core.flip(frame, frame, 1) // Flip horizontally
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
                if ((leftEye != null && leftIris != null) || (rightEye != null && rightIris != null)) {
                    currentStage = DetectionStage.IRISES
                }
            }
            DetectionStage.IRISES -> {
                if (leftEye != null) {
                    leftIris = detectIris(frame.submat(leftEye!!), true)
                }
                if (rightEye != null) {
                    rightIris = detectIris(frame.submat(rightEye!!), false)
                }
                if ((leftIris != null && leftPupil != null) || (rightIris != null && rightPupil != null)) {
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
            // Error handling:
            // Option 1: Display error message to the user
            // Option 2: Retry the conversion
            return
        }

        val faces = MatOfRect()
        val faceCascade = CascadeClassifier(File(filesDir, "haarcascade_frontalface_alt.xml").absolutePath)
        faceCascade.detectMultiScale(grayFrame, faces)

        if (faces.toArray().isNotEmpty()) {
            face = faces.toArray()[0]
            currentStage = DetectionStage.EYES
        } else {
            face = null // Set face to null if no face detected
            Log.d(TAG, "Face not detected in current frame.") // Additional logging
        }
    }

    private fun detectEyes(frame: Mat) {
        if (face == null) return
        val grayFrame = Mat()
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY)
        val eyes = MatOfRect()

        // **Create a region of interest (ROI) for the upper half of the face**
        val upperFaceRoi = Rect(
            face!!.x,
            face!!.y,
            face!!.width,
            face!!.height / 2 // Take only the upper half
        )

        // Use more specific cascade files for individual eye detection within the ROI
        for (cascadeFile in listOf("haarcascade_lefteye_2splits.xml", "haarcascade_righteye_2splits.xml")) {
            val eyeCascade = CascadeClassifier(File(filesDir, cascadeFile).absolutePath)
            eyeCascade.detectMultiScale(grayFrame.submat(upperFaceRoi), eyes)
            // ^^^^ Pass the upperFaceRoi here
            if (eyes.toArray().isNotEmpty()) break
        }

        when (eyes.toArray().size) {
            2 -> {
                val eyeRects = eyes.toArray()

                // Considering front camera mirroring:
                if (currentCameraId == CameraBridgeViewBase.CAMERA_ID_FRONT) {
                    leftEye = if (eyeRects[0].x < eyeRects[1].x) eyeRects[0] else eyeRects[1] // Left eye is on the left side of the mirrored image
                    rightEye = if (eyeRects[0].x > eyeRects[1].x) eyeRects[0] else eyeRects[1] // Right eye is on the right side of the mirrored image
                } else {
                    // Logic for back camera (original code)
                    leftEye = if (eyeRects[0].x > eyeRects[1].x) eyeRects[0] else eyeRects[1] // Left eye is on the right side of the image
                    rightEye = if (eyeRects[0].x < eyeRects[1].x) eyeRects[0] else eyeRects[1] // Right eye is on the left side of the image
                }

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

                currentStage = DetectionStage.IRISES
            }
            else -> {
                // Handle cases with no eyes or more than two eyes detected
                Log.w(TAG, "Unexpected number of eyes detected: ${eyes.toArray().size}")
                // You might want to reset the detection stage or implement other logic here
            }
        }
    }
    private fun detectIris(eyeMat: Mat, isLeftEye: Boolean): RotatedRect? {
        if (eyeMat.empty()) return null
        val grayEye = Mat()
        Imgproc.cvtColor(eyeMat, grayEye, Imgproc.COLOR_BGR2GRAY)

        // 1. Apply adaptive thresholding to segment the iris
        val thresh = Mat()
        Imgproc.adaptiveThreshold(grayEye, thresh, 255.0, Imgproc.ADAPTIVE_THRESH_MEAN_C,
            Imgproc.THRESH_BINARY_INV, 21, 10.0)

        // 2. Find contours in the thresholded image
        val contours = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)
        if (contours.isEmpty()) return null

        // 3. Filter contours based on their location and size within the eye region
        val filteredContours = contours.filter { contour ->
            val rect = Imgproc.boundingRect(contour)
            val area = Imgproc.contourArea(contour)
            // Check if the contour's bounding rectangle is within the eye region AND
            // check if the area is within expected range (to avoid detecting eyelids or other features)
            rect.x >= 0 && rect.y >= 0 && rect.x + rect.width <= eyeMat.cols() &&
                    rect.y + rect.height <= eyeMat.rows() &&
                    area >= minExpectedEyeArea && area <= maxExpectedEyeArea
        }
        if (filteredContours.isEmpty()) return null

        // 4. Select the contour with the largest area as the potential iris
        var largestContour: MatOfPoint? = null
        var largestArea = 0.0
        for (contour in filteredContours) {
            val area = Imgproc.contourArea(contour)
            if (area > largestArea) {
                largestArea = area
                largestContour = contour
            }
        }
        if (largestContour == null) return null

        // 5. Fit an ellipse to the largest contour to approximate the iris shape
        val ellipse = Imgproc.fitEllipse(MatOfPoint2f(*largestContour.toArray()))

        // Adjust iris center coordinates relative to the face region
        val eyeRect = if (isLeftEye) leftEye else rightEye  // Get the corresponding eye rectangle
        ellipse.center.x += eyeRect!!.x
        ellipse.center.y += eyeRect.y

        // Add stage progression logic after successful iris detection
        if (isLeftEye && leftIris != null) {
            currentStage = DetectionStage.PUPILS
        } else if (!isLeftEye && rightIris != null) {
            currentStage = DetectionStage.PUPILS
        }

        return ellipse
    }

    private fun detectPupils(frame: Mat) {
        if (leftEye == null && rightEye == null) return
        val grayFrame = Mat()
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY)

        if (leftEye != null) {
            val detectedPupilEllipse = detectPupilEllipse(grayFrame.submat(leftEye!!), true)
            leftPupil?.let {
                it.ellipse = detectedPupilEllipse ?: it.ellipse
                it.lastSeenFrame = frameCounter
                smoothPupilCenter(it, detectedPupilEllipse?.center ?: it.ellipse.center)
                it.irisDiameter = leftIris?.let { iris -> (iris.size.width + iris.size.height) / 2.0 }
            } ?: run {
                leftPupil = detectedPupilEllipse?.let { PupilData(it) }
            }
        }

        if (rightEye != null) {
            val detectedPupilEllipse = detectPupilEllipse(grayFrame.submat(rightEye!!), false)
            rightPupil?.let {
                it.ellipse = detectedPupilEllipse ?: it.ellipse
                it.lastSeenFrame = frameCounter
                smoothPupilCenter(it, detectedPupilEllipse?.center ?: it.ellipse.center)
                it.irisDiameter = rightIris?.let { iris -> (iris.size.width + iris.size.height) / 2.0 }
            } ?: run {
                rightPupil = detectedPupilEllipse?.let { PupilData(it) }
            }
        }

        if ((leftPupil != null && leftGlint != null) || (rightPupil != null && rightGlint != null)) {
            currentStage = DetectionStage.GLINTS
        }
    }

    private fun detectPupilEllipse(pupilMat: Mat, isLeftEye: Boolean): RotatedRect? {
        if (pupilMat.empty()) return null
        Log.d(TAG, "pupilMat channels: ${pupilMat.channels()}") // Verify it's 1 channel (grayscale)
        // No conversion needed:
        val grayPupil = pupilMat.clone() // Create a copy for further processing


        // 1. Adaptive Thresholding
        val thresh = Mat()
        Imgproc.adaptiveThreshold(grayPupil, thresh, 255.0, Imgproc.ADAPTIVE_THRESH_MEAN_C,
            Imgproc.THRESH_BINARY_INV, 21, 10.0)

        // 2. Find Contours
        val contours = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)
        if (contours.isEmpty()) return null

        // 3. Filter Contours based on size and location
        val filteredContours = contours.filter { contour ->
            val rect = Imgproc.boundingRect(contour)
            val area = Imgproc.contourArea(contour)
            rect.x >= 0 && rect.y >= 0 && rect.x + rect.width <= pupilMat.cols() &&
                    rect.y + rect.height <= pupilMat.rows() &&
                    area >= minExpectedEyeArea && area <= maxExpectedEyeArea
        }
        if (filteredContours.isEmpty()) return null

        // 4. Select Contour with Largest Area
        var largestContour: MatOfPoint? = null
        var largestArea = 0.0
        for (contour in filteredContours) {
            val area = Imgproc.contourArea(contour)
            if (area > largestArea) {
                largestArea = area
                largestContour = contour
            }
        }
        if (largestContour == null) return null

        // 5. Fit Ellipse to the Contour
        val ellipse = Imgproc.fitEllipse(MatOfPoint2f(*largestContour.toArray()))

        // 6. Adjust Center Coordinates relative to Face Region
        val eyeRect = if (isLeftEye) leftEye else rightEye
        ellipse.center.x += eyeRect!!.x
        ellipse.center.y += eyeRect.y

        return ellipse
    }
    private fun smoothPupilCenter(pupilData: PupilData, newCenter: Point): Point {
        val smoothingFactor = 0.2  // Adjust as needed
        pupilData.smoothedCenter = if (pupilData.smoothedCenter == null) {
            newCenter
        } else {
            Point(
                pupilData.smoothedCenter!!.x * (1 - smoothingFactor) + newCenter.x * smoothingFactor,
                pupilData.smoothedCenter!!.y * (1 - smoothingFactor) + newCenter.y * smoothingFactor
            )
        }
        return pupilData.smoothedCenter!!
    }

    private fun detectGlints(frame: Mat) {
        if (leftPupil == null && rightPupil == null) return
        val grayFrame = Mat()
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY)

        if (leftPupil != null && leftEye != null) {
            val leftPupilRect = leftPupil!!.ellipse.boundingRect()
            val leftPupilMat = frame.submat(leftPupilRect.y, leftPupilRect.y + leftPupilRect.height,
                leftPupilRect.x, leftPupilRect.x + leftPupilRect.width)
            val glint = detectGlintBlob(leftPupilMat)
            if (glint != null) {
                leftGlint = glint
                // Adjust glint coordinates based on pupil rectangle offset
                val center = Point(glint.x + leftEye!!.x + leftPupilRect.x,
                    glint.y + leftEye!!.y + leftPupilRect.y)
                Imgproc.circle(frame, center, 3, Scalar(255.0, 0.0, 255.0), -1)
            }
        }

        if (rightPupil != null && rightEye != null) {
            val rightPupilRect = rightPupil!!.ellipse.boundingRect()
            val rightPupilMat = frame.submat(rightPupilRect.y, rightPupilRect.y + rightPupilRect.height,
                rightPupilRect.x, rightPupilRect.x + rightPupilRect.width)
            val glint = detectGlintBlob(rightPupilMat)
            if (glint != null) {
                rightGlint = glint
                // Adjust glint coordinates based on pupil rectangle offset
                val center = Point(glint.x + rightEye!!.x + rightPupilRect.x,
                    glint.y + rightEye!!.y + rightPupilRect.y)
                Imgproc.circle(frame, center, 3, Scalar(255.0, 0.0, 255.0), -1)
            }
        }

        if ((leftPupil != null && leftGlint != null) || (rightPupil != null && rightGlint != null)) {
            currentStage = DetectionStage.ANALYSIS
        }
    }

    private fun detectGlintBlob(grayPupil: Mat): Point? {
        if (grayPupil.empty()) {
            Log.w(TAG, "Empty grayPupil Mat passed to detectGlintBlob")
            return null
        }

        // Обеспечиваем grayscale и правильный тип данных (CV_8UC1)
        val processedPupil = Mat()
        if (grayPupil.channels() > 1) {
            Imgproc.cvtColor(grayPupil, processedPupil, Imgproc.COLOR_BGR2GRAY)
        } else {
            grayPupil.copyTo(processedPupil) // Избегаем совместного использования данных
        }
        processedPupil.convertTo(processedPupil, CvType.CV_8UC1)

        // Проверяем размер изображения
        if (processedPupil.rows() == 0 || processedPupil.cols() == 0) {
            Log.w(TAG, "Invalid image size in detectGlintBlob")
            return null
        }

        // 1. Адаптивная пороговая обработка (настройте параметры по необходимости)
        val binaryPupil = Mat()
        Imgproc.adaptiveThreshold(processedPupil, binaryPupil, 255.0,
            Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
            Imgproc.THRESH_BINARY, 15, 5.0)

        // 2. Морфологические операции (настройте параметры по необходимости)
        val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, Size(2.0, 2.0))
        Imgproc.morphologyEx(binaryPupil, binaryPupil, Imgproc.MORPH_CLOSE, kernel)

        // 3. Поиск контуров
        val contours = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(binaryPupil, contours, hierarchy,
            Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)
        if (contours.isEmpty()) {
            Log.w(TAG, "No contours found in pupil region")
            return null
        }

        // 4. Фильтрация контуров по площади и окружности (настройте пороги по необходимости)
        val minArea = 5.0    // Измените на Double
        val maxArea = 50.0   // Измените на Double
        val minCircularity = 0.7
        val filteredContours = contours.filter { contour ->
            val area = Imgproc.contourArea(contour)
            val perimeter = Imgproc.arcLength(MatOfPoint2f(*contour.toArray()), true)
            val circularity = 4 * Math.PI * area / (perimeter * perimeter)
            area in minArea..maxArea && circularity >= minCircularity
        }
        if (filteredContours.isEmpty()) {
            Log.w(TAG, "No suitable contours (glints) found after filtering")
            return null
        }

        // 5. Выбор контура с наибольшей площадью (скорее всего, блик)
        var largestContour: MatOfPoint? = null
        var largestArea = 0.0
        for (contour in filteredContours) {
            val area = Imgproc.contourArea(contour)
            if (area > largestArea) {
                largestArea = area
                largestContour = contour
            }
        }

        // 6. Находим центр контура (приблизительное положение блика)
        val moments = Imgproc.moments(largestContour!!)
        val centerX = moments.m10 / moments.m00
        val centerY = moments.m01 / moments.m00

        return Point(centerX, centerY)
    }
    @SuppressLint("SetTextI18n")
    private fun analyzeGlints() {
        if (leftGlint == null || rightGlint == null || leftPupil == null || rightPupil == null || strabismusAnalysisDone) return

        val leftPupilCenter = leftPupil!!.ellipse.center
        val rightPupilCenter = rightPupil!!.ellipse.center
        val leftPupilWidth = leftPupil!!.ellipse.size.width
        val rightPupilWidth = rightPupil!!.ellipse.size.width

        // Calculate relative glint positions (horizontal and vertical)
        val leftRelativeGlintX = leftGlint!!.x - leftPupilCenter.x
        val leftRelativeGlintY = leftGlint!!.y - leftPupilCenter.y
        val rightRelativeGlintX = rightGlint!!.x - rightPupilCenter.x
        val rightRelativeGlintY = rightGlint!!.y - rightPupilCenter.y

        // Calculate distances from center and normalized distances (horizontal)
        val leftDistanceFromCenter = sqrt(leftRelativeGlintX * leftRelativeGlintX + leftRelativeGlintY * leftRelativeGlintY)
        val rightDistanceFromCenter = sqrt(rightRelativeGlintX * rightRelativeGlintX + rightRelativeGlintY * rightRelativeGlintY)
        val leftPupilRadius = leftPupilWidth / 2.0  // Use width as an approximation for radius
        val rightPupilRadius = rightPupilWidth / 2.0 // Use width as an approximation for radius
        val leftNormalizedDistance = leftDistanceFromCenter / leftPupilRadius
        val rightNormalizedDistance = rightDistanceFromCenter / rightPupilRadius

        // Approximate horizontal angles
        val leftAngle = approximateAngle(leftNormalizedDistance, leftGlint!!, leftPupilCenter, true)
        val rightAngle = approximateAngle(rightNormalizedDistance, rightGlint!!, rightPupilCenter, false)

        // Threshold for vertical deviation (adjust based on your image resolution and pupil size)
        val thresholdVertical = 10 // Example threshold - adjust as needed

        // Determine vertical strabismus type for each eye
        val leftVerticalStrabismus = when {
            leftRelativeGlintY > thresholdVertical -> "Гипертропия (вверх)"
            leftRelativeGlintY < -thresholdVertical -> "Гипотропия (вниз)"
            else -> "Нет вертикального отклонения"
        }
        val rightVerticalStrabismus = when {
            rightRelativeGlintY > thresholdVertical -> "Гипертропия (вверх)"
            rightRelativeGlintY < -thresholdVertical -> "Гипотропия (вниз)"
            else -> "Нет вертикального отклонения"
        }

        // Check for both eyes having the same vertical deviation
        val bothEyesHypotropia = leftVerticalStrabismus == "Гипотропия (вниз)" && rightVerticalStrabismus == "Гипотропия (вниз)"
        val bothEyesHypertropia = leftVerticalStrabismus == "Гипертропия (вверх)" && rightVerticalStrabismus == "Гипертропия (вверх)"

        // Combine horizontal and vertical strabismus results with special messages
        val strabismusType = when {
            bothEyesHypotropia -> "Пациент смотрит вниз - перенаправьте взгляд пациента в камеру"
            bothEyesHypertropia -> "Пациент смотрит вверх - перенаправьте взгляд пациента в камеру"
            abs(leftAngle) < 5 && abs(rightAngle) < 5 -> {
                if (leftVerticalStrabismus == "Нет вертикального отклонения" && rightVerticalStrabismus == "Нет вертикального отклонения") {
                    "Ортофория (нет косоглазия)"
                } else {
                    "$leftVerticalStrabismus (левый глаз), $rightVerticalStrabismus (правый глаз)"
                }
            }
            leftAngle > 5 && rightAngle > 5 ->  // Esotropia
                "Эзотропия (сходящееся косоглазие) - ${max(leftAngle, rightAngle)}°" +
                        if (leftVerticalStrabismus != "Нет вертикального отклонения" || rightVerticalStrabismus != "Нет вертикального отклонения") {
                            ", $leftVerticalStrabismus (левый глаз), $rightVerticalStrabismus (правый глаз)"
                        } else {
                            ""
                        }
            leftAngle < -5 && rightAngle < -5 -> // Exotropia
                "Экзотропия (расходящееся косоглазие) - ${min(leftAngle, rightAngle)}°" +
                        if (leftVerticalStrabismus != "Нет вертикального отклонения" || rightVerticalStrabismus != "Нет вертикального отклонения") {
                            ", $leftVerticalStrabismus (левый глаз), $rightVerticalStrabismus (правый глаз)"
                        } else {
                            ""
                        }
            else ->  // Combined horizontal and vertical deviations (one eye eso, the other exo)
                "$leftVerticalStrabismus (левый глаз), $rightVerticalStrabismus (правый глаз)"
        }

        Log.d(TAG, "Strabismus Analysis: $strabismusType")

        // Display or store results as needed (example: update TextView)
        runOnUiThread {
            strabismusResultTextView.text = "Strabismus Analysis: $strabismusType"
        }

        strabismusAnalysisDone = true
    }
    private fun approximateAngle(normalizedDistance: Double, glintPosition: Point, pupilCenter: Point, isLeftEye: Boolean): Double {
        val glintXRelativeToCenter = glintPosition.x - pupilCenter.x
        val glintYRelativeToCenter = glintPosition.y - pupilCenter.y

        // Determine quadrant based on glint position and eye (left/right)
        val quadrant = when {
            glintXRelativeToCenter >= 0 && glintYRelativeToCenter <= 0 && isLeftEye -> 1  // Upper right quadrant in left eye
            glintXRelativeToCenter <= 0 && glintYRelativeToCenter <= 0 && isLeftEye -> 2  // Upper left quadrant in left eye
            glintXRelativeToCenter <= 0 && glintYRelativeToCenter >= 0 && isLeftEye -> 3  // Lower left quadrant in left eye
            glintXRelativeToCenter >= 0 && glintYRelativeToCenter >= 0 && isLeftEye -> 4  // Lower right quadrant in left eye
            glintXRelativeToCenter <= 0 && glintYRelativeToCenter <= 0 && !isLeftEye -> 1  // Upper left quadrant in right eye (mirrored)
            glintXRelativeToCenter >= 0 && glintYRelativeToCenter <= 0 && !isLeftEye -> 2  // Upper right quadrant in right eye (mirrored)
            glintXRelativeToCenter >= 0 && glintYRelativeToCenter >= 0 && !isLeftEye -> 3  // Lower right quadrant in right eye (mirrored)
            else -> 4  // Lower left quadrant in right eye (mirrored)
        }

        // Approximate angle using more granular ranges or a mapping function
        val baseAngle = when (normalizedDistance) {
            in 0.0..0.1 -> 0.0
            in 0.1..0.2 -> 7.5  // Adjust these values based on the Hirschberg charts
            in 0.2..0.3 -> 15.0
            in 0.3..0.4 -> 22.5
            in 0.4..0.5 -> 30.0
            in 0.5..0.6 -> 37.5
            in 0.6..0.7 -> 45.0
            else -> 60.0
        }

        // Adjust angle sign based on quadrant and eye
        val angle = when (quadrant) {
            1 ->  baseAngle   // Esotropia (inward) for both eyes
            2 -> -baseAngle   // Exotropia (outward) for both eyes
            3 -> -baseAngle   // Exotropia (outward) for both eyes
            else ->  baseAngle   // Esotropia (inward) for both eyes
        }

        return angle
    }




    private fun visualizeResults(frame: Mat) {
        if (face != null) {
            Imgproc.rectangle(frame, face!!.tl(), face!!.br(), Scalar(255.0, 255.0, 0.0), 2) // Yellow rectangle for face
        } else {
            Imgproc.putText(
                frame, "Лицо не обнаружено. Направьте камеру на лицо и нажмите Refresh",
                Point(10.0, 50.0), Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, Scalar(255.0, 0.0, 0.0), 2
            )
        }

        if (leftEye != null) {
            // Обновленное отображение для левого глаза
            val centerL = Point(leftEye!!.x + leftEye!!.width / 2.0, leftEye!!.y + leftEye!!.height / 2.0)
            val radiusL = (leftEye!!.width + leftEye!!.height) / 5.0  // Adjust radius as needed
            Imgproc.circle(frame, centerL, radiusL.toInt(), Scalar(0.0, 0.0, 255.0), 2)
            Imgproc.putText(frame, "L", Point(leftEye!!.x - 10.0, leftEye!!.y - 10.0), Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, Scalar(0.0, 0.0, 255.0), 2)
        }

        if (rightEye != null) {
            // Обновленное отображение для правого глаза
            val centerR = Point(rightEye!!.x + rightEye!!.width / 2.0, rightEye!!.y + rightEye!!.height / 2.0)
            val radiusR = (rightEye!!.width + rightEye!!.height) / 5.0 // Adjust radius as needed
            Imgproc.circle(frame, centerR, radiusR.toInt(), Scalar(0.0, 0.0, 255.0), 2)
            Imgproc.putText(frame, "R", Point(rightEye!!.x - 10.0, rightEye!!.y - 10.0), Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, Scalar(0.0, 0.0, 255.0), 2)
        }

        if ((leftEye == null || leftPupil == null) && (rightEye == null || rightPupil == null)) {
            Imgproc.putText(frame, "Глаз не обнаружено", Point(10.0, 100.0), Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, Scalar(255.0, 0.0, 0.0), 2)
        }

        if (leftEye != null) {
            val leftIris = detectIris(frame.submat(leftEye!!), true) // Correct: isLeftEye = true
            if (leftIris != null) {
                Imgproc.ellipse(frame, leftIris, Scalar(0.0, 255.0, 0.0), 2) // Green ellipse for left iris
            }
        }

        if (rightEye != null) {
            val rightIris = detectIris(frame.submat(rightEye!!), false) // Correct: isLeftEye = false
            if (rightIris != null) {
                Imgproc.ellipse(frame, rightIris, Scalar(0.0, 255.0, 0.0), 2) // Green ellipse for left iris
            }
        }

        if (leftEye != null && leftPupil != null && frameCounter - leftPupil!!.lastSeenFrame < 5) {
            val smoothedEllipse = RotatedRect(leftPupil!!.smoothedCenter!!, leftPupil!!.ellipse.size, leftPupil!!.ellipse.angle)
            val pupilRadius = leftPupil!!.irisDiameter?.let { it * 0.4 }?.toInt() ?: 5
            Imgproc.circle(frame, smoothedEllipse.center, pupilRadius, Scalar(255.0, 255.0, 0.0), 2)
        }

        if (rightEye != null && rightPupil != null && frameCounter - rightPupil!!.lastSeenFrame < 5) {
            val smoothedEllipse = RotatedRect(rightPupil!!.smoothedCenter!!, rightPupil!!.ellipse.size, rightPupil!!.ellipse.angle)
            val pupilRadius = rightPupil!!.irisDiameter?.let { it * 0.4 }?.toInt() ?: 5
            Imgproc.circle(frame, smoothedEllipse.center, pupilRadius, Scalar(255.0, 255.0, 0.0), 2)
        }

        if ((leftPupil == null || leftGlint == null) && (rightPupil == null || rightGlint == null)) {
            Imgproc.putText(frame, "Зрачки не найдены", Point(10.0, 150.0), Imgproc.FONT_HERSHEY_SIMPLEX, 1.0, Scalar(255.0, 0.0, 0.0), 2)
        }

        if (leftGlint != null) {
            val center = Point(leftGlint!!.x + leftEye!!.x, leftGlint!!.y + leftEye!!.y)
            val lineStart = Point(center.x, center.y - leftEye!!.height / 4)
            val lineEnd = Point(center.x, center.y + leftEye!!.height / 4)
            Imgproc.line(frame, lineStart, lineEnd, Scalar(255.0, 0.0, 255.0), 2)
        }

        if (rightGlint != null) {
            val center = Point(rightGlint!!.x + rightEye!!.x, rightGlint!!.y + rightEye!!.y)
            val lineStart = Point(center.x, center.y - rightEye!!.height / 4)
            val lineEnd = Point(center.x, center.y + rightEye!!.height / 4)
            Imgproc.line(frame, lineStart, lineEnd, Scalar(255.0, 0.0, 255.0), 2)
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

            // Reset detection stage when switching cameras
            resetDetectionStage()
        }
    private fun resetDetectionStage() {
        currentStage = DetectionStage.FACE
        face = null
        leftEye = null
        rightEye = null
        leftIris = null
        rightIris = null
        leftPupil = null
        rightPupil = null
        leftGlint = null
        rightGlint = null
        strabismusAnalysisDone = false // Allow a new analysis after refresh
        strabismusResultTextView.text = ""
        // Optionally clear visualizations on the frame here
    }
}


