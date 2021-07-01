package com.gotti.memoit.fragments

import android.R.attr.bitmap
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gotti.memoit.R
import com.gotti.memoit.databinding.FragmentEditPictureBinding
import com.gotti.memoit.others.Constants
import com.gotti.memoit.others.StickerPictureRecyclerViewAdapter
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class EditPictureFragment : Fragment(), StickerPictureRecyclerViewAdapter.onItemClickListener {

    //for binding
    private var _binding: FragmentEditPictureBinding? = null
    private val binding get() = _binding!!

    private lateinit var safeContext: Context
    val images = ArrayList<Int>()

    companion object{
        var factor = 1.0f
        lateinit var view1: View
    }

    lateinit var SGD_1: ScaleGestureDetector

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentEditPictureBinding.inflate(inflater, container, false)
        val view = binding.root
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        SGD_1 = ScaleGestureDetector(safeContext, ScaleListener())

        //setting up picture and recyclerview
        val byteArray = arguments?.getByteArray("image")
        val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)

        if (bmp != null){
            binding.pictureImageView.setImageBitmap(bmp)
        } else{
            Toast.makeText(safeContext, "picture is null", Toast.LENGTH_LONG).show()
        }

        settingStickersRecyclerView()

        binding.addEmojiCardView.setOnClickListener{
            showStickers()
        }

        binding.doneCardView.setOnClickListener {
            saveImage()
        }

        return view
    }

    fun saveImage(){

        //setting visibility gone of what i dont want to appear in the picture
        binding.doneCardView.visibility = View.GONE
        binding.addEmojiCardView.visibility = View.GONE

        if (binding.stickersRecyclerView.visibility == View.VISIBLE){
            binding.stickersRecyclerView.visibility = View.GONE
        }else { }

        val pictureDone: Bitmap? = binding.constraintParentLayout.drawToBitmap() // bitmap of current layout's state

        // here too
        binding.doneCardView.visibility = View.VISIBLE
        binding.addEmojiCardView.visibility = View.VISIBLE
        if (binding.stickersRecyclerView.visibility == View.GONE){
            binding.stickersRecyclerView.visibility = View.VISIBLE
        }else { }

        val dir: File = File(safeContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"images")

        if (!dir.exists()){
            dir.mkdirs()
        }

        val file: File = File(dir, SimpleDateFormat(Constants.FILE_NAME_FORMAT,
                Locale.US).format(System.currentTimeMillis()) +".jpg")

        val fo = FileOutputStream(file)
        pictureDone!!.compress(Bitmap.CompressFormat.JPEG, 75, fo)
        Toast.makeText(safeContext, "image saved in ${dir.absolutePath}", Toast.LENGTH_LONG).show()
        fo.flush()
        fo.close()


    }

    class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector?): Boolean {

            factor *= detector!!.scaleFactor
            view1.scaleX = factor
            view1.scaleY = factor

            return super.onScale(detector)
        }
    }

    fun settingStickersRecyclerView(){
        images.add(R.drawable.glasses_meme)
        images.add(R.drawable.sexy_frog)
        images.add(R.drawable.sexy_pokeball)
        images.add(R.drawable.shrek_wazoski)
        images.add(R.drawable.smoke_meme)
        images.add(R.drawable.trollface)
        images.add(R.drawable.weird_smile)
        images.add(R.drawable.white_dude_crying)

        binding.stickersRecyclerView.layoutManager = LinearLayoutManager(safeContext, LinearLayoutManager.HORIZONTAL, false)
        binding.stickersRecyclerView.setHasFixedSize(true)
        binding.stickersRecyclerView.adapter = StickerPictureRecyclerViewAdapter(images, this)
    }

    fun showStickers(){
        if(binding.stickersRecyclerView.visibility == View.GONE){
            binding.stickersRecyclerView.visibility = View.VISIBLE
        } else {
            binding.stickersRecyclerView.visibility = View.GONE
        }
    }

    //recyclerview's onItemCLick
    override fun onItemClick(position: Int) {
        view1 = layoutInflater.inflate(R.layout.image_in_picture, null)
        val imageView = view1.findViewById<ImageView>(R.id.imageSticker_ImageView)
        imageView.setImageResource(images[position])
        imageView.elevation = 12f

        binding.constraintParentLayout.addView(view1)

        movingOnTouch(view1)

    }


    fun movingOnTouch(view: View){

        view.setOnTouchListener { v, event ->

            SGD_1.onTouchEvent(event)

            var xDown: Float = 0f
            var yDown: Float = 0f

            when(event.actionMasked){

                //the user just put his finger on view
                MotionEvent.ACTION_DOWN -> {
                    xDown = event.x; yDown = event.y;true
                }

                //the user moved his finger with the view
                MotionEvent.ACTION_MOVE -> {
                    var movedX: Float;
                    var movedY: Float

                    movedX = event.x
                    movedY = event.y

                    //calculate how much the user moved his finger
                    val distanceX = movedX - xDown
                    val distanceY = movedY - yDown

                    //now move the view to that position
                    view.x = view.x + distanceX
                    view.y = view.y + distanceY

                    //for next move event
                    xDown = movedX
                    yDown = movedY

                    true

                }
                else -> false
            }

        }
    }


}