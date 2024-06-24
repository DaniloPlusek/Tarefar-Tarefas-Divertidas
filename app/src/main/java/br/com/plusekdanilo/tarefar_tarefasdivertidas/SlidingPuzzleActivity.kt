package br.com.plusekdanilo.tarefar_tarefasdivertidas

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap

class SlidingPuzzleActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var imageChunks: Array<Bitmap?>
    private lateinit var puzzlePieces: MutableList<ImageView>
    private val gridSize = 3
    private var emptyX = gridSize - 1
    private var emptyY = gridSize - 1
    private lateinit var congratsImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sliding_puzzle)

        gridLayout = findViewById(R.id.gridLayout)
        congratsImageView = findViewById(R.id.congratsImageView)
        puzzlePieces = mutableListOf()
        imageChunks = splitImage(R.drawable.puzzle_image)

        setupGridLayout()
        shufflePuzzle()
    }

    private fun splitImage(imageResId: Int): Array<Bitmap?> {
        val bitmap = BitmapFactory.decodeResource(resources, imageResId)
        val chunkWidth = bitmap.width / gridSize
        val chunkHeight = bitmap.height / gridSize
        val chunks = Array<Bitmap?>(gridSize * gridSize) { null }

        var yCoord = 0
        for (y in 0 until gridSize) {
            var xCoord = 0
            for (x in 0 until gridSize) {
                chunks[y * gridSize + x] = Bitmap.createBitmap(bitmap, xCoord, yCoord, chunkWidth, chunkHeight)
                xCoord += chunkWidth
            }
            yCoord += chunkHeight
        }

        return chunks
    }

    private fun setupGridLayout() {
        gridLayout.removeAllViews()
        gridLayout.columnCount = gridSize
        gridLayout.rowCount = gridSize

        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                val imageView = ImageView(this)
                val params = GridLayout.LayoutParams()
                params.width = 0
                params.height = 0
                params.rowSpec = GridLayout.spec(i, 1f)
                params.columnSpec = GridLayout.spec(j, 1f)
                imageView.layoutParams = params
                imageView.scaleType = ImageView.ScaleType.FIT_XY

                val index = i * gridSize + j
                if (index != (gridSize * gridSize - 1)) {
                    imageView.setImageBitmap(imageChunks[index])
                }

                imageView.setOnClickListener {
                    onTileClicked(i, j)
                }

                puzzlePieces.add(imageView)
                gridLayout.addView(imageView)
            }
        }
    }

    private fun shufflePuzzle() {
        for (i in 0 until 100) {
            val direction = kotlin.random.Random.nextInt(4)
            when (direction) {
                0 -> moveTile(emptyX - 1, emptyY)
                1 -> moveTile(emptyX + 1, emptyY)
                2 -> moveTile(emptyX, emptyY - 1)
                3 -> moveTile(emptyX, emptyY + 1)
            }
        }
    }

    private fun onTileClicked(x: Int, y: Int) {
        if ((x == emptyX && Math.abs(y - emptyY) == 1) || (y == emptyY && Math.abs(x - emptyX) == 1)) {
            moveTile(x, y)
            if (isPuzzleSolved()) {
                showCongratsAndReturn()
            }
        }
    }

    private fun moveTile(x: Int, y: Int) {
        if (x in 0 until gridSize && y in 0 until gridSize) {
            val emptyIndex = emptyX * gridSize + emptyY
            val tileIndex = x * gridSize + y

            val emptyImageView = puzzlePieces[emptyIndex]
            val tileImageView = puzzlePieces[tileIndex]

            emptyImageView.setImageBitmap(tileImageView.drawable.toBitmap())
            tileImageView.setImageBitmap(null)

            emptyX = x
            emptyY = y
        }
    }

    private fun isPuzzleSolved(): Boolean {
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                val index = i * gridSize + j
                val imageView = puzzlePieces[index]
                val expectedBitmap = if (index != gridSize * gridSize - 1) imageChunks[index] else null

                val currentDrawable = imageView.drawable
                val currentBitmap = currentDrawable?.toBitmap()

                if ((currentDrawable == null && index != gridSize * gridSize - 1) ||
                    (currentDrawable != null && currentBitmap != expectedBitmap)) {
                    return false
                }
            }
        }
        return true
    }

    private fun showCongratsAndReturn() {
        congratsImageView.visibility = ImageView.VISIBLE
        Handler().postDelayed({
            congratsImageView.visibility = ImageView.GONE
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }
}