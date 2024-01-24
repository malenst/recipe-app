package ru.ystu.mealmaster.presentation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
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
import ru.ystu.mealmaster.data.repository.RecipeRepositoryImpl
import ru.ystu.mealmaster.domain.dto.NutritionalInfoDTO
import ru.ystu.mealmaster.domain.dto.RecipeData
import ru.ystu.mealmaster.domain.enumeration.MeasureUnit
import ru.ystu.mealmaster.domain.enumeration.RecipeCategory
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl
import ru.ystu.mealmaster.domain.repository.RecipeRepository
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.UUID

class UpdateRecipeActivity : AppCompatActivity() {
    private lateinit var layout: ConstraintLayout
    private lateinit var editTextIngredientsAmount: EditText
    private lateinit var btnAddIngredient: Button
    private lateinit var btnAddStep: Button
    private lateinit var btnSaveRecipe: ImageView
    private lateinit var activityAddRecipeTittle: TextView
    private lateinit var backBtn: ImageView
    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var uploadImage: ImageView
    private var uploadedImageBase64: String? = null

    private lateinit var allIngredients : Map<String, String>
    private lateinit var allSteps : Map<String, String>
    private lateinit var recipeIdString: String

    private val ingredientNameEditTexts = mutableListOf<EditText>()
    private val ingredientAmountEditTexts = mutableListOf<EditText>()
    private val stepEditTexts = mutableListOf<EditText>()

    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var spinnerMeasure: Spinner
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
        getRecipeById()

        activityAddRecipeTittle = findViewById(R.id.activityAddRecipeTittle)
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

        activityAddRecipeTittle.text = "Изменить рецепт"

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
        recipeIdString = intent.extras?.getString("RECIPE_ID")!!
        btnSaveRecipe.setOnClickListener {
            collectData()
            val selectedCategoryPosition = spinnerCategory.selectedItemPosition
            val recipeCategoryEnum = categoryMapping[selectedCategoryPosition] ?: RecipeCategory.APPETIZERS // Значение по умолчанию, если что-то пойдет не так
            val selectedMeasureUnitPosition = spinnerNutritionalUnit.selectedItemPosition
            val measureUnitEnum = measureUnitMapping[selectedMeasureUnitPosition] ?: MeasureUnit.G // Значение по умолчанию, если что-то пойдет не так

            val recipe = RecipeData(
                name = editTextName.text.toString().takeIf { it.isNotBlank() },
                description = editTextDescription.text.toString().takeIf { it.isNotBlank() },
                category = recipeCategoryEnum.name,
                nutritionalInfoDTO = NutritionalInfoDTO(
                    amount = editTextNutritionalAmount.text.toString().toIntOrNull(),
                    measureUnit = measureUnitEnum.name,
                    calories = editTextCalories.text.toString().toDoubleOrNull(),
                    carbohydrates = editTextCarbohydrates.text.toString().toDoubleOrNull(),
                    fat = editTextFat.text.toString().toDoubleOrNull(),
                    protein = editTextProtein.text.toString().toDoubleOrNull()
                ),
                cookingTime = editTextCookingTime.text.toString().toIntOrNull(),
                ingredients = allIngredients,
                steps = allSteps,
                image = uploadedImageBase64
            )

            updateRecipe(recipeIdString, recipe)
        }

