package ru.ystu.mealmaster.presentation.activity

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import ru.ystu.mealmaster.R
import ru.ystu.mealmaster.data.RecipeApi


class AddRecipeActivity : AppCompatActivity() {
    private lateinit var layout: ConstraintLayout
    private lateinit var editTextIngredients: EditText
    private lateinit var editTextIngredientsAmount: EditText
    private lateinit var editTextSteps: EditText
    private lateinit var btnAddIngredient: Button
    private lateinit var btnAddStep: Button
    private lateinit var btnSaveRecipe: Button
    private lateinit var backBtn: ImageView

    private var lastAddedIngredientConstraintLayout: ConstraintLayout? = null
    private var lastAddedStepEditText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        RecipeApi.init(this)
        val api = RecipeApi.api

        layout = findViewById(R.id.activityAddRecipeConstraintLayout)

        initCategorySpinner()
        initNutritionalUnitSpinner()

        editTextIngredients = findViewById(R.id.editTextIngredientName)
        editTextIngredientsAmount = findViewById(R.id.editTextIngredientAmount)
        editTextSteps = findViewById(R.id.editTextSteps)
        btnAddIngredient = findViewById(R.id.btnAddIngredient)
        lastAddedIngredientConstraintLayout = findViewById(R.id.ingredientLayout)
        btnAddStep = findViewById(R.id.btnAddStep)

        btnAddIngredient.setOnClickListener {
            addNewIngredientFields()
        }

        btnAddStep.setOnClickListener {
            addNewEditText(editTextSteps)
        }

        btnSaveRecipe = findViewById(R.id.buttonSaveRecipe)
        // TODO: Реализовать логику добавления рецепта
//        btnSaveRecipe.setOnClickListener {
//            api.addRecipe(
//                Recipe(UUID.randomUUID(), ...)
//            ).enqueue(object : Callback<ApiResponseDto<Recipe>> {
//                override fun onResponse(
//                    call: Call<ApiResponseDto<Recipe>>,
//                    response: Response<ApiResponseDto<Recipe>>
//                ) {
//                    if (response.isSuccessful || response.code() == 301 || response.code() == 302) {
//
//                    } else {
//
//                    }
//                }
//
//                override fun onFailure(call: Call<ApiResponseDto<Recipe>>, t: Throwable) {
//
//                }
//            })
//        }

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

}