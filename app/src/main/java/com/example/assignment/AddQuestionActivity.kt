package com.example.assignment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AddQuestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)
    }

    override fun onBackPressed() {
        val builder =  AlertDialog.Builder(this);
        builder.setMessage("Doresti sa salvezi intrebarea inainte de a iesi de pe pagina?");
        builder.setCancelable(false);
        builder.apply {
            setPositiveButton("Da",
                DialogInterface.OnClickListener { dialog, id ->
                    val questionText = findViewById<EditText>(R.id.addQuestionInput)
                    val answerText = findViewById<EditText>(R.id.addAnswerInput)
                    val valueText = findViewById<EditText>(R.id.addValueInput)
                    val insertedNumber= valueText.text.toString().toIntOrNull();
                    val categoryText = findViewById<EditText>(R.id.addCategoryInput)

                    val questionErrorText = findViewById<TextView>(R.id.questionErrorText)
                    questionErrorText.text = "";
                    val answerErrorText = findViewById<TextView>(R.id.answerErrorText)
                    answerErrorText.text = "";
                    val valueErrorText = findViewById<TextView>(R.id.valueErrorText)
                    valueErrorText.text = "";
                    val categoryErrorText = findViewById<TextView>(R.id.categoryErrorText)
                    categoryErrorText.text = "";


                    var ok = true
                    if(questionText.text.length <= 5) {
                        ok = false
                        questionErrorText.text = "Intrebarea este prea scurta. Sunt necesare minim 6 caractere."
                    }

                    if(answerText.text.length <= 5) {
                        ok = false
                        answerErrorText.text = "raspunsul este prea scurt. Sunt necesare minim 6 caractere."
                    }

                    if(insertedNumber == null) {

                        ok = false
                        valueErrorText.text = "Introdu un numar"
                    }
                    else {

                        if((insertedNumber > 150) || (insertedNumber < 50)) {

                            ok = false
                            valueErrorText.text = "Valoarea trebuie sa fie intre 50 si 150"
                        }
                    }

                    if(categoryText.text.toString() != "Music"  && categoryText.text.toString() !="Geography"
                        && categoryText.text.toString() != "History" && categoryText.text.toString() !="Personalities" ) {

                        ok = false
                        categoryErrorText.text = "Categoriile sunt: Music, History, Geography,Personalities"
                    }
                    if(ok)
                    {
                        val intent= Intent()
                        intent.putExtra("question",questionText.text.toString())
                        intent.putExtra("answer",answerText.text.toString())
                        intent.putExtra("value", insertedNumber);
                        intent.putExtra("category",categoryText.text.toString())
                        setResult(RESULT_OK,intent)
                        finish()
                    }

                })
            setNegativeButton("Nu",
                DialogInterface.OnClickListener { dialog, id ->
                    finish();
                })
        }
        builder.create().show();
    }
}