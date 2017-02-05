package ch.fhnw.afpars.algorithm.informationsegmentation

import ch.fhnw.afpars.algorithm.AlgorithmParameter
import ch.fhnw.afpars.algorithm.IAlgorithm
import ch.fhnw.afpars.model.AFImage
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

/**
 * Created by cansik on 13.10.16.
 */
class MorphologicalTransform() : IAlgorithm {
    companion object {
        val MORPH = "MorphTransform"
    }

    override val name: String
        get() = "Morphological Transform"

    @AlgorithmParameter(name = "Threshold", minValue = 0.0, maxValue = 255.0)
    var treshold = 200.0

    @AlgorithmParameter(name = "Opening", minValue = 0.0, maxValue = 20.0)
    var openingSize = 9

    @AlgorithmParameter(name = "Closing")
    var closingSize = 12

    constructor(threshold: Double, openingSize: Int, closingSize: Int) : this() {
        this.treshold = threshold
        this.openingSize = openingSize
        this.closingSize = closingSize
    }

    override fun run(image: AFImage, history: MutableList<AFImage>): AFImage {
        val img = image.clone()

        threshold(img.image, treshold)

        // opening
        dilate(img.image, openingSize)
        erode(img.image, openingSize)

        // closing
        erode(img.image, closingSize)
        dilate(img.image, closingSize)
        image.attributes.put(MORPH,img.image)
        history.add(img)
        return image
    }

    fun erode(img: Mat, erosionSize: Int) {
        val element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(2.0 * erosionSize + 1.0, 2.0 * erosionSize + 1.0))
        Imgproc.erode(img, img, element)
    }

    fun dilate(img: Mat, dilationSize: Int) {
        val element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(2.0 * dilationSize + 1.0, 2.0 * dilationSize + 1.0))
        Imgproc.dilate(img, img, element)
    }

    fun threshold(img: Mat, treshold: Double, maxValue: Double, type: Int) {
        Imgproc.threshold(img, img, treshold, maxValue, type)
    }

    fun threshold(img: Mat, treshold: Double) {
        this.threshold(img, treshold, 255.0, Imgproc.THRESH_BINARY)
    }
}