        backBtn = findViewById(R.id.addRecipeBackBtn)
        backBtn.setOnClickListener { finish() }
    }

    @SuppressLint("SetTextI18n")
    private fun getRecipeById() {
        lifecycleScope.launch {
            try {
                recipeIdString = intent.extras?.getString("RECIPE_ID")
                    ?: throw IllegalArgumentException("Recipe ID not found.")
                val recipeFromDb = interactor.getRecipeById(UUID.fromString(recipeIdString))

                recipeFromDb.let { recipe ->
                    val ingredientsFormatted =
                        recipe.ingredients.entries.joinToString(separator = "\n") { (key, value) ->
                            "• $key ― $value"
                        }
                    editTextName.text = Editable.Factory.getInstance().newEditable(recipe.name)
                    editTextCookingTime.text = Editable.Factory.getInstance().newEditable(recipe.cookingTime)
                    editTextDescription.text = Editable.Factory.getInstance().newEditable(recipe.description)
                    editTextNutritionalAmount.text = Editable.Factory.getInstance().newEditable(recipe.nutritionalInfoDTO.amount.toString())

                    val adapter = spinnerCategory.adapter
                    val position = (0 until adapter.count).firstOrNull { adapter.getItem(it).toString() == recipe.category }
                        ?: 0
                    val adapterMeas = spinnerNutritionalUnit.adapter
                    val positionMeas = (0 until adapterMeas.count).firstOrNull { adapterMeas.getItem(it).toString() == recipe.nutritionalInfoDTO.measureUnit }
                        ?: 0
                    spinnerCategory.setSelection(position)
                    spinnerNutritionalUnit.setSelection(positionMeas)
                    editTextCalories.text = Editable.Factory.getInstance().newEditable(recipe.nutritionalInfoDTO.calories.toString())
                    editTextProtein.text = Editable.Factory.getInstance().newEditable(recipe.nutritionalInfoDTO.protein.toString())
                    editTextFat.text = Editable.Factory.getInstance().newEditable(recipe.nutritionalInfoDTO.fat.toString())
                    editTextCarbohydrates.text = Editable.Factory.getInstance().newEditable(recipe.nutritionalInfoDTO.carbohydrates.toString())


                    val sortedSteps = recipe.steps.entries
                        .sortedBy { (key, _) -> key.toIntOrNull() ?: Int.MAX_VALUE }


                    var firstElement = sortedSteps.firstOrNull()
                    if (firstElement != null) {
                        editTextStep.text = Editable.Factory.getInstance().newEditable(firstElement.value)
                    }


                    for ((_, value) in sortedSteps.drop(1)) {
                        val localEditStep = addNewEditText(editTextStep)
                        localEditStep.text = Editable.Factory.getInstance().newEditable(value)
                    }

                    editTextIngredientName.text = Editable.Factory.getInstance().newEditable(recipe.nutritionalInfoDTO.carbohydrates.toString())
                    editTextIngredientAmount.text = Editable.Factory.getInstance().newEditable(recipe.nutritionalInfoDTO.carbohydrates.toString())

                    firstElement = recipe.ingredients.entries.firstOrNull()
                    if (firstElement != null) {
                        editTextIngredientName.text = Editable.Factory.getInstance().newEditable(firstElement.key)
                        editTextIngredientAmount.text = Editable.Factory.getInstance().newEditable(firstElement.value)
                    }
                    for ((key, value) in recipe.ingredients.entries.drop(1)) {
                        val localIngFields = addNewIngredientFields()
                        localIngFields.first.text = Editable.Factory.getInstance().newEditable(key)
                        localIngFields.second.text = Editable.Factory.getInstance().newEditable(value)

                    }


                }
            } catch (e: Exception) {
                Log.e("RecipeLoadError", "Error loading recipe", e)
            }
        }
    }

    private fun selectImage() {
        getContent.launch("image/*")
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null) {
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

    private fun addNewIngredientFields(): Pair<EditText, EditText> {
        val prevId = View.generateViewId()
        val newIngredientLayout = ConstraintLayout(this).apply {
            id = prevId
            layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT).apply {
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                topToBottom = prevId
            }
        }

        val newIngredientNameEditText = createEditTextIngredientName()
        val newIngredientAmountEditText = createEditTextIngredientAmount()

        newIngredientNameEditText.id = View.generateViewId()
        newIngredientAmountEditText.id = View.generateViewId()

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

        return (Pair(newIngredientNameEditText, newIngredientAmountEditText))
    }

    private fun createEditTextIngredientName(): EditText {
        return EditText(this, null, 0, R.style.MyEditText).apply {
            id = R.id.editTextIngredientName
            setHint(R.string.edit_recipe_ingredient_name_text)
            inputType = InputType.TYPE_CLASS_TEXT
            layoutParams = ConstraintLayout.LayoutParams(240.dpToPx(), 55.dpToPx()).apply {
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                topMargin = 8.dpToPx()
                marginStart = (-58).dpToPx()
            }
        }
    }

    private fun createEditTextIngredientAmount(): EditText {
        return EditText(this, null, 0, R.style.MyEditText).apply {
            id = R.id.editTextIngredientAmount
            setHint(R.string.edit_recipe_ingredient_amount_text)
            inputType = InputType.TYPE_CLASS_TEXT
            layoutParams = ConstraintLayout.LayoutParams(130.dpToPx(), 55.dpToPx()).apply {
                topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                topMargin = 8.dpToPx()
                marginEnd = (-58).dpToPx()
            }
            setPaddingRelative(16.dpToPx(), 0, 0, 0)
        }
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

    private fun addNewEditText(targetEditText: EditText): EditText {
        val newEditText = EditText(this, null, 0, R.style.MyEditText)
        newEditText.id = View.generateViewId()

        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
            55.dpToPx()
        ).apply {
            topMargin = 8.dpToPx()
            marginStart = 16.dpToPx()
            marginEnd = 16.dpToPx()
        }
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
        return newEditText
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

    private fun updateRecipe(recipeId: String, recipe: RecipeData) {
        lifecycleScope.launch {
            try {
                interactor.updateRecipe(UUID.fromString(recipeId), recipe)

                intent = Intent(this@UpdateRecipeActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@UpdateRecipeActivity, "Failed to add recipe: ${e.message}", Toast.LENGTH_SHORT).show()
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