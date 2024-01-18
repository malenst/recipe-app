package ru.ystu.mealmaster.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.RecipeApi
import ru.ystu.mealmaster.data.RecipeApiService
import ru.ystu.mealmaster.data.RecipeRepositoryImpl
import ru.ystu.mealmaster.domain.*
import ru.ystu.mealmaster.domain.interactor.RecipeInteractor
import ru.ystu.mealmaster.domain.interactor.RecipeInteractorImpl


class AddRecipeActivity : AppCompatActivity() {
    private lateinit var layout: ConstraintLayout
    private lateinit var editTextIngredientsAmount: EditText
    private lateinit var btnAddIngredient: Button
    private lateinit var btnAddStep: Button
    private lateinit var btnSaveRecipe: Button
    private lateinit var backBtn: ImageView

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

        editTextIngredientName = findViewById(R.id.editTextIngredientName)
        editTextIngredientsAmount = findViewById(R.id.editTextIngredientAmount)
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
            val selectedCategoryPosition = spinnerCategory.selectedItemPosition
            val recipeCategoryEnum = categoryMapping[selectedCategoryPosition] ?: RecipeCategory.APPETIZERS // Значение по умолчанию, если что-то пойдет не так
            val selectedMeasureUnitPosition = spinnerCategory.selectedItemPosition
            val measureUnitEnum = measureUnitMapping[selectedMeasureUnitPosition] ?: MeasureUnit.G // Значение по умолчанию, если что-то пойдет не так

            val recipe = RecipeData(
                name = editTextName.text.toString(),
                description = editTextDescription.text.toString(),
                category = recipeCategoryEnum.name,
                nutritionalInfo = NutritionalInfo(
                    amount = editTextNutritionalAmount.text.toString().toInt(),
                    unit = measureUnitEnum.name,
                    calories = editTextCalories.text.toString().toInt(),
                    carbohydrates = editTextCarbohydrates.text.toString().toDouble(),
                    fat = editTextFat.text.toString().toDouble(),
                    protein = editTextProtein.text.toString().toDouble()
                ),
                cookingTime = editTextCookingTime.text.toString().toInt(),
                ingredients = mapOf(Pair(editTextIngredientName.text.toString(), editTextIngredientsAmount.text.toString())),
                steps = mapOf(Pair("1", editTextStep.text.toString()))/*,
                image = upload*/
            )

            addRecipe(recipe)
        }

        backBtn = findViewById(R.id.addRecipeBackBtn)
        backBtn.setOnClickListener { finish() }
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
    }

    private fun addRecipe(recipe: RecipeData) {
        lifecycleScope.launch {
            try {
                interactor.addRecipe(recipe)

                intent = Intent(this@AddRecipeActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@AddRecipeActivity, "Failed to add recipe", Toast.LENGTH_SHORT).show()
            }
        }
    }

}