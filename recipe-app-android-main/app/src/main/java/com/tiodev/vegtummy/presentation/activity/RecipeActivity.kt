package com.tiodev.vegtummy.presentation.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.tiodev.vegtummy.R
import com.tiodev.vegtummy.data.RecipeApiService
import com.tiodev.vegtummy.data.RecipeRepositoryImpl
import com.tiodev.vegtummy.domain.Recipe
import java.util.UUID

class RecipeActivity : AppCompatActivity() {
    var img: ImageView? = null
    var backBtn: ImageView? = null
    var overlay: ImageView? = null
    var scroll: ImageView? = null
    var zoomImage: ImageView? = null
    var txt: TextView? = null
    var ing: TextView? = null
    var time: TextView? = null
    var steps: TextView? = null
    lateinit var ingList: Array<String>
    var stepBtn: Button? = null
    var ing_btn: Button? = null
    var isImgCrop = false
    var scrollView: ScrollView? = null
    var scrollView_step: ScrollView? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        val api = RecipeApiService.api
        val repository = RecipeRepositoryImpl(api)


        var recipeFromDb: Recipe? = null
//        intent.extras?.getString("CATEGORY")?.let { Log.d("BRUKVA", it) }
        intent.extras?.getString("CATEGORY")?.let { repository.getRecipesByCategory(it) {x ->
            Log.d("CHESNOK", x?.map { it.toString() } .toString())
        } }
//        repository.getRecipeById(UUID.fromString(intent.extras?.getString("RECIPE_ID"))) {
//            recipe ->
//            recipe?.let {
//                recipeFromDb = it
//                val ingredientsFormatted = recipeFromDb?.ingredients?.entries?.joinToString(separator = "\n") { (key, value) ->
//                    "• $key ― $value"
//                }
//                ing?.text = ingredientsFormatted
//                val stepsFormatted = recipeFromDb?.steps?.entries?.joinToString(separator = "\n") { (key, value) ->
//                    "$key) $value"
//                }
//                steps?.text = stepsFormatted
//                time?.text = it.cookingTime
//                txt?.text = it.name
//
//                if (!recipe.image.isNullOrEmpty()) {
//                    val imageNorm = recipe.image.replace("localhost", "10.0.2.2")
//                    Picasso.get().load(imageNorm).into(img)
//                    img?.visibility = View.VISIBLE
//                }
//                Log.d("KARTOFEL", it.steps.entries.toString())
//            }
//        }

        // Find views
        img = findViewById(R.id.recipe_img)
        txt = findViewById(R.id.tittle)
        ing = findViewById(R.id.ing)
        time = findViewById(R.id.time)
        stepBtn = findViewById(R.id.steps_btn)
        ing_btn = findViewById(R.id.ing_btn)
        backBtn = findViewById(R.id.back_btn)
        steps = findViewById(R.id.steps_txt)
        scrollView = findViewById(R.id.ing_scroll)
        scrollView_step = findViewById(R.id.steps)
        overlay = findViewById(R.id.image_gradient)


        Log.d("LUK SELENII", steps?.text.toString())
        stepBtn?.setTextColor(getColor(R.color.black))


        //        scroll = findViewById(R.id.scroll);
//        zoomImage = findViewById(R.id.zoom_image);

        // Load recipe image from link
        Glide.with(applicationContext).load(intent.getStringExtra("img"))
            .into(img!!)
        // Set recipe title
        txt?.setText(intent.getStringExtra("tittle"))

//        // Set recipe ingredients
//        ingList = getIntent().getStringExtra("ing").split("\n");
        // Set time
//        time.setText(ingList[0]);


//        for (int i = 1; i<ingList.length; i++){
//            ing.setText(ing.getText()+"\uD83D\uDFE2  "+ingList[i]+"\n");
        /*if(ingList[i].startsWith(" ")){
                ing.setText(ing.getText()+"\uD83D\uDFE2  "+ingList[i].trim().replaceAll("\\s{2,}", " ")+"\n");
            }else{

            }*/
//
//        }
        // Set recipe steps
//        steps?.setText(intent.getStringExtra("des"))
        // steps.setText(Html.fromHtml(getIntent().getStringExtra("des")));
        stepBtn?.setBackground(null)
        stepBtn?.setOnClickListener(View.OnClickListener {
            stepBtn?.setBackgroundResource(R.drawable.btn_ing)
            stepBtn?.setTextColor(getColor(R.color.white))
            ing_btn?.background = null
            ing_btn?.setTextColor(getColor(R.color.black))
            scrollView?.visibility = View.GONE
            scrollView_step?.visibility = View.VISIBLE
        })
        ing_btn?.setOnClickListener {
            ing_btn?.setBackgroundResource(R.drawable.btn_ing)
            ing_btn?.setTextColor(getColor(R.color.white))
            stepBtn?.background = null
            stepBtn?.setTextColor(getColor(R.color.black))
            scrollView?.visibility = View.VISIBLE
            scrollView_step?.visibility = View.GONE
        }


        // Exit activity
        backBtn?.setOnClickListener { finish() }
    }
}