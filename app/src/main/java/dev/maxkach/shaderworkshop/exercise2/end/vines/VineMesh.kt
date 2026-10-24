package dev.maxkach.shaderworkshop.exercise2.end.vines

import android.graphics.ColorSpace
import android.graphics.Mesh
import android.graphics.MeshSpecification
import android.graphics.RectF
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.geometry.Size
import java.nio.ByteBuffer
import java.nio.ByteOrder

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
class VineMesh(val mesh: Mesh, @Suppress("unused") private val vertices: ByteBuffer)

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun vinesMeshSpecification(): MeshSpecification = MeshSpecification.make(
    arrayOf(
        MeshSpecification.Attribute(MeshSpecification.TYPE_FLOAT2, 0, "position"),
        MeshSpecification.Attribute(MeshSpecification.TYPE_FLOAT2, 8, "prev"),
        MeshSpecification.Attribute(MeshSpecification.TYPE_FLOAT2, 16, "next"),
        MeshSpecification.Attribute(MeshSpecification.TYPE_FLOAT2, 24, "meta"),
    ),
    VineVertexStrideBytes,
    emptyArray(),
    VINES_VERTEX_SHADER,
    VINES_FRAGMENT_SHADER,
    ColorSpace.get(ColorSpace.Named.SRGB),
    MeshSpecification.ALPHA_TYPE_PREMULTIPLIED,
)

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun buildVineMeshes(specification: MeshSpecification, seed: UInt, size: Size): List<VineMesh> {
  if (size.width <= 0f || size.height <= 0f) return emptyList()

  val bounds = RectF(-MainWidthPx, -MainWidthPx, size.width + MainWidthPx, size.height + MainWidthPx)
  return (0 until VinesCount).map { vine ->
    val line = walkVineCenterline(vine, seed, size.width, size.height)
    val vertices = packVineVertices(line)
    val buffer = ByteBuffer.allocateDirect(vertices.size * Float.SIZE_BYTES).order(ByteOrder.nativeOrder())
    buffer.asFloatBuffer().put(vertices)
    val mesh = Mesh(specification, Mesh.TRIANGLE_STRIP, buffer, line.count * 2, bounds).apply {
      setFloatUniform("screenSize", size.width, size.height)
      setFloatUniform("mainWidth", MainWidthPx)
      setFloatUniform("pointCount", line.count.toFloat())
      setFloatUniform("progress", 0f)
    }
    VineMesh(mesh, buffer)
  }
}

const val MainWidthPx = 80f

const val VineVertexFloats = 8
const val VineVertexStrideBytes = VineVertexFloats * Float.SIZE_BYTES

fun packVineVertices(line: VineCenterline): FloatArray {
  val out = FloatArray(line.count * 2 * VineVertexFloats)
  var o = 0
  for (i in 0 until line.count) {
    val prev = maxOf(i - 1, 0)
    val next = minOf(i + 1, line.count - 1)
    for (side in floatArrayOf(1f, -1f)) {
      out[o++] = line.x(i)
      out[o++] = line.y(i)
      out[o++] = line.x(prev)
      out[o++] = line.y(prev)
      out[o++] = line.x(next)
      out[o++] = line.y(next)
      out[o++] = i.toFloat()
      out[o++] = side
    }
  }
  return out
}
