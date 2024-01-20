package ru.ystu.mealmaster.presentation.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.BuildConfig
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.RecipeApi
import ru.ystu.mealmaster.data.RecipeApiService
import ru.ystu.mealmaster.data.RecipeRepositoryImpl
import ru.ystu.mealmaster.domain.NutritionalInfo
import ru.ystu.mealmaster.domain.RecipeData
import ru.ystu.mealmaster.domain.RecipeRepository
import ru.ystu.mealmaster.domain.enumeration.MeasureUnit
import ru.ystu.mealmaster.domain.enumeration.RecipeCategory
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import java.io.ByteArrayOutputStream
import java.io.InputStream


class AddRecipeActivity : AppCompatActivity() {
    private lateinit var layout: ConstraintLayout
    private lateinit var editTextIngredientsAmount: EditText
    private lateinit var btnAddIngredient: Button
    private lateinit var btnAddStep: Button
    private lateinit var btnSaveRecipe: ImageView
    private lateinit var backBtn: ImageView
    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var uploadImage: ImageView
    private lateinit var uploadedImageBase64: String

    private lateinit var allIngredients : Map<String, String>
    private lateinit var allSteps : Map<String, String>

    private val ingredientNameEditTexts = mutableListOf<EditText>()
    private val ingredientAmountEditTexts = mutableListOf<EditText>()
    private val stepEditTexts = mutableListOf<EditText>()

    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var editTextNutritionalAmount: EditText
    private lateinit var spinnerNutritionalUnit: Spinner
    private lateinit var editTextCalories: EditText
    private lateinit var editTextCarbohydrates: EditText
    private lateinit var editTextFat: EditText
    private lateinit var editTextProtein: EditText
    private lateinit var editTextCookingTime: EditText
    private lateinit var editTextIngredientName: EditText
    private lateinit var editTextIngredientAmount: EditText
    private lateinit var editTextStep: EditText
    private lateinit var editTextBase64String: EditText

    private lateinit var api: RecipeApiService
    private lateinit var repository: RecipeRepository
    private lateinit var interactor: RecipeInteractor

    private var lastAddedIngredientConstraintLayout: ConstraintLayout? = null
    private var lastAddedStepEditText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        RecipeApi.init(this)
        api = RecipeApi.api
        repository = RecipeRepositoryImpl(api, this)
        interactor = RecipeInteractorImpl(repository)

        layout = findViewById(R.id.activityAddRecipeConstraintLayout)

        initCategorySpinner()
        initNutritionalUnitSpinner()

        uploadImage = findViewById(R.id.uploadImage)
        editTextIngredientName = findViewById(R.id.editTextIngredientName)
        editTextIngredientAmount = findViewById(R.id.editTextIngredientAmount)
        editTextStep = findViewById(R.id.editTextSteps)
        btnAddIngredient = findViewById(R.id.btnAddIngredient)
        lastAddedIngredientConstraintLayout = findViewById(R.id.ingredientLayout)
        btnAddStep = findViewById(R.id.btnAddStep)

        editTextName = findViewById(R.id.editTextName)
        editTextDescription = findViewById(R.id.editTextDescription)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        editTextNutritionalAmount = findViewById(R.id.editTextNutritionalAmount)
        spinnerNutritionalUnit = findViewById(R.id.spinnerNutritionalUnit)
        editTextCalories = findViewById(R.id.editTextCalories)
        editTextCarbohydrates = findViewById(R.id.editTextCarbohydrates)
        editTextFat = findViewById(R.id.editTextFat)
        editTextProtein = findViewById(R.id.editTextProtein)
        editTextCookingTime = findViewById(R.id.editTextCookingTime)
        editTextStep = findViewById(R.id.editTextSteps)

        ingredientNameEditTexts.add(editTextIngredientName)
        ingredientAmountEditTexts.add(editTextIngredientAmount)
        stepEditTexts.add(editTextStep)

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val imageStream: InputStream? = contentResolver.openInputStream(uri)

                val fileSize = imageStream?.available() ?: 0
                val maxFileSize = BuildConfig.MAX_RECIPE_IMAGE_SIZE // 1048576 = 1MB
                if (fileSize > maxFileSize) {
                    showError("Размер файла не должен превышать ${BuildConfig.MAX_RECIPE_IMAGE_SIZE}")
                    return@registerForActivityResult
                }

                val selectedImage = BitmapFactory.decodeStream(imageStream)

