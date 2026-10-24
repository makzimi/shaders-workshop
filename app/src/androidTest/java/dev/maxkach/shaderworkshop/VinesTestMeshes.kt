package dev.maxkach.shaderworkshop

import android.graphics.ColorSpace
import android.graphics.Mesh
import android.graphics.MeshSpecification
import android.graphics.RectF
import android.os.Build
import androidx.annotation.RequiresApi
import dev.maxkach.shaderworkshop.exercise2.start.vines.VineVertexStrideBytes
import dev.maxkach.shaderworkshop.exercise2.start.vines.VinesCount
import dev.maxkach.shaderworkshop.exercise2.start.vines.packVineVertices
import dev.maxkach.shaderworkshop.exercise2.start.vines.walkVineCenterline
import java.nio.ByteBuffer
import java.nio.ByteOrder

/** Builds the vine meshes like the app does, but from any vertex shader source. */
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class VinesTestMeshes(vertexShader: String, fragmentShader: String, width: Float, height: Float, progress: Float) {
    private val specification = MeshSpecification.make(
        arrayOf(
            MeshSpecification.Attribute(MeshSpecification.TYPE_FLOAT2, 0, "position"),
            MeshSpecification.Attribute(MeshSpecification.TYPE_FLOAT2, 8, "prev"),
            MeshSpecification.Attribute(MeshSpecification.TYPE_FLOAT2, 16, "next"),
            MeshSpecification.Attribute(MeshSpecification.TYPE_FLOAT2, 24, "meta"),
        ),
        VineVertexStrideBytes,
        emptyArray(),
        vertexShader,
        fragmentShader,
        ColorSpace.get(ColorSpace.Named.SRGB),
        MeshSpecification.ALPHA_TYPE_PREMULTIPLIED,
    )
    private val buffers = mutableListOf<ByteBuffer>()

    val meshes: List<Mesh> = (0 until VinesCount).map { vine ->
        val line = walkVineCenterline(vine, 1337u, width, height)
        val vertices = packVineVertices(line)
        val buffer = ByteBuffer.allocateDirect(vertices.size * Float.SIZE_BYTES).order(ByteOrder.nativeOrder())
        buffer.asFloatBuffer().put(vertices)
        buffers += buffer
        Mesh(specification, Mesh.TRIANGLE_STRIP, buffer, line.count * 2, RectF(-80f, -80f, width + 80f, height + 80f)).apply {
            setFloatUniform("screenSize", width, height)
            setFloatUniform("mainWidth", 80f)
            setFloatUniform("pointCount", line.count.toFloat())
            setFloatUniform("progress", progress)
        }
    }
}
