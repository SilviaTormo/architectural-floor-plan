package ch.fhnw.afpars.ui.control.editor.shapes

import ch.fhnw.afpars.util.format
import ch.fhnw.afpars.util.toCvPoint
import ch.fhnw.afpars.util.toCvScalar
import javafx.geometry.Dimension2D
import javafx.geometry.Point2D
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import org.jfree.graphics2d.svg.SVGGraphics2D
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

/**
 * Created by cansik on 14.02.17.
 */
class PolygonShape() : BaseShape() {
    var points : MutableList<Point2D> = mutableListOf()

    constructor(points : MutableList<Point2D> = mutableListOf<Point2D>()) : this() {
        this.points = points
    }

    override fun render(gc: GraphicsContext) {
        gc.fillPolygon(points.map { it.x }.toDoubleArray(), points.map { it.y }.toDoubleArray(), points.size)
        gc.strokePolygon(points.map { it.x }.toDoubleArray(), points.map { it.y }.toDoubleArray(), points.size)
    }

    override fun renderToSvg(g: SVGGraphics2D) {
        super.renderToSvg(g)
        g.drawPolygon(points.map { it.x.toInt() }.toIntArray(), points.map { it.y.toInt() }.toIntArray(), points.size)
    }

    override fun renderToMat(m: Mat) {
        super.renderToMat(m)

        // not implemented
    }

    override fun toString(): String {
        return "Poly (${getSize().format(0)} px)"
    }

    fun getSize() : Double
    {
        return polygonArea(points.map { it.x }.toDoubleArray(), points.map { it.y }.toDoubleArray(), points.size)
    }

    fun polygonArea(x : DoubleArray, y : DoubleArray, npoints : Int) : Double
    {
        var area = 0.0
        var j = npoints - 1
        var i = 0

        while(i < npoints)
        {
            area += (x[j]+x[i]) * (y[j]-y[i])
            j = i
            i++
        }

        return area / 2.0
    }
}