                val maxWidth = BuildConfig.MAX_RECIPE_IMAGE_WIDTH
                val maxHeight = BuildConfig.MAX_RECIPE_IMAGE_HEIGHT
                if (selectedImage.width > maxWidth || selectedImage.height > maxHeight) {
                    showError("Разрешение изображения не должно превышать ${maxWidth}x${maxHeight}")
                    return@registerForActivityResult
                }

                val outputStream = ByteArrayOutputStream()
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                val byteArray = outputStream.toByteArray()

                val base64Image = Base64.encodeToString(byteArray, Base64.NO_WRAP)
                uploadedImageBase64 = base64Image
            }
        }

        uploadImage.setOnClickListener {
            selectImage()
        }

        btnAddIngredient.setOnClickListener {
            addNewIngredientFields()
        }

        btnAddStep.setOnClickListener {
            addNewEditText(this.editTextStep)
        }


        val categoryMapping = RecipeCategory.entries.associateBy(RecipeCategory::ordinal)
        val measureUnitMapping = MeasureUnit.entries.associateBy(MeasureUnit::ordinal)

        btnSaveRecipe = findViewById(R.id.buttonSaveRecipeCheck)

        btnSaveRecipe.setOnClickListener {
            collectData()
            val selectedCategoryPosition = spinnerCategory.selectedItemPosition
            val recipeCategoryEnum = categoryMapping[selectedCategoryPosition] ?: RecipeCategory.APPETIZERS // Значение по умолчанию, если что-то пойдет не так
            val selectedMeasureUnitPosition = spinnerNutritionalUnit.selectedItemPosition
            val measureUnitEnum = measureUnitMapping[selectedMeasureUnitPosition] ?: MeasureUnit.G // Значение по умолчанию, если что-то пойдет не так

            val recipe = RecipeData(
                name = editTextName.text.toString(),
                description = editTextDescription.text.toString(),
                category = recipeCategoryEnum.name,
                nutritionalInfo = NutritionalInfo(
                    amount = editTextNutritionalAmount.text.toString().toInt(),
                    unit = measureUnitEnum.name,
                    calories = editTextCalories.text.toString().toDouble(),
                    carbohydrates = editTextCarbohydrates.text.toString().toDouble(),
                    fat = editTextFat.text.toString().toDouble(),
                    protein = editTextProtein.text.toString().toDouble()
                ),
                cookingTime = editTextCookingTime.text.toString().toInt(),
                ingredients = allIngredients,
                steps = allSteps,
                image = uploadedImageBase64
            )

            addRecipe(recipe)
        }

        backBtn = findViewById(R.id.addRecipeBackBtn)
        backBtn.setOnClickListener { finish() }
    }

    private fun selectImage() {
        getContent.launch("image/*")
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Log.d("UPLOADED IMAGE", data.toString())
        }
    }

    private fun showError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 777
    }

    private fun initCategorySpinner() {
        val spinnerCategory = findViewById<Spinner>(R.id.spinnerCategory)
        val categoryAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.recipe_categories, android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter

    }

    private fun initNutritionalUnitSpinner() {
        val spinnerNutritionalUnit = findViewById<Spinner>(ru.ystu.mealmaster.R.id.spinnerNutritionalUnit)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.nutritional_units, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerNutritionalUnit.adapter = adapter
    }

    private fun addNewIngredientFields() {
        val newIngredientLayout = ConstraintLayout(this).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                0,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val newIngredientNameEditText = createNewEditText("Ингредиент")
        val newIngredientAmountEditText = createNewEditText("Количество")

        newIngredientLayout.addView(newIngredientNameEditText)
        newIngredientLayout.addView(newIngredientAmountEditText)

        layout.addView(newIngredientLayout)

        val constraintSet = ConstraintSet()

        constraintSet.clone(layout)
        val topAnchorId = lastAddedIngredientConstraintLayout?.id ?: R.id.btnAddIngredient
        constraintSet.connect(
            newIngredientLayout.id,
            ConstraintSet.TOP,
            topAnchorId,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            newIngredientLayout.id,
            ConstraintSet.START,
            layout.id,
            ConstraintSet.START
        )
        constraintSet.connect(
            newIngredientLayout.id,
            ConstraintSet.END,
            layout.id,
            ConstraintSet.END
        )
        constraintSet.applyTo(layout)

        constraintSet.clone(newIngredientLayout)

        constraintSet.connect(
            newIngredientNameEditText.id,
            ConstraintSet.START,
            newIngredientLayout.id,
            ConstraintSet.START
        )
        constraintSet.connect(
            newIngredientNameEditText.id,
            ConstraintSet.END, newIngredientAmountEditText.id,
            ConstraintSet.START,
            8
        )
        constraintSet.connect(
            newIngredientNameEditText.id,
            ConstraintSet.TOP,
            newIngredientLayout.id,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            newIngredientNameEditText.id,
            ConstraintSet.BOTTOM,
            newIngredientLayout.id,
            ConstraintSet.BOTTOM
        )

        constraintSet.connect(
            newIngredientAmountEditText.id,
            ConstraintSet.START,
            newIngredientNameEditText.id,
            ConstraintSet.END,
            8
        )
        constraintSet.connect(
            newIngredientAmountEditText.id,
            ConstraintSet.END, newIngredientLayout.id,
            ConstraintSet.END
        )
        constraintSet.connect(
            newIngredientAmountEditText.id,
            ConstraintSet.TOP,
            newIngredientLayout.id,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            newIngredientAmountEditText.id,
            ConstraintSet.BOTTOM,
            newIngredientLayout.id,
            ConstraintSet.BOTTOM
        )

        constraintSet.applyTo(newIngredientLayout)

        lastAddedIngredientConstraintLayout = newIngredientLayout

        constraintSet.clone(layout)
        constraintSet.connect(
            R.id.btnAddIngredient,
            ConstraintSet.TOP,
            newIngredientLayout.id,
            ConstraintSet.BOTTOM
        )
        constraintSet.applyTo(layout)

        ingredientNameEditTexts.add(newIngredientNameEditText)
        ingredientAmountEditTexts.add(newIngredientAmountEditText)
    }

    private fun createNewEditText(hint: String): EditText {
        return EditText(this).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(
                0,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 8, 8, 8)
            }
            setHint(hint)
            inputType = InputType.TYPE_CLASS_TEXT
        }
    }

    private fun addNewEditText(targetEditText: EditText) {
        val newEditText = EditText(this)
        newEditText.id = View.generateViewId()

        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        newEditText.layoutParams = layoutParams

        newEditText.hint = targetEditText.hint
        newEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE

        layout.addView(newEditText)

        val constraintSet = ConstraintSet()
        constraintSet.clone(layout)

        val lastEditText = lastAddedStepEditText ?: targetEditText

        constraintSet.connect(
            newEditText.id,
            ConstraintSet.TOP,
            lastEditText.id,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            newEditText.id,
            ConstraintSet.START,
            layout.id,
            ConstraintSet.START
        )
        constraintSet.connect(
            newEditText.id,
            ConstraintSet.END,
            layout.id,
            ConstraintSet.END
        )

        lastAddedStepEditText = newEditText

        constraintSet.connect(
            R.id.btnAddStep,
            ConstraintSet.TOP,
            newEditText.id,
            ConstraintSet.BOTTOM
        )

        constraintSet.applyTo(layout)

        stepEditTexts.add(newEditText)
    }

    private fun collectData() {
        allIngredients = gatherIngredients()
        allSteps = gatherSteps()

        allIngredients.forEach { (ingredient, amount) ->
            Log.d("Ingredients", "Ингредиент: $ingredient, Количество: $amount")
        }

        allSteps.forEach { (stepNumber, stepDescription) ->
            Log.d("Steps", "$stepNumber: $stepDescription")
        }
    }

    private fun addRecipe(recipe: RecipeData) {
        lifecycleScope.launch {
            try {
                interactor.addRecipe(recipe)

                intent = Intent(this@AddRecipeActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@AddRecipeActivity, "Failed to add recipe: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun gatherIngredients(): Map<String, String> {
        val ingredientsMap = mutableMapOf<String, String>()
        for (i in ingredientNameEditTexts.indices) {
            val name = ingredientNameEditTexts[i].text.toString()
            val amount = ingredientAmountEditTexts.getOrNull(i)?.text.toString()
            if (name.isNotEmpty() && amount.isNotEmpty()) {
                ingredientsMap[name] = amount
            }
        }
        return ingredientsMap
    }

    private fun gatherSteps(): Map<String, String> {
        val stepsMap = mutableMapOf<String, String>()
        stepEditTexts.forEachIndexed { index, editText ->
            val stepNumber = "${index + 1}"
            stepsMap[stepNumber] = editText.text.toString()
        }
        return stepsMap
    }